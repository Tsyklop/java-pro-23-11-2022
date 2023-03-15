package ua.ithillel.javapro.dao;

import ua.ithillel.javapro.UserEntity;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

public class UserDao extends BaseDao<UserEntity> {

    private final static String INSERT_SQL = "INSERT INTO \"user\" (login, fio, role, password, birthday, created_at) VALUES (?, ?, ?, ?, ?, ?);";

    public UserDao(DataSource dataSource) {
        super(dataSource);
    }

    public Optional<UserEntity> findByLogin(String login) throws SQLException {
        try (Connection connection = this.dataSource.getConnection()) {

            try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM \"user\" WHERE login = ?")) {

                ps.setString(1, login);

                try (ResultSet rs = ps.executeQuery()) {

                    if (!rs.next()) {
                        return Optional.empty();
                    }

                    return Optional.of(extractEntity(rs));

                }

            }

        }
    }

    @Override
    protected UserEntity extractEntity(ResultSet rs) throws SQLException {

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

        return user;

    }

}
