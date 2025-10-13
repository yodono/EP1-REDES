package protocol;

import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

/**
 * Classe que guarda registro de comandos suportados e mapeamento para o handler correto
 */
public class CommandRegistry {

    private final Map<String, Command> commands = new HashMap<>();

    public void register(Command cmd) {
        commands.put(cmd.name(), cmd);
    }

    public Command get(String name) {
        return commands.get(name);
    }

    public Collection<Command> all() {
        return commands.values();
    }
}
