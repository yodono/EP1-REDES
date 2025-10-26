package com.kevydn.redes.protocol;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe que processa comando recebido
 * Trecho retirado e adaptado da conversa com ChatGPT: pág 95/172 do anexo ChatGPT Data Export - redes.pdf.
 */
public class CommandParser {

    private static final String DELIMITER = " "; // space

    public static ParsedCommand parse(String raw, CommandRegistry registry) {
        if (raw == null || raw.isBlank()) {
            return new ParsedCommand("", List.of());
        }

        // Separa a lista original por espaços (sintaxe do protocolo)
        List<String> tokens = new ArrayList<>(List.of(raw.trim().split(DELIMITER)));

        // O nome do comando é o primeiro item (p. ex. /msg)
        String name = tokens.get(0);

        // Pega o Command a partir do registros de comando passado como argumento
        Command command = registry.get(name);
        List<String> args;

        // O Command implementado deve possuir um método expectedArgs, informando o número de
        // argumentos que vamos tentar extrair aqui
        if (command == null || command.expectedArgs() < 0) {
            // Caso desconhecido ou sem expectedArgs, cada token splitado pelo delimitador será
            // interpretado como arg
            args = tokens.subList(1, tokens.size());
        } else {
            int expected = command.expectedArgs();
            args = new ArrayList<>();

            // Caso válido, pegamos os n-1 argumentos esperados, começando depois do nome do comando
            for (int i = 1; i < Math.min(tokens.size(), expected); i++) {
                args.add(tokens.get(i));
            }

            // Caso tenhamos separado em mais partes do que os argumentos esperados
            // os últimos tokens serão novamente unificados em apenas 1 argumento
            // p. ex. /msg mensagem com espacos, teriamos 4 tokens splitados, mas 2 args espeardos
            // o interpretador resultaria na tupla ("/msg", "/mensagem com espacos")
            if (tokens.size() > expected) {
                String lastArg = String.join(" ", tokens.subList(expected, tokens.size()));
                args.add(lastArg);
            }
        }

        return new ParsedCommand(name, args);
    }
}
