# Word Counter CLI (wc-by-ktevfik)

A Java implementation of the Unix `wc` command using Spring Boot. This command-line utility counts lines, words, bytes, and characters in files or from standard input with full Unicode support.

## Features

- Line counting (`-l`)
- Word counting (`-w`)
- Byte counting (`-c`)
- Character counting (`-m`)
- Combined counting (default mode)
- File and stdin input support
- Full Unicode/UTF-8 support
- Thread-safe implementation
- Locale-aware character counting

## Prerequisites

- Java 21
- Maven 3.6+
- Windows (for .exe build)

## Building

```bash
mvn clean package
```

This creates:
- `target/wc-by-ktevfik-1.0.jar`
- `target/wc-by-ktevfik.exe`

## Usage Examples

```bash
# Count lines
wc-by-ktevfik.exe -l file.txt
cat file.txt | wc-by-ktevfik.exe -l

# Count words
wc-by-ktevfik.exe -w file.txt
echo "hello world" | wc-by-ktevfik.exe -w

# Count bytes
wc-by-ktevfik.exe -c file.txt

# Count characters
wc-by-ktevfik.exe -m file.txt

# All counts (lines, words, bytes)
wc-by-ktevfik.exe file.txt
```

## Project Structure

```bash
src/main/java/com/asparagus/
├── Application.java
├── enums/
│   └── Command.java
├── handler/
│   └── CommandHandler.java
├── models/
│   ├── FileStats.java
│   └── InputSource.java
├── service/
│   ├── CliService.java
│   └── FileService.java
└── utils/
    ├── Constants.java
    └── StreamReader.java
```

## Testing

Run the automated test suite:
```bash
.\test-script.ps1
```

The script performs comprehensive testing of all functionality including:
- File operations
- Standard input handling
- Error cases
- Unicode support
- Edge cases

## Error Handling

```bash
# No input
wc-by-ktevfik.exe -l
> Error: No input provided

# Invalid file
wc-by-ktevfik.exe -l nonexistent.txt
> Error: Cannot access file 'nonexistent.txt'

# Invalid option
wc-by-ktevfik.exe -x
> Unknown command: -x
```

## Technical Details

- Built with Spring Boot 3.2.3
- Uses ThreadLocal for thread-safe input caching
- UTF-8 encoding by default
- Supports CRLF and LF line endings
- Handles multibyte characters
- Proper resource management with try-with-resources

## Development

The project uses:
- Lombok for reducing boilerplate
- Spring Boot for dependency injection
- Launch4j for executable generation
- Maven for build automation

## License

MIT License
