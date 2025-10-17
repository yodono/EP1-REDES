package com.kevydn.redes.protocol;

/**
 * Classe orquestradora que interpreta e executa comandos e define condições de execução
 */
public abstract class CommandHandler {

    protected final CommandRegistry registry;
    protected final NetworkContext networkContext;
    protected final MessageObserver messageObserver;

    public CommandHandler(NetworkContext networkContext) {
        this.networkContext = networkContext;
        this.registry = new CommandRegistry();
        this.messageObserver = null;
        registerCommands();
    }

    public CommandHandler(NetworkContext networkContext, MessageObserver messageObserver) {
        this.networkContext = networkContext;
        this.registry = new CommandRegistry();
        this.messageObserver = messageObserver;
        registerCommands();
    }

    public void handle(String input) {
        ParsedCommand cmd = CommandParser.parse(input, registry);
        Command command = registry.get(cmd.name());

        if (command == null) {
            handleUnknown();
            return;
        }
        if (!precondition(cmd)) return;

        command.execute(cmd, networkContext, messageObserver);
    }

    // TODO mover precondition para cada comando?? por exemplo play command no client
    protected abstract Boolean precondition(ParsedCommand cmd);
    protected abstract void registerCommands();
    protected abstract void handleUnknown();
}
