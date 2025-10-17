package com.kevydn.redes.protocol;

public interface Command {
    String name();
    String description();
    int expectedArgs();
    void execute(ParsedCommand cmd, NetworkContext ctx, MessageObserver messageObserver);
}
