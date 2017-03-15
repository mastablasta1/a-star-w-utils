package pl.edu.agh.idziak.asw.utils.test.grid2d.io;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.common.base.MoreObjects;

import java.util.List;

/**
 * Created by Tomasz on 27.08.2016.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"name", "stateSpace", "stateSpaceRows", "stateSpaceCols", "entities"})
public class TestCaseDTO {
    private String name;
    private int[][] stateSpace;
    private Integer stateSpaceRows;
    private Integer stateSpaceCols;
    private List<EntityDTO> entities;

    public TestCaseDTO() {
    }

    private TestCaseDTO(Builder builder) {
        stateSpace = builder.stateSpace;
        stateSpaceRows = builder.stateSpaceRows;
        stateSpaceCols = builder.stateSpaceCols;
        entities = builder.entities;
        name = builder.name;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public int[][] getStateSpace() {
        return stateSpace;
    }

    public List<EntityDTO> getEntities() {
        return entities;
    }

    public String getName() {
        return name;
    }

    public Integer getStateSpaceCols() {
        return stateSpaceCols;
    }

    public Integer getStateSpaceRows() {
        return stateSpaceRows;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("stateSpace", stateSpace)
                .add("stateSpaceRows", stateSpaceRows)
                .add("stateSpaceCols", stateSpaceCols)
                .add("entities", entities)
                .toString();
    }

    public static final class Builder {
        private int[][] stateSpace;
        private Integer stateSpaceRows;
        private Integer stateSpaceCols;
        private List<EntityDTO> entities;
        private String name;

        private Builder() {
        }

        public Builder stateSpace(int[][] val) {
            stateSpace = val;
            return this;
        }

        public Builder stateSpaceRows(Integer val) {
            stateSpaceRows = val;
            return this;
        }

        public Builder stateSpaceCols(Integer val) {
            stateSpaceCols = val;
            return this;
        }

        public Builder entities(List<EntityDTO> val) {
            entities = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public TestCaseDTO build() {
            return new TestCaseDTO(this);
        }
    }


}
