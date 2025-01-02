package com.asparagus.handler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.asparagus.models.FileStats;
import com.asparagus.models.InputSource;
import com.asparagus.service.FileService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class CommandHandler {
    
    private final FileService fileService;
    
    @Value("${cli.commands.help}")
    private String helpMessage;
    
    @Value("${cli.commands.version}")
    private String version;

    private boolean hasStdin() {
        try {
            return System.in.available() > 0;
        } catch (IOException e) {
            return false;
        }
    }

    public String handleFileSize(String command) {
        if (command.equals("-c")) {
            if (!hasStdin()) {
                return "Error: No input provided";
            }
            return fileService.getFileSize(InputSource.fromStdin());
        }
        String filename = command.substring(3).trim();
        return fileService.getFileSize(InputSource.fromFile(filename));
    }

    public String handleLineCount(String command) {
        if (command.equals("-l")) {
            if (!hasStdin()) {
                return "Error: No input provided";
            }
            return fileService.getLineCount(InputSource.fromStdin());
        }
        String filename = command.substring(3).trim();
        return fileService.getLineCount(InputSource.fromFile(filename));
    }

    public String handleWordCount(String command) {
        if (command.equals("-w")) {
            if (!hasStdin()) {
                return "Error: No input provided";
            }
            return fileService.getWordCount(InputSource.fromStdin());
        }
        String filename = command.substring(3).trim();
        return fileService.getWordCount(InputSource.fromFile(filename));
    }

    public String handleCharCount(String command) {
        if (command.equals("-m")) {
            if (!hasStdin()) {
                return "Error: No input provided";
            }
            return fileService.getCharacterCount(InputSource.fromStdin());
        }
        String filename = command.substring(3).trim();
        return fileService.getCharacterCount(InputSource.fromFile(filename));
    }

    public String handleDefault(String filename) {
        if (filename.isEmpty()) {
            if (!hasStdin()) {
                return "Error: No input provided";
            }
            FileStats stats = fileService.getCombinedCount(InputSource.fromStdin());
            return String.format("%d %d %d", stats.lines(), stats.words(), stats.bytes());
        }
        
        FileStats stats = fileService.getCombinedCount(InputSource.fromFile(filename));
        return String.format("%d %d %d %s", stats.lines(), stats.words(), stats.bytes(), filename);
    }

    public String handleHelp() {
        return helpMessage;
    }

    public String handleVersion() {
        return String.format("Version %s (Running on Java %s)", 
            version, System.getProperty("java.version"));
    }

    public String handleUnknown(String command) {
        return "Unknown command: " + command;
    }
} 