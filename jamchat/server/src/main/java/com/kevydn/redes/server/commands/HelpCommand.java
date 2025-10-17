package com.kevydn.redes.server.commands;

import com.kevydn.redes.protocol.*;

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
    public void execute(ParsedCommand cmd, NetworkContext ctx, MessageObserver messageObserver) {
        StringBuilder sb = new StringBuilder(">>> Comandos disponíveis:\\n");
        for (Command c : registry.all()) {
            sb.append(" - ").append(c.name()).append(" - ").append(c.description()).append("\\n");
        }
        ctx.send("/msg " + sb);
    }

    @Override
    public int expectedArgs() { return 0; }
}
