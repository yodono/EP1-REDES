package com.kevydn.redes.client.commands;

import com.kevydn.redes.client.Client;
import com.kevydn.redes.protocol.Command;
import com.kevydn.redes.protocol.CommandContext;
import com.kevydn.redes.protocol.ParsedCommand;

public class MsgCommand implements Command {

    @Override
    public String name() { return "/msg"; }

    @Override
    public String description() { return "Recebe uma mensagem do servidor"; }

    @Override
    public void execute(ParsedCommand cmd, CommandContext ctx) {
        String msg = cmd.arg(0);
        assert msg != null;
        msg = msg.replace("\\n", "\n");
        Client client = (Client) ctx;
        client.out(msg);
    }

    @Override
    public int expectedArgs() { return 1; }
}
