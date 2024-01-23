package dev.naamad.spreadsheet.exception;

public class ColumnNotFoundException extends RuntimeException{

    public ColumnNotFoundException(String message) {
        super(message);
    }
}
