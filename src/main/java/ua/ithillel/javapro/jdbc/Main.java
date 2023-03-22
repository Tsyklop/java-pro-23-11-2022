package ua.ithillel.javapro.jdbc;

import ua.ithillel.javapro.UserEntity;
import ua.ithillel.javapro.reflection.Column;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Main {

    private static final RowMapper<UserEntity> userRowMapper = new UserRowMapper();

    private final static String USER_INSERT_SQL = "INSERT INTO \"user\" (login, fio, role, password, birthday, created_at) VALUES (?, ?, ?, ?, ?, ?);";

    private final static String EXAMPLE_SQL_WITH_TEXT_BLOCK = """
                        
            SELECT *
            FROM "user"
            WHERE ...
                        
            """;

    public static void main(String[] args) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        /*Iterator<String> iterator = List.of("1", "2").iterator();

        while (iterator.hasNext()) {

            String value = iterator.next();

            System.out.println(value);

            //iterator.remove();

        }*/

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/hillel", "postgres", "1")) {

            try (Statement statement = connection.createStatement()) {

                try (ResultSet resultSet = statement.executeQuery("SELECT * FROM \"user\";")) {

                    ResultSetMetaData metaData = resultSet.getMetaData();

                    while (resultSet.next()) {

                        for (int i = 0; i < metaData.getColumnCount(); i++) {
                            resultSet.getString(metaData.getColumnName(i + 1));
                        }

                        String login = resultSet.getString("login");
                        String password = resultSet.getString("password");
                        String fio = resultSet.getString("fio");
                        String role = resultSet.getString("role");
                        String phone = resultSet.getString("phone");

                        System.out.println(login + "; " + password + "; " + fio + "; " + role + "; " + phone);

                    }

                }

                //statement.executeUpdate("INSERT INTO \"user\" (login, fio, role, password) VALUES ('test101', 'gggggg', 'gggggg', 'qwertyuioi')");

            }

            try (PreparedStatement ps = connection.prepareStatement(USER_INSERT_SQL, PreparedStatement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, "skghjsdgf3");
                ps.setString(2, "skghjsdgf3");
                ps.setString(3, "skghjsdgf3");
                ps.setString(4, "skghjsdgf3");

                ps.setDate(5, new java.sql.Date(Calendar.getInstance().getTime().getTime()));
                ps.setDate(6, new java.sql.Date(Calendar.getInstance().getTime().getTime()));

                ps.executeUpdate();

                ResultSet gkrs = ps.getGeneratedKeys();

                gkrs.next();
                System.out.println(gkrs.getInt(1));

            }

            try (Statement statement = connection.createStatement()) {

                try (ResultSet resultSet = statement.executeQuery("SELECT * FROM \"user\";")) {

                    List<UserEntity> users = buildUserEntities(resultSet);

                    users.forEach(System.out::println);

                }

            }

            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();

            try (Statement statement = connection.createStatement()) {

                try (ResultSet resultSet = statement.executeQuery("SELECT * FROM \"user\";")) {

                    List<UserEntity> users = mapToObjects(UserEntity.class, resultSet);

                    users.forEach(System.out::println);

                }

            }

            try (Statement statement = connection.createStatement()) {

                try (ResultSet resultSet = statement.executeQuery("SELECT l.*, h.id AS h_id, h.name AS h_name, h.description AS h_description from lesson AS l JOIN homework AS h on h.id = l.homework_id;")) {

                    while (resultSet.next()) {

                        LessonEntity lesson = new LessonEntity();

                        lesson.setId(resultSet.getInt("id"));
                        lesson.setName(resultSet.getString("name"));
                        //lesson.setUpdatedOn(resultSet.getDate("updated_on"));

                        HomeworkEntity homework = new HomeworkEntity();

                        homework.setId(resultSet.getInt("h_id"));
                        homework.setName(resultSet.getString("h_name"));
                        homework.setDescription(resultSet.getString("h_description"));

                        lesson.setHomework(homework);

                    }

                }

            }

        }

    }


    private static List<UserEntity> buildUserEntities(ResultSet rs) throws SQLException {

        List<UserEntity> users = new ArrayList<>();

        ResultSetMetaData metaData = rs.getMetaData();

        while (rs.next()) {
            users.add(userRowMapper.map(rs));
        }

        return users;

    }

    private static <T> List<T> mapToObjects(Class<T> type, ResultSet rs) throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        Field[] fields = type.getDeclaredFields();

        List<T> result = new ArrayList<>();

        while (rs.next()) {

            Object obj = type.getDeclaredConstructor().newInstance();

            for (Field field : fields) {

                Class fieldType = field.getType();

                Method setter = null;

                try {
                    setter = type.getDeclaredMethod("set" + (field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1)), fieldType);
                } catch (NoSuchMethodException e) {
                }

                Column column = field.getAnnotation(Column.class);

                String columnName = column != null ? column.name() : field.getName();

                if (setter != null) {

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

                } else {

                    try {

                        field.setAccessible(true);

                        if (fieldType.isPrimitive()) {
                            if (fieldType == Float.TYPE) {
                                field.setFloat(obj, rs.getFloat(columnName));
                            } else if (fieldType == Double.TYPE) {
                                field.setDouble(obj, rs.getDouble(columnName));
                            } else if (fieldType == Integer.TYPE) {
                                field.setInt(obj, rs.getInt(columnName));
                            } else if (fieldType == Long.TYPE) {
                                field.setLong(obj, rs.getLong(columnName));
                            } else if (fieldType == Short.TYPE) {
                                field.setShort(obj, rs.getShort(columnName));
                            } else if (fieldType == Byte.TYPE) {
                                field.setByte(obj, rs.getByte(columnName));
                            } else if (fieldType == Boolean.TYPE) {
                                field.setBoolean(obj, rs.getBoolean(columnName));
                            } else if (fieldType == Character.TYPE) {
                                field.setChar(obj, rs.getString(columnName).charAt(0));
                            }
                        } else {
                            field.set(obj, rs.getObject(columnName));
                        }

                    } finally {
                        field.setAccessible(false);
                    }

                }

            }

            result.add((T) obj);

        }

        return result;

    }

    public static interface RowMapper<T> {
        T map(ResultSet rs) throws SQLException;
    }

    public static class UserRowMapper implements RowMapper<UserEntity> {

        @Override
        public UserEntity map(ResultSet rs) throws SQLException {

            UserEntity user = new UserEntity();

            user.setId(rs.getInt("id"));
            user.setLogin(rs.getString("login"));
            user.setPassword(rs.getString("password"));
            user.setFio(rs.getString("fio"));
            user.setRole(rs.getString("role"));
            user.setPhone(rs.getString("phone"));
            user.setBalance(rs.getFloat("balance"));
            user.setBirthday(rs.getDate("birthday"));
            user.setCreatedAt(rs.getDate("created_at"));

            //user.setAddress(new Address(rs.getString("address_name")));

            return user;
        }
    }

    public static class LessonEntity {

        private Integer id;
        private String name;
        private HomeworkEntity homework;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public HomeworkEntity getHomework() {
            return homework;
        }

        public void setHomework(HomeworkEntity homework) {
            this.homework = homework;
        }
    }

    public static class HomeworkEntity {

        private Integer id;
        private String name;
        private String description;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

}
