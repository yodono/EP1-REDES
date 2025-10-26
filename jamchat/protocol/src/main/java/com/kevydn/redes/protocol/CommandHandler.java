package com.kevydn.redes.protocol;

/**
 * Classe orquestradora que interpreta e executa comandos e define condições de execução
 */
public abstract class CommandHandler {

    // Guarda comandos aceitos por esse handler
    protected final CommandRegistry registry;
    // Guarda network context para ser utilizado nos comandos
    protected final NetworkContext networkContext;
    // Guarda interface MessageObserver para reagir a mensagens trocadas
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

    // Comando centralizado que interpreta a sintaxe do comando, pega no registro, analisa pre-condições e executa o comando
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
