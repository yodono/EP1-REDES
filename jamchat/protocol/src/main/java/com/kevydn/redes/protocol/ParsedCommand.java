package com.kevydn.redes.protocol;

import java.util.List;

public record ParsedCommand(String name, List<String> args) {

    public String arg(int index) {
        return index < args.size() ? args.get(index) : null;
    }
}
