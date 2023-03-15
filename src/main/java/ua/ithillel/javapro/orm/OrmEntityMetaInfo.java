package ua.ithillel.javapro.orm;

import java.lang.reflect.Constructor;
import java.util.List;

public class OrmEntityMetaInfo<T> {

    private final Class<T> type;

    private final String tableName;

    private final List<OrmEntityColumn> columns;

    private final OrmEntityPrimaryKey primaryKey;

    private final Constructor<T> defaultConstructor;

    public OrmEntityMetaInfo(Class<T> type, OrmEntityPrimaryKey primaryKey, String tableName, List<OrmEntityColumn> columns, Constructor<T> defaultConstructor) {
        this.type = type;
        this.primaryKey = primaryKey;
        this.tableName = tableName;
        this.columns = columns;
        this.defaultConstructor = defaultConstructor;
    }

    public Class<T> getType() {
        return type;
    }

    public String getTableName() {
        return tableName;
    }

    public List<OrmEntityColumn> getColumns() {
        return columns;
    }

    public OrmEntityPrimaryKey getPrimaryKey() {
        return primaryKey;
    }

    public Constructor<T> getDefaultConstructor() {
        return defaultConstructor;
    }

}
