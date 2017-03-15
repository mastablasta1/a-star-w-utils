package pl.edu.agh.idziak.asw.utils.test.grid2d.model;

import pl.edu.agh.idziak.asw.impl.ExtendedOutputPlan;
import pl.edu.agh.idziak.asw.impl.grid2d.G2DCollectiveState;
import pl.edu.agh.idziak.asw.impl.grid2d.G2DInputPlan;
import pl.edu.agh.idziak.asw.impl.grid2d.G2DStateSpace;

/**
 * Created by Tomasz on 27.08.2016.
 */
public class TestCase {

    private G2DInputPlan inputPlan;
    private String name;
    private boolean sparseDefinition;
    private ExtendedOutputPlan<G2DStateSpace, G2DCollectiveState> outputPlan;

    public TestCase(String name, G2DInputPlan inputPlan, boolean sparseDefinition) {
        this.name = name;
        this.inputPlan = inputPlan;
        this.sparseDefinition = sparseDefinition;
    }

    public G2DInputPlan getInputPlan() {
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

    public void setOutputPlan(ExtendedOutputPlan<G2DStateSpace, G2DCollectiveState> outputPlan) {
        this.outputPlan = outputPlan;
    }

    public ExtendedOutputPlan<G2DStateSpace, G2DCollectiveState> getExtendedOutputPlan() {
        return outputPlan;
    }
}
