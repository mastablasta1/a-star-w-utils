package pl.edu.agh.idziak.asw.utils.test.grid2d.io;

import com.google.common.collect.ImmutableList;
import pl.edu.agh.idziak.asw.common.Utils;
import pl.edu.agh.idziak.asw.impl.grid2d.GridCollectiveState;
import pl.edu.agh.idziak.asw.impl.grid2d.GridEntityState;
import pl.edu.agh.idziak.asw.impl.grid2d.GridInputPlan;
import pl.edu.agh.idziak.asw.impl.grid2d.GridStateSpace;
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
        GridStateSpace stateSpace = mapStateSpace(testCaseDTO);

        Map<Object, GridEntityState> initialStates = new HashMap<>(numEntities);
        Map<Object, GridEntityState> targetStates = new HashMap<>(numEntities);

        entities.sort(comparing(EntityDTO::getId));

        for (EntityDTO entityDTO : entities) {
            Entity entity = Entity.newBuilder().id(entityDTO.getId()).build();

            Integer entityRow = entityDTO.getRow();
            Integer entityCol = entityDTO.getCol();

            validateEntityRow(stateSpace, entity, entityRow);
            validateEntityColumn(stateSpace, entity, entityCol);

            initialStates.put(entity, GridEntityState.of(entityRow, entityCol));
            targetStates.put(entity, GridEntityState.of(entityDTO.getTargetRow(), entityDTO.getTargetCol()));
        }
        validateStatesUniquePositions(targetStates);
        validateStatesUniquePositions(initialStates);

        GridCollectiveState initialState = GridCollectiveState.from(initialStates);
        GridCollectiveState targetState = GridCollectiveState.from(targetStates);

        GridInputPlan gridInputPlan = new GridInputPlan(
                initialState.getEntityStates().keySet(), stateSpace, initialState, targetState);

        return new TestCase(testCaseDTO.getName(), gridInputPlan, DTOMapper.isLightlyDefined(testCaseDTO));
    }

    public static OptimizedTestCase dtoToInternalOpt(TestCaseDTO testCaseDTO) {
        List<EntityDTO> entityDTOS = testCaseDTO.getEntities();
        int numEntities = entityDTOS.size();
        GridStateSpace stateSpace = mapStateSpaceOpt(testCaseDTO);

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

        GridCollectiveState initialState = new GridCollectiveState(initialStateArray);
        GridCollectiveState targetState = new GridCollectiveState(targetStateArray);

        GridInputPlan gridInputPlan = new GridInputPlan(
                entities.build(), stateSpace, initialState, targetState);

        return new OptimizedTestCase(testCaseDTO.getName(), gridInputPlan, DTOMapper.isLightlyDefined(testCaseDTO));
    }

    private static void validateEntityColumn(GridStateSpace stateSpace, Entity entity, Integer entityCol) {
        if (entityCol >= stateSpace.getCols() || entityCol < 0) {
            throw new InvalidInputException(
                    format("Position of entity %s is beyond the boundaries of state space. Entity column was %s, state space has %s columns.",
                            entity, entityCol, stateSpace.getCols()));
        }
    }

    private static void validateEntityRow(GridStateSpace stateSpace, Entity entity, Integer entityRow) {
        if (entityRow >= stateSpace.getRows() || entityRow < 0) {
            throw new InvalidInputException(
                    format("Position of entity %s is beyond the boundaries of state space. Entity row was %s, state space has %s rows.",
                            entity, entityRow, stateSpace.getRows()));
        }
    }

    private static void validateStatesUniquePositions(Map<Object, GridEntityState> states) {
        HashSet<GridEntityState> statesSet = new HashSet<>(states.values());
        if (statesSet.size() != states.size()) {
            throw new InvalidInputException("Initial or target states of entities are not unique: " + states);
        }
    }

    private static GridStateSpace mapStateSpace(TestCaseDTO testCaseDTO) {
        GridStateSpace stateSpace;
        if (testCaseDTO.getStateSpace() != null) {
            stateSpace = new GridStateSpace(testCaseDTO.getStateSpace());
        } else {
            if (!isLightlyDefined(testCaseDTO)) {
                throw new RuntimeException("Missing state space definition in " + testCaseDTO);
            }
            Integer rows = testCaseDTO.getStateSpaceRows();
            Integer cols = testCaseDTO.getStateSpaceCols();
            stateSpace = new GridStateSpace(new int[rows][cols]);
        }
        return stateSpace;
    }

    private static GridStateSpace mapStateSpaceOpt(TestCaseDTO testCaseDTO) {
        GridStateSpace stateSpace;
        if (testCaseDTO.getStateSpace() != null) {
            stateSpace = new GridStateSpace(Utils.toByteArray(testCaseDTO.getStateSpace()));
        } else {
            if (!isLightlyDefined(testCaseDTO)) {
                throw new RuntimeException("Missing state space definition in " + testCaseDTO);
            }
            Integer rows = testCaseDTO.getStateSpaceRows();
            Integer cols = testCaseDTO.getStateSpaceCols();
            stateSpace = new GridStateSpace(Utils.toByteArray(new int[rows][cols]));
        }
        return stateSpace;
    }

    private static boolean isLightlyDefined(TestCaseDTO testCaseDTO) {
        return testCaseDTO.getStateSpaceCols() != null && testCaseDTO.getStateSpaceRows() != null;
    }

}
