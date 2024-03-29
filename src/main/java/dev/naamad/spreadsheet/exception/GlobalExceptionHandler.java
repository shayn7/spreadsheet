package dev.naamad.spreadsheet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SchemaNotFoundException.class)
    public ResponseEntity<String> handleSchemaRequestException(SchemaNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(SheetNotFoundException.class)
    public ResponseEntity<String> handleSpreadsheetRequestException(SheetNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(ColumnNotFoundException.class)
    public ResponseEntity<String> handleColumnNotFoundException(ColumnNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidCellValueException.class)
    public ResponseEntity<String> handleInvalidCellValueException(InvalidCellValueException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
