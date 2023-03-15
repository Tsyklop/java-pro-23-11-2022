package ua.ithillel.javapro.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ua.ithillel.javapro.LessonEntity;
import ua.ithillel.javapro.UserEntity;
import ua.ithillel.javapro.orm.OrmUtil;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException, InvocationTargetException, InstantiationException, IllegalAccessException {

        OrmUtil.collect(UserEntity.class, LessonEntity.class);

        //Hibernate hibernate = new Hibernate(config);

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/hillel");
        config.setUsername("postgres");
        config.setPassword("1");
        config.setMinimumIdle(2);
        /*config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");*/

        DataSource ds = new HikariDataSource(config);

        UserDao userDao = new UserDao(ds);

        userDao.findAll().forEach(System.out::println);

    }

}
