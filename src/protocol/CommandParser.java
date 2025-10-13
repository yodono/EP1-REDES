package protocol;

import java.util.Arrays;
import java.util.List;

/**
 * Classe que processa comando recebido
 */
public class CommandParser {

    private static final String DELIMITER = " "; // space

    public static ParsedCommand parse(String raw) {
        List<String> parts = Arrays.asList(raw.split(DELIMITER));
        String name = parts.getFirst();
        List<String> args = parts.subList(1, parts.size());
        return new ParsedCommand(name, args);
    }
}
