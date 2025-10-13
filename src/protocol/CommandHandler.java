package protocol;

/**
 * Classe orquestradora que interpreta e executa comandos e define condições de execução
 */
public abstract class CommandHandler {

    protected final CommandRegistry registry;
    protected final CommandContext ctx;

    public CommandHandler(CommandContext ctx) {
        this.ctx = ctx;
        this.registry = new CommandRegistry();
        registerCommands();
    }

    public void handle(String input) {
        ParsedCommand cmd = CommandParser.parse(input);
        Command command = registry.get(cmd.name());

        if (!precondition(cmd)) return;

        if (command == null) {
            ctx.send(">>> Comando desconhecido. Use /help para ver a lista.");
            return;
        }

        command.execute(cmd, ctx);
    }

    protected abstract Boolean precondition(ParsedCommand cmd);
    protected abstract void registerCommands();
}
