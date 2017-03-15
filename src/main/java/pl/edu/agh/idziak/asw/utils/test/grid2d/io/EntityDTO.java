package pl.edu.agh.idziak.asw.utils.test.grid2d.io;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.common.base.MoreObjects;

/**
 * Created by Tomasz on 27.08.2016.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "row", "col", "targetRow", "targetCol"})
public class EntityDTO {
    private Integer id;
    private Integer row;
    private Integer col;
    private Integer targetRow;
    private Integer targetCol;

    public EntityDTO() {
    }

    private EntityDTO(Builder builder) {
        row = builder.row;
        col = builder.col;
        targetRow = builder.targetRow;
        targetCol = builder.targetCol;
        id = builder.id;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public Integer getRow() {
        return row;
    }

    public Integer getCol() {
        return col;
    }

    public Integer getTargetRow() {
        return targetRow;
    }

    public Integer getTargetCol() {
        return targetCol;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("row", row)
                .add("col", col)
                .add("targetRow", targetRow)
                .add("targetCol", targetCol)
                .add("id", id)
                .toString();
    }


    public static final class Builder {
        private Integer row;
        private Integer col;
        private Integer targetRow;
        private Integer targetCol;
        private Integer id;

        private Builder() {
        }

        public Builder row(Integer val) {
            row = val;
            return this;
        }

        public Builder col(Integer val) {
            col = val;
            return this;
        }

        public Builder targetRow(Integer val) {
            targetRow = val;
            return this;
        }

        public Builder targetCol(Integer val) {
            targetCol = val;
            return this;
        }

        public Builder id(Integer val) {
            id = val;
            return this;
        }

        public EntityDTO build() {
            return new EntityDTO(this);
        }
    }
}
