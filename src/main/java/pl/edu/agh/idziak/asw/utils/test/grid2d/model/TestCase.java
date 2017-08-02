package pl.edu.agh.idziak.asw.utils.test.grid2d.model;

import pl.edu.agh.idziak.asw.impl.ExtendedOutputPlan;
import pl.edu.agh.idziak.asw.impl.grid2d.GridCollectiveState;
import pl.edu.agh.idziak.asw.impl.grid2d.GridInputPlan;
import pl.edu.agh.idziak.asw.impl.grid2d.GridStateSpace;

/**
 * Created by Tomasz on 27.08.2016.
 */
public class TestCase {

    private GridInputPlan inputPlan;
    private String name;
    private boolean sparseDefinition;
    private ExtendedOutputPlan<GridStateSpace, GridCollectiveState> outputPlan;

    public TestCase(String name, GridInputPlan inputPlan, boolean sparseDefinition) {
        this.name = name;
        this.inputPlan = inputPlan;
        this.sparseDefinition = sparseDefinition;
    }

    public GridInputPlan getInputPlan() {
        return inputPlan;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSparseDefinition() {
        return sparseDefinition;
    }

    public void setOutputPlan(ExtendedOutputPlan<GridStateSpace, GridCollectiveState> outputPlan) {
        this.outputPlan = outputPlan;
    }

    public ExtendedOutputPlan<GridStateSpace, GridCollectiveState> getExtendedOutputPlan() {
        return outputPlan;
    }
}
