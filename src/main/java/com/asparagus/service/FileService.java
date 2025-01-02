package com.asparagus.service;

import org.springframework.stereotype.Service;

import com.asparagus.models.FileStats;
import com.asparagus.models.InputSource;
import com.asparagus.utils.StreamReader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Locale;

@Service
public class FileService {
    private static final String FORMAT_WITH_FILENAME = "%d %s";
    private static final String FORMAT_WITHOUT_FILENAME = "%d";
    private static final String FORMAT_COMBINED = "%d %d %d";
    private static final String FORMAT_COMBINED_WITH_FILENAME = "%d %d %d %s";

    public String getFileSize(InputSource source) {
        if (source.isStdin()) {
            try {
                byte[] bytes = StreamReader.readBytes(System.in);
                return String.format("%d", bytes.length);
            } catch (IOException e) {
                throw new RuntimeException("Error reading from standard input", e);
            }
        }

        try {
            Path path = Paths.get(source.getFilename());
            long bytes = Files.size(path);
            return String.format("%d %s", bytes, source.getFilename());
        } catch (IOException e) {
            return String.format("Error: Cannot access file '%s'", source.getFilename());
        }
    }

    public String getLineCount(InputSource source) {
        if (source.isStdin()) {
            try {
                String content = StreamReader.readContent(System.in);
                long lines = content.isEmpty() ? 0 : 
                    content.chars().filter(ch -> ch == '\n').count() + 
                    (content.charAt(content.length() - 1) == '\n' ? 0 : 1);
                return String.format("%d", lines);
            } catch (IOException e) {
                throw new RuntimeException("Error reading from standard input", e);
            }
        }

        try {
            Path path = Paths.get(source.getFilename());
            long lines = Files.lines(path, StandardCharsets.UTF_8).count();
            return String.format("%d %s", lines, source.getFilename());
        } catch (IOException e) {
            return String.format("Error: Cannot access file '%s'", source.getFilename());
        }
    }

    public String getWordCount(InputSource source) {
        if (source.isStdin()) {
            try {
                String content = StreamReader.readContent(System.in);
                return String.format("%d", countWords(content));
            } catch (IOException e) {
                throw new RuntimeException("Error reading from standard input", e);
            }
        }

        try {
            Path path = Paths.get(source.getFilename());
            String content = Files.readString(path, StandardCharsets.UTF_8);
            long words = countWords(content);
            return String.format("%d %s", words, source.getFilename());
        } catch (IOException e) {
            return String.format("Error: Cannot access file '%s'", source.getFilename());
        }
    }

    public String getCharacterCount(InputSource source) {
        if (source.isStdin()) {
            try {
                String content = StreamReader.readContent(System.in);
                long count = isMultibyteLocale() ? 
                    content.codePoints().count() : 
                    content.getBytes(Charset.defaultCharset()).length;
                return String.format("%d", count);
            } catch (IOException e) {
                throw new RuntimeException("Error reading from standard input", e);
            }
        }

        try {
            Path path = Paths.get(source.getFilename());
            String content = Files.readString(path, StandardCharsets.UTF_8);
            long count = isMultibyteLocale() ? 
                content.codePoints().count() : 
                Files.size(path);
            return String.format("%d %s", count, source.getFilename());
        } catch (IOException e) {
            return String.format("Error: Cannot access file '%s'", source.getFilename());
        }
    }

    public FileStats getCombinedCount(InputSource source) {
        if (source.isStdin()) {
            try {
                byte[] bytes = StreamReader.readBytes(System.in);
                String content = new String(bytes, StandardCharsets.UTF_8);
                long lines = content.isEmpty() ? 0 : 
                    content.chars().filter(ch -> ch == '\n').count() + 
                    (content.charAt(content.length() - 1) == '\n' ? 0 : 1);
                
                return new FileStats(
                    lines,
                    countWords(content),
                    bytes.length
                );
            } catch (IOException e) {
                throw new RuntimeException("Error reading from standard input", e);
            }
        }

        try {
            Path path = Paths.get(source.getFilename());
            byte[] bytes = Files.readAllBytes(path);
            String content = new String(bytes, StandardCharsets.UTF_8);
            long lines = Files.lines(path, StandardCharsets.UTF_8).count();
            
            return new FileStats(
                lines,
                countWords(content),
                bytes.length
            );
        } catch (IOException e) {
            throw new RuntimeException("Cannot access file '" + source.getFilename() + "'", e);
        }
    }

    /**
     * Counts words using whitespace separation, similar to wc command.
     * Supports words in any script (Chinese, Latin, Arabic, etc.)
     */
    private long countWords(String text) {
        if (text.trim().isEmpty()) {
            return 0;
        }

        // Split by whitespace (similar to wc behavior)
        return Arrays.stream(text.split("\\s+"))
            .filter(word -> !word.isEmpty())
            .count();
    }

    /**
     * Checks if the current locale supports multibyte characters.
     * Returns true for locales that commonly use multibyte characters
     * like Chinese, Japanese, Korean, etc.
     */
    private boolean isMultibyteLocale() {
        String language = Locale.getDefault().getLanguage();
        return language.equals("zh") ||    // Chinese
               language.equals("ja") ||    // Japanese
               language.equals("ko") ||    // Korean
               language.equals("th") ||    // Thai
               language.equals("ta") ||    // Tamil
               !Charset.defaultCharset().equals(StandardCharsets.US_ASCII);
    }

    private String formatOutput(long value, InputSource source) {
        return source.isStdin() ? 
            String.format(FORMAT_WITHOUT_FILENAME, value) :
            String.format(FORMAT_WITH_FILENAME, value, source.getFilename());
    }

    private String formatCombinedOutput(FileStats stats, InputSource source) {
        return source.isStdin() ?
            String.format(FORMAT_COMBINED, stats.lines(), stats.words(), stats.bytes()) :
            String.format(FORMAT_COMBINED_WITH_FILENAME, stats.lines(), stats.words(), 
                stats.bytes(), source.getFilename());
    }
} 