package pl.edu.agh.idziak.asw.utils.test.grid2d.io;

import com.google.common.collect.ImmutableList;
import pl.edu.agh.idziak.asw.common.Utils;
import pl.edu.agh.idziak.asw.impl.grid2d.G2DCollectiveState;
import pl.edu.agh.idziak.asw.impl.grid2d.G2DEntityState;
import pl.edu.agh.idziak.asw.impl.grid2d.G2DInputPlan;
import pl.edu.agh.idziak.asw.impl.grid2d.G2DStateSpace;
import pl.edu.agh.idziak.asw.impl.gridarray.G2DOptCollectiveState;
import pl.edu.agh.idziak.asw.impl.gridarray.G2DOptInputPlan;
import pl.edu.agh.idziak.asw.impl.gridarray.G2DOptStateSpace;
import pl.edu.agh.idziak.asw.utils.test.grid2d.InvalidInputException;
import pl.edu.agh.idziak.asw.utils.test.grid2d.model.Entity;
import pl.edu.agh.idziak.asw.utils.test.grid2d.model.OptimizedTestCase;
import pl.edu.agh.idziak.asw.utils.test.grid2d.model.TestCase;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static java.util.Comparator.comparing;

/**
 * Created by Tomasz on 04.09.2016.
 */
public class DTOMapper {

    public static TestCase dtoToInternal(TestCaseDTO testCaseDTO) {
        List<EntityDTO> entities = testCaseDTO.getEntities();
        int numEntities = entities.size();
        G2DStateSpace stateSpace = mapStateSpace(testCaseDTO);

        Map<Object, G2DEntityState> initialStates = new HashMap<>(numEntities);
        Map<Object, G2DEntityState> targetStates = new HashMap<>(numEntities);

        entities.sort(comparing(EntityDTO::getId));

        for (EntityDTO entityDTO : entities) {
            Entity entity = Entity.newBuilder().id(entityDTO.getId()).build();

            Integer entityRow = entityDTO.getRow();
            Integer entityCol = entityDTO.getCol();

            validateEntityRow(stateSpace, entity, entityRow);
            validateEntityColumn(stateSpace, entity, entityCol);

            initialStates.put(entity, G2DEntityState.of(entityRow, entityCol));
            targetStates.put(entity, G2DEntityState.of(entityDTO.getTargetRow(), entityDTO.getTargetCol()));
        }
        validateStatesUniquePositions(targetStates);
        validateStatesUniquePositions(initialStates);

        G2DCollectiveState initialState = G2DCollectiveState.from(initialStates);
        G2DCollectiveState targetState = G2DCollectiveState.from(targetStates);

        G2DInputPlan g2DInputPlan = new G2DInputPlan(
                initialState.getEntityStates().keySet(), stateSpace, initialState, targetState);

        return new TestCase(testCaseDTO.getName(), g2DInputPlan, DTOMapper.isLightlyDefined(testCaseDTO));
    }

    public static OptimizedTestCase dtoToInternalOpt(TestCaseDTO testCaseDTO) {
        List<EntityDTO> entityDTOS = testCaseDTO.getEntities();
        int numEntities = entityDTOS.size();
        G2DOptStateSpace stateSpace = mapStateSpaceOpt(testCaseDTO);

        entityDTOS.sort(comparing(EntityDTO::getId));

        byte[] initialStateArray = new byte[numEntities * 2];
        byte[] targetStateArray = new byte[numEntities * 2];
        int i = 0;
        ImmutableList.Builder<Entity> entities = ImmutableList.builder();

        for (EntityDTO entityDTO : entityDTOS) {
            Entity entity = Entity.newBuilder().id(entityDTO.getId()).build();
            entities.add(entity);

            // validateEntityRow(stateSpace, entity, entityRow);
            // validateEntityColumn(stateSpace, entity, entityCol);

            initialStateArray[i] = entityDTO.getRow().byteValue();
            initialStateArray[i + 1] = entityDTO.getCol().byteValue();

            targetStateArray[i] = entityDTO.getTargetRow().byteValue();
            targetStateArray[i + 1] = entityDTO.getTargetCol().byteValue();
            i += 2;
        }

        G2DOptCollectiveState initialState = new G2DOptCollectiveState(initialStateArray);
        G2DOptCollectiveState targetState = new G2DOptCollectiveState(targetStateArray);

        G2DOptInputPlan g2DInputPlan = new G2DOptInputPlan(
                entities.build(), stateSpace, initialState, targetState);

        return new OptimizedTestCase(testCaseDTO.getName(), g2DInputPlan, DTOMapper.isLightlyDefined(testCaseDTO));
    }

    private static void validateEntityColumn(G2DStateSpace stateSpace, Entity entity, Integer entityCol) {
        if (entityCol >= stateSpace.countCols() || entityCol < 0) {
            throw new InvalidInputException(
                    format("Position of entity %s is beyond the boundaries of state space. Entity column was %s, state space has %s columns.",
                            entity, entityCol, stateSpace.countCols()));
        }
    }

    private static void validateEntityRow(G2DStateSpace stateSpace, Entity entity, Integer entityRow) {
        if (entityRow >= stateSpace.countRows() || entityRow < 0) {
            throw new InvalidInputException(
                    format("Position of entity %s is beyond the boundaries of state space. Entity row was %s, state space has %s rows.",
                            entity, entityRow, stateSpace.countRows()));
        }
    }

    private static void validateStatesUniquePositions(Map<Object, G2DEntityState> states) {
        HashSet<G2DEntityState> statesSet = new HashSet<>(states.values());
        if (statesSet.size() != states.size()) {
            throw new InvalidInputException("Initial or target states of entities are not unique: " + states);
        }
    }

    private static G2DStateSpace mapStateSpace(TestCaseDTO testCaseDTO) {
        G2DStateSpace stateSpace;
        if (testCaseDTO.getStateSpace() != null) {
            stateSpace = new G2DStateSpace(testCaseDTO.getStateSpace());
        } else {
            if (!isLightlyDefined(testCaseDTO)) {
                throw new RuntimeException("Missing state space definition in " + testCaseDTO);
            }
            Integer rows = testCaseDTO.getStateSpaceRows();
            Integer cols = testCaseDTO.getStateSpaceCols();
            stateSpace = new G2DStateSpace(new int[rows][cols]);
        }
        return stateSpace;
    }

    private static G2DOptStateSpace mapStateSpaceOpt(TestCaseDTO testCaseDTO) {
        G2DOptStateSpace stateSpace;
        if (testCaseDTO.getStateSpace() != null) {
            stateSpace = new G2DOptStateSpace(Utils.toByteArray(testCaseDTO.getStateSpace()));
        } else {
            if (!isLightlyDefined(testCaseDTO)) {
                throw new RuntimeException("Missing state space definition in " + testCaseDTO);
            }
            Integer rows = testCaseDTO.getStateSpaceRows();
            Integer cols = testCaseDTO.getStateSpaceCols();
            stateSpace = new G2DOptStateSpace(Utils.toByteArray(new int[rows][cols]));
        }
        return stateSpace;
    }

    private static boolean isLightlyDefined(TestCaseDTO testCaseDTO) {
        return testCaseDTO.getStateSpaceCols() != null && testCaseDTO.getStateSpaceRows() != null;
    }

}
