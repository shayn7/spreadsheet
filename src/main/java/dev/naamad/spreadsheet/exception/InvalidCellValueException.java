package dev.naamad.spreadsheet.exception;

public class InvalidCellValueException extends RuntimeException{

    public InvalidCellValueException(String message) {
        super(message);
    }
}
