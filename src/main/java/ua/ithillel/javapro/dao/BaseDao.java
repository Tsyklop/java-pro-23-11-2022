package ua.ithillel.javapro.dao;

import ua.ithillel.javapro.orm.OrmEntityColumn;
import ua.ithillel.javapro.orm.OrmEntityMetaInfo;
import ua.ithillel.javapro.orm.OrmUtil;
import ua.ithillel.javapro.reflection.Column;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class BaseDao<T> {

    protected final DataSource dataSource;

    protected final Class<T> persistentClass;

    protected final OrmEntityMetaInfo<T> entityMetaInfo;

    public BaseDao(DataSource dataSource) {
        this.dataSource = dataSource;
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.entityMetaInfo = OrmUtil.getMetaInfo(this.persistentClass).orElseThrow();
    }

    public List<T> findAll() throws SQLException, InvocationTargetException, InstantiationException, IllegalAccessException {

        try (Connection connection = this.dataSource.getConnection()) {
            try (ResultSet rs = connection.prepareStatement("SELECT * FROM \"" + entityMetaInfo.getTableName() + "\"").executeQuery()) {

                List<T> entities = new ArrayList<>();

                while (rs.next()) {
                    entities.add(extractEntityNew(rs));
                }

                return entities;

            }
        }

    }

    public Optional<T> findById(Integer id) throws SQLException, InvocationTargetException, InstantiationException, IllegalAccessException {

        try (Connection connection = this.dataSource.getConnection()) {

            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM \"" + entityMetaInfo.getTableName() + "\" WHERE " + entityMetaInfo.getPrimaryKey().getName() + " = ?"
            )) {

                ps.setInt(1, id);

                try (ResultSet rs = ps.executeQuery()) {

                    if (!rs.next()) {
                        return Optional.empty();
                    }

                    return Optional.of(extractEntityNew(rs));

                }

            }

        }

    }

    public T save(T entity) throws SQLException, IllegalAccessException {

        try (Connection connection = this.dataSource.getConnection()) {

            StringBuilder insertSql = new StringBuilder("INSERT INTO \"" + this.entityMetaInfo.getTableName() + "\" (");

            /*for (OrmEntityColumn column : this.entityMetaInfo.getColumns()) {

                if (column.getName().equals(this.entityMetaInfo.getPrimaryKey().getName())) {
                    continue;
                }

                insertSql.append(column.getName()).append(',');

            }*/

            insertSql.append(
                            this.entityMetaInfo.getColumns()
                                    .stream()
                                    .filter(column -> !column.getName().equals(this.entityMetaInfo.getPrimaryKey().getName()))
                                    .map(column -> "\"" + column.getName() + "\"")
                                    .collect(Collectors.joining(","))
                    )
                    .append(") ");

            insertSql.append("VALUES (")
                    .append(
                            this.entityMetaInfo.getColumns()
                                    .stream()
                                    .filter(column -> !column.getName().equals(this.entityMetaInfo.getPrimaryKey().getName()))
                                    .map(column -> "?")
                                    .collect(Collectors.joining(","))
                    )
                    .append(")");

            try (PreparedStatement ps = connection.prepareStatement(insertSql.toString(), PreparedStatement.RETURN_GENERATED_KEYS)) {

                for (OrmEntityColumn column : this.entityMetaInfo.getColumns()) {

                    if (column.getName().equals(this.entityMetaInfo.getPrimaryKey().getName())) {
                        continue;
                    }

                    // TODO
                    //column.getField().getType()

                }

                ps.setString(1, entity.getLogin());
                ps.setString(2, entity.getFio());
                ps.setString(3, entity.getRole());
                ps.setString(4, entity.getPassword());

                ps.setDate(5, new java.sql.Date(entity.getBirthday().getTime()));
                ps.setDate(6, new java.sql.Date(entity.getCreatedAt().getTime()));

                ps.executeUpdate();

                ResultSet gkrs = ps.getGeneratedKeys();

                gkrs.next();

                entityMetaInfo.getPrimaryKey().getField().setAccessible(true);
                entityMetaInfo.getPrimaryKey().getField().set(entity, gkrs.getInt(1));
                entityMetaInfo.getPrimaryKey().getField().setAccessible(false);

            }

        }

        return entity;

    }

    public void update(T entity) throws SQLException {

    }

    public void deleteById(Integer id) throws SQLException {
        try (Connection connection = this.dataSource.getConnection()) {

            try (PreparedStatement ps = connection.prepareStatement("DELETE FROM \"" + entityMetaInfo.getTableName() + "\" WHERE " + entityMetaInfo.getPrimaryKey().getName() + " = ?")) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }

        }
    }

    public void delete(T entity) throws SQLException, IllegalAccessException {
        deleteById((Integer) this.entityMetaInfo.getPrimaryKey().getField().get(entity));
    }

    protected abstract T extractEntity(ResultSet rs) throws SQLException;

    private T extractEntityNew(ResultSet rs) throws SQLException, InvocationTargetException, InstantiationException, IllegalAccessException {

        Object obj = this.entityMetaInfo.getDefaultConstructor().newInstance();

        for (OrmEntityColumn column : this.entityMetaInfo.getColumns()) {

            String columnName = column.getName();

            Field columnField = column.getField();

            Class fieldType = columnField.getType();

            /*Method setter = null;

            try {
                setter = type.getDeclaredMethod("set" + (field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1)), fieldType);
            } catch (NoSuchMethodException e) {
            }*/

            /*if (setter != null) {

                try {

                    setter.setAccessible(true);

                    if (fieldType.isPrimitive()) {
                        if (fieldType == Float.TYPE) {
                            setter.invoke(obj, rs.getFloat(columnName));
                        } else if (fieldType == Double.TYPE) {
                            setter.invoke(obj, rs.getDouble(columnName));
                        } else if (fieldType == Integer.TYPE) {
                            setter.invoke(obj, rs.getInt(columnName));
                        } else if (fieldType == Long.TYPE) {
                            setter.invoke(obj, rs.getLong(columnName));
                        } else if (fieldType == Short.TYPE) {
                            setter.invoke(obj, rs.getShort(columnName));
                        } else if (fieldType == Byte.TYPE) {
                            setter.invoke(obj, rs.getByte(columnName));
                        } else if (fieldType == Boolean.TYPE) {
                            setter.invoke(obj, rs.getBoolean(columnName));
                        } else if (fieldType == Character.TYPE) {
                            setter.invoke(obj, rs.getString(columnName).charAt(0));
                        }
                    } else {
                        setter.invoke(obj, rs.getObject(columnName));
                    }

                } finally {
                    setter.setAccessible(false);
                }

            } else {*/

            try {

                columnField.setAccessible(true);

                if (fieldType.isPrimitive()) {
                    if (fieldType == Float.TYPE) {
                        columnField.setFloat(obj, rs.getFloat(columnName));
                    } else if (fieldType == Double.TYPE) {
                        columnField.setDouble(obj, rs.getDouble(columnName));
                    } else if (fieldType == Integer.TYPE) {
                        columnField.setInt(obj, rs.getInt(columnName));
                    } else if (fieldType == Long.TYPE) {
                        columnField.setLong(obj, rs.getLong(columnName));
                    } else if (fieldType == Short.TYPE) {
                        columnField.setShort(obj, rs.getShort(columnName));
                    } else if (fieldType == Byte.TYPE) {
                        columnField.setByte(obj, rs.getByte(columnName));
                    } else if (fieldType == Boolean.TYPE) {
                        columnField.setBoolean(obj, rs.getBoolean(columnName));
                    } else if (fieldType == Character.TYPE) {
                        columnField.setChar(obj, rs.getString(columnName).charAt(0));
                    }
                } else {
                    columnField.set(obj, rs.getObject(columnName));
                }

            } finally {
                columnField.setAccessible(false);
            }

            //}

        }

        return (T) obj;

    }

}
