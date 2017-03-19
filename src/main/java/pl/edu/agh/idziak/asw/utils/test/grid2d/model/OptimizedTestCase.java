package pl.edu.agh.idziak.asw.utils.test.grid2d.model;

import pl.edu.agh.idziak.asw.impl.ExtendedOutputPlan;
import pl.edu.agh.idziak.asw.impl.gridarray.G2DOptCollectiveState;
import pl.edu.agh.idziak.asw.impl.gridarray.G2DOptInputPlan;
import pl.edu.agh.idziak.asw.impl.gridarray.G2DOptStateSpace;

/**
 * Created by Tomasz on 27.08.2016.
 */
public class OptimizedTestCase {

    private G2DOptInputPlan inputPlan;
    private String name;
    private boolean sparseDefinition;
    private ExtendedOutputPlan<G2DOptStateSpace, G2DOptCollectiveState> outputPlan;

    public OptimizedTestCase(String name, G2DOptInputPlan inputPlan, boolean sparseDefinition) {
        this.name = name;
        this.inputPlan = inputPlan;
        this.sparseDefinition = sparseDefinition;
    }

    public G2DOptInputPlan getInputPlan() {
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

    public void setOutputPlan(ExtendedOutputPlan<G2DOptStateSpace, G2DOptCollectiveState> outputPlan) {
        this.outputPlan = outputPlan;
    }

    public ExtendedOutputPlan<G2DOptStateSpace, G2DOptCollectiveState> getExtendedOutputPlan() {
        return outputPlan;
    }
}
