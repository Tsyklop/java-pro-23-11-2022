package ua.ithillel.javapro.orm;

import java.lang.reflect.Field;

public class OrmEntityPrimaryKey {

    private final String name;

    private final Field field;

    public OrmEntityPrimaryKey(String name, Field field) {
        this.name = name;
        this.field = field;
    }

    public String getName() {
        return name;
    }

    public Field getField() {
        return field;
    }

}
