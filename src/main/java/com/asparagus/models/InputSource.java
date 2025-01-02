package com.asparagus.models;

public class InputSource {
    private final String filename;
    private final boolean isStdin;

    private InputSource(String filename, boolean isStdin) {
        this.filename = filename;
        this.isStdin = isStdin;
    }

    public static InputSource fromStdin() {
        return new InputSource(null, true);
    }

    public static InputSource fromFile(String filename) {
        return new InputSource(filename, false);
    }

    public String getFilename() {
        return filename;
    }

    public boolean isStdin() {
        return isStdin;
    }
} 