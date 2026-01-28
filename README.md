# File Converter

A command-line utility for converting files between JSON, XML, and CSV formats.

## Features

- Convert between JSON, XML, and CSV formats (all 6 directions supported)
- Flat data structure support
- Clean error messages
- Simple CLI interface

## Requirements

- Java 21 or higher
- Maven 3.6+ (optional - Maven Wrapper included)

## Building

```bash
./mvnw clean package
```

On Windows:
```bash
mvnw.cmd clean package
```

This creates an executable JAR at `target/file-converter.jar`.

## Usage

```bash
java -jar target/file-converter.jar --input <input-file> --output <output-file>
```

### Options

| Option     | Description                    | Required |
|------------|--------------------------------|----------|
| `--input`  | Path to the input file         | Yes      |
| `--output` | Path to the output file        | Yes      |
| `--help`   | Show help message              | No       |

### Example Files

The `examples/` folder contains sample files for testing:

- `examples/username.csv` - CSV format
- `examples/username.json` - JSON format
- `examples/username.xml` - XML format

### Examples

**CSV to JSON:**
```bash
java -jar target/file-converter.jar --input examples/username.csv --output output.json
```

**CSV to XML:**
```bash
java -jar target/file-converter.jar --input examples/username.csv --output output.xml
```

**JSON to CSV:**
```bash
java -jar target/file-converter.jar --input examples/username.json --output output.csv
```

**JSON to XML:**
```bash
java -jar target/file-converter.jar --input examples/username.json --output output.xml
```

**XML to JSON:**
```bash
java -jar target/file-converter.jar --input examples/username.xml --output output.json
```

**XML to CSV:**
```bash
java -jar target/file-converter.jar --input examples/username.xml --output output.csv
```

## Supported Data Formats

### JSON

Input/output format - array of flat objects:

```json
[
  {"id": "1", "name": "John", "email": "john@example.com"},
  {"id": "2", "name": "Jane", "email": "jane@example.com"}
]
```

Single objects are also supported:

```json
{"id": "1", "name": "John", "email": "john@example.com"}
```

### XML

```xml
<?xml version="1.0" encoding="UTF-8"?>
<records>
  <record>
    <id>1</id>
    <name>John</name>
    <email>john@example.com</email>
  </record>
  <record>
    <id>2</id>
    <name>Jane</name>
    <email>jane@example.com</email>
  </record>
</records>
```

### CSV

```csv
id,name,email
1,John,john@example.com
2,Jane,jane@example.com
```

## Limitations

- **Flat structures only**: Nested objects and arrays are not supported. The converter will report an error if nested structures are detected.
- **String values**: All values are treated as strings during conversion.

## Error Messages

| Situation               | Message                                                              |
|-------------------------|----------------------------------------------------------------------|
| File not found          | `Error: Input file 'path' not found`                                 |
| Unsupported format      | `Error: Unsupported file format '.xyz'`                              |
| Invalid JSON            | `Error: Invalid JSON syntax at line X`                               |
| Invalid XML             | `Error: Invalid XML syntax: message`                                 |
| Nested structure        | `Error: Nested structures are not supported. Please use flat JSON`   |
| Missing --input         | `Error: Missing required argument --input`                           |
| Missing --output        | `Error: Missing required argument --output`                          |

## Running Tests

```bash
./mvnw test
