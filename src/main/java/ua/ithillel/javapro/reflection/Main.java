package ua.ithillel.javapro.reflection;

import ua.ithillel.javapro.UserEntity;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Main {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {

        UserEntity user = new UserEntity();

        Class<UserEntity> clazz1 = UserEntity.class;

        Class<? extends UserEntity> clazz2 = new UserEntity().getClass();

        for (Field field : clazz1.getDeclaredFields()) {
            System.out.println(field.toString());
        }

        System.out.println(user.getId());

        Field idField = clazz1.getDeclaredField("id");

        idField.setAccessible(true);
        idField.set(user, 1);
        idField.setAccessible(false);

        System.out.println(user.getId());

        Method setIdMethod = clazz1.getDeclaredMethod("setId", Integer.class);

        setIdMethod.setAccessible(true);
        setIdMethod.invoke(user, 13);
        setIdMethod.setAccessible(false);

        System.out.println(user.getId());

        System.out.println(clazz1.getDeclaredMethod("build").invoke(user));

        Constructor<UserEntity> constructor = clazz1.getDeclaredConstructor();

        System.out.println(constructor.newInstance());

        /*try {

            Field field = UserEntity.class.getField("login");

            if (field.getType() == String.class) {
                field.set(user, rs.getString("login"));
            } else if (field.getType() == Integer.class) {
                field.set(user, rs.getInt("login"));
            }


        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }*/

    }

}
