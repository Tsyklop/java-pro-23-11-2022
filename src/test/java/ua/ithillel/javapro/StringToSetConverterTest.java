package ua.ithillel.javapro;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.ithillel.javapro.testing.StringToSetConverter;

import java.util.Set;

class StringToSetConverterTest {

    @BeforeAll
    static void beforeAll() {

    }

    @BeforeEach
    void beforeEach() {

    }

    @Test
    void shouldReturnSetOfStrings_forCorrectString() {

        Set<String> result = StringToSetConverter.convertToSet("[abc][fgh][fdf][try][asf]");

        Assertions.assertEquals(5, result.size());

        //Assertions.assertEquals("abc,fgh,fdf,try,asf", String.join(",", result)); // not good

        Assertions.assertTrue(result.contains("abc"));
        Assertions.assertTrue(result.contains("fgh"));
        Assertions.assertTrue(result.contains("fdf"));
        Assertions.assertTrue(result.contains("try"));
        Assertions.assertTrue(result.contains("asf"));

    }

    @Test
    void shouldReturnNull_forNullAndEmptyString() {

        Assertions.assertNull(
                StringToSetConverter.convertToSet(null)
        );

        Assertions.assertNull(
                StringToSetConverter.convertToSet("")
        );

    }

    @Test
    void shouldReturnSetWithOneEmptyString_forStringWithTwoSymbols() {

        Set<String> result = StringToSetConverter.convertToSet("[]");

        Assertions.assertTrue(result.isEmpty());

    }

    @Test
    void shouldThrowException_forStringWithOneSymbol() {

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> StringToSetConverter.convertToSet("[")
        );

    }

    @Test
    void shouldReturnString_forSetOfCorrectStrings() {

        Assertions.assertEquals("[abc]", StringToSetConverter.convertToSinlgeString(Set.of("abc")));
        Assertions.assertEquals("[abc][fgh]", StringToSetConverter.convertToSinlgeString(Set.of("abc", "fgh")));
        Assertions.assertEquals("[abc][fgh]", StringToSetConverter.convertToSinlgeString(Set.of("abc", "fgh", "")));

    }

    @Test
    void shouldReturnNull_forIncorrectSetOfStrings() {

        Assertions.assertNull(StringToSetConverter.convertToSinlgeString(null));
        Assertions.assertNull(StringToSetConverter.convertToSinlgeString(Set.of()));

    }

    @Test
    void shouldReturnNull_forSetOfEmptyStrings() {
        Assertions.assertNull(StringToSetConverter.convertToSinlgeString(Set.of("")));
    }

}