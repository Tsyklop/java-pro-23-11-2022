package ua.ithillel.javapro.testing;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StringToSetConverter {

    /**
     * [abc][fgh][fdf][try][asf]
     * <p>
     * abc, fgh, fdf, try, asf
     *
     * @param source
     * @return
     */
    public static Set<String> convertToSet(String source) {

        if (source == null || source.isEmpty()) {
            return null;
        }

        if (!source.startsWith("[") || !source.endsWith("]")) {
            throw new IllegalArgumentException("Source incorrect");
        }

        String[] sourceParts = source.substring(1, source.length() - 1).split("]\\["); // abc, fgh, fdf, try, asf

        /*sourceParts[0] = sourceParts[0].replace("[", "");
        sourceParts[sourceParts.length - 1] = sourceParts[sourceParts.length - 1].replace("]", "");*/

        return Stream.of(sourceParts).filter(str -> !str.isEmpty()).collect(Collectors.toSet());

    }

    /**
     * abc, fgh, fdf, try, asf
     * <p>
     * [abc][fgh][fdf][try][asf]
     *
     * @param source
     * @return
     */
    public static String convertToSinlgeString(Set<String> source) {
        if (source == null || source.isEmpty()) {
            return null;
        }

        source = source.stream().filter(s -> !s.isEmpty()).collect(Collectors.toSet());

        if (source.isEmpty()) {
            return null;
        }

        return "[" + String.join("][", source) + "]";

    }

}
