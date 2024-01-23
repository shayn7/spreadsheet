package dev.naamad.spreadsheet.exception;

public class SheetNotFoundException extends RuntimeException{

    public SheetNotFoundException(String message) {
        super(message);
    }

}
