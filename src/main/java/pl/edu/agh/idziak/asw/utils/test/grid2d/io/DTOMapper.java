package pl.edu.agh.idziak.asw.utils.test.grid2d.io;

import pl.edu.agh.idziak.asw.impl.grid2d.G2DCollectiveState;
import pl.edu.agh.idziak.asw.impl.grid2d.G2DEntityState;
import pl.edu.agh.idziak.asw.impl.grid2d.G2DInputPlan;
import pl.edu.agh.idziak.asw.impl.grid2d.G2DStateSpace;
import pl.edu.agh.idziak.asw.utils.test.grid2d.InvalidInputException;
import pl.edu.agh.idziak.asw.utils.test.grid2d.model.Entity;
import pl.edu.agh.idziak.asw.utils.test.grid2d.model.TestCase;

import java.util.*;

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

    public static TestCaseDTO internalToDto(TestCase testCase) {
        TestCaseDTO.Builder builder = TestCaseDTO.newBuilder().name(testCase.getName());
        G2DStateSpace stateSpace = testCase.getInputPlan().getStateSpace();
        if (testCase.isSparseDefinition()) {
            builder.stateSpaceCols(stateSpace.countCols());
            builder.stateSpaceRows(stateSpace.countRows());
        } else {
            builder.stateSpace(stateSpace.getGridArray());
        }
        return builder
                .entities(inputPlanToEntityDTOs(testCase.getInputPlan()))
                .build();
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

    private static boolean isLightlyDefined(TestCaseDTO testCaseDTO) {
        return testCaseDTO.getStateSpaceCols() != null && testCaseDTO.getStateSpaceRows() != null;
    }

    private static List<EntityDTO> inputPlanToEntityDTOs(G2DInputPlan inputPlan) {
        Set<?> entities = inputPlan.getEntities();
        G2DCollectiveState initialCollectiveState = inputPlan.getInitialCollectiveState();
        G2DCollectiveState targetCollectiveState = inputPlan.getTargetCollectiveState();

        List<EntityDTO> dtoList = new ArrayList<>(entities.size());

        for (Object entity : entities) {
            G2DEntityState initialState = initialCollectiveState.getStateForEntity(entity);
            G2DEntityState targetState = targetCollectiveState.getStateForEntity(entity);

            EntityDTO.Builder builder = EntityDTO.newBuilder()
                                                 .row(initialState.getRow())
                                                 .col(initialState.getCol())
                                                 .targetRow(targetState.getRow())
                                                 .targetCol(targetState.getCol());

            if (entity instanceof Entity) {
                Entity entity1 = (Entity) entity;
                builder.id(entity1.getId());
            }

            dtoList.add(builder.build());
        }
        return dtoList;
    }
}
