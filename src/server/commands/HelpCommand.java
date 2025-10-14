package server.commands;


import protocol.Command;
import protocol.CommandContext;
import protocol.CommandRegistry;
import protocol.ParsedCommand;

public class HelpCommand implements Command {

    private final CommandRegistry registry;

    public HelpCommand(CommandRegistry registry) {
        this.registry = registry;
    }

    @Override
    public String name() { return "/help"; }

    @Override
    public String description() { return "Mostra esta lista de comandos."; }

    @Override
    public void execute(ParsedCommand cmd, CommandContext ctx) {
        StringBuilder sb = new StringBuilder(">>> Comandos disponíveis:\\n");
        for (Command c : registry.all()) {
            sb.append(" - ").append(c.name()).append(" - ").append(c.description()).append("\\n");
        }
        ctx.send("/msg " + sb);
    }

    @Override
    public int expectedArgs() { return 0; }
}
