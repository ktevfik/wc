package com.asparagus.enums;

public enum Command {
    HELP("help"),
    VERSION("version"),
    FILE_SIZE("-c"),
    LINE_COUNT("-l"),
    WORD_COUNT("-w"),
    CHAR_COUNT("-m"),
    DEFAULT("");

    private final String commandText;

    Command(String commandText) {
        this.commandText = commandText;
    }

    public boolean matches(String input) {
        if (input == null) {
            return false;
        }
        if (this == DEFAULT) {
            return !input.startsWith("-") && 
                   !input.equals(HELP.commandText) && 
                   !input.equals(VERSION.commandText);
        }
        return input.startsWith(commandText);
    }

    public String getCommandText() {
        return commandText;
    }
} 