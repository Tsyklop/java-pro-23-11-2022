package ua.ithillel.javapro.dao;

import ua.ithillel.javapro.LessonEntity;
import ua.ithillel.javapro.UserEntity;
import ua.ithillel.javapro.jdbc.Main;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LessonDao extends BaseDao<LessonEntity> {

    private final static String INSERT_SQL = "INSERT INTO \"lesson\" (login, fio, role, password, birthday, created_at) VALUES (?, ?, ?, ?, ?, ?);";

    public LessonDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected LessonEntity extractEntity(ResultSet rs) throws SQLException {

        LessonEntity lesson = new LessonEntity();

        lesson.setId(rs.getInt("id"));
        lesson.setName(rs.getString("name"));

        return lesson;

    }

}
