package ua.ithillel.javapro.orm;

import ua.ithillel.javapro.OrmException;
import ua.ithillel.javapro.reflection.Column;
import ua.ithillel.javapro.reflection.Id;
import ua.ithillel.javapro.reflection.Table;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class OrmUtil {

    private static final Map<Class<?>, OrmEntityMetaInfo<?>> entityMetaInfoByType = new HashMap<>();

    public static void collect(Class... types) {

        for (Class type : types) {

            Table table = (Table) type.getAnnotation(Table.class);

            if (table == null) {
                throw new OrmException("Table annotation not found for type " + type);
            }

            Field primaryKeyField = null;
            List<OrmEntityColumn> columns = new ArrayList<>();

            for (Field field : type.getDeclaredFields()) {

                if (primaryKeyField == null) {

                    Id id = field.getAnnotation(Id.class);

                    if (id != null) {
                        primaryKeyField = field;
                    }

                }

                String name = field.getName();

                Column column = field.getAnnotation(Column.class);

                if (column != null) {
                    name = column.name();
                }

                columns.add(new OrmEntityColumn(field, name));

            }

            if (primaryKeyField == null) {
                throw new OrmException("Primary key not found for type " + type);
            }

            Constructor<?> defaultConstructor = null;
            try {
                defaultConstructor = type.getDeclaredConstructor();
            } catch (NoSuchMethodException e) {
                throw new OrmException("Default constructor not found for type " + type);
            }

            entityMetaInfoByType.put(
                    type,
                    new OrmEntityMetaInfo<>(
                            type,
                            new OrmEntityPrimaryKey(primaryKeyField.getName(), primaryKeyField),
                            table.name(),
                            columns,
                            defaultConstructor
                    )
            );

        }

    }

    public static Optional<OrmEntityMetaInfo> getMetaInfo(Class type) {
        return Optional.ofNullable(entityMetaInfoByType.get(type));
    }

}
