package ua.ithillel.javapro.orm;

import java.lang.reflect.Field;

public class OrmEntityColumn {

    private Field field;

    private String name;

    public OrmEntityColumn(Field field, String name) {
        this.field = field;
        this.name = name;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
