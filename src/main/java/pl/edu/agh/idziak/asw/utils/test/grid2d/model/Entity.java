package pl.edu.agh.idziak.asw.utils.test.grid2d.model;

/**
 * Created by Tomasz on 27.08.2016.
 */
public class Entity {
    private Integer id;

    private Entity(Builder builder) {
        id = builder.id;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {
        return "E" + id;
    }

    public static final class Builder {
        private Integer id;

        private Builder() {
        }

        public Builder id(Integer val) {
            id = val;
            return this;
        }

        public Entity build() {
            return new Entity(this);
        }
    }


}
