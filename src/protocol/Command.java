package protocol;

public interface Command {
    String name();
    String description();
    void execute(ParsedCommand cmd, CommandContext ctx);
}
