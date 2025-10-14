package com.kevydn.redes.protocol;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe que processa comando recebido
 */
public class CommandParser {

    private static final String DELIMITER = " "; // space

    public static ParsedCommand parse(String raw, CommandRegistry registry) {
        if (raw == null || raw.isBlank()) {
            return new ParsedCommand("", List.of());
        }

        // Split by spaces initially
        List<String> tokens = new ArrayList<>(List.of(raw.trim().split(DELIMITER)));
        String name = tokens.get(0);

        Command command = registry.get(name);
        List<String> args;

        if (command == null || command.expectedArgs() < 0) {
            // Unknown command or variable arg count → take everything as args
            args = tokens.subList(1, tokens.size());
        } else {
            int expected = command.expectedArgs();
            args = new ArrayList<>();

            // Take the first expected-1 args normally
            for (int i = 1; i < Math.min(tokens.size(), expected); i++) {
                args.add(tokens.get(i));
            }

            // If we expect fewer args than provided, merge the rest as one last argument
            if (tokens.size() > expected) {
                // Join the remaining tokens into one argument (useful for messages)
                String lastArg = String.join(" ", tokens.subList(expected, tokens.size()));
                args.add(lastArg);
            }
        }

        return new ParsedCommand(name, args);
    }
}
