package com.asparagus.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Arrays;

public final class StreamReader {
    private static final int BUFFER_SIZE = 8192;
    private static final ThreadLocal<byte[]> cachedInput = new ThreadLocal<>();
    
    private StreamReader() {} // Prevent instantiation
    
    public static synchronized void resetCache() {
        cachedInput.remove();
    }
    
    public static List<String> readLines(InputStream input) throws IOException {
        String content = readContent(input);
        return Arrays.asList(content.split("\n", -1));
    }

    public static String readContent(InputStream input) throws IOException {
        return new String(readBytes(input), StandardCharsets.UTF_8);
    }

    public static byte[] readBytes(InputStream input) throws IOException {
        byte[] cached = cachedInput.get();
        if (cached != null) {
            return cached;
        }

        try (BufferedInputStream bis = new BufferedInputStream(input);
             ByteArrayOutputStream baos = new ByteArrayOutputStream(BUFFER_SIZE)) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            byte[] result = baos.toByteArray();
            cachedInput.set(result);
            return result;
        }
    }
}