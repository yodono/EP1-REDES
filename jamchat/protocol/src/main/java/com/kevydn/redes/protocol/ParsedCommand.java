package com.kevydn.redes.protocol;

import java.util.List;

// Trecho retirado e adaptado da conversa com ChatGPT: pág 128/172 do anexo ChatGPT Data Export - redes.pdf.
public record ParsedCommand(String name, List<String> args) {

    public String arg(int index) {
        return index < args.size() ? args.get(index) : null;
    }
}
