package com.asparagus.service;

import org.springframework.stereotype.Service;

import com.asparagus.enums.Command;
import com.asparagus.handler.CommandHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CliService {

    private final CommandHandler commandHandler;

    public void executeCommand(String command) {
        if (command == null || command.isEmpty()) {
            printResponse(commandHandler.handleDefault(""));
            return;
        }

        Command matchedCommand = null;
        for (Command cmd : Command.values()) {
            if (cmd.matches(command)) {
                matchedCommand = cmd;
                break;
            }
        }

        String response = switch (matchedCommand) {
            case HELP -> commandHandler.handleHelp();
            case VERSION -> commandHandler.handleVersion();
            case FILE_SIZE -> commandHandler.handleFileSize(command);
            case LINE_COUNT -> commandHandler.handleLineCount(command);
            case WORD_COUNT -> commandHandler.handleWordCount(command);
            case CHAR_COUNT -> commandHandler.handleCharCount(command);
            case DEFAULT -> commandHandler.handleDefault(command);
            case null -> commandHandler.handleUnknown(command);
        };

        printResponse(response);
    }

    private void printResponse(String response) {
        System.out.println(response);
    }
} 