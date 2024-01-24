package dev.naamad.spreadsheet.service;

import dev.naamad.spreadsheet.dto.CellValueRequest;
import dev.naamad.spreadsheet.dto.SpreadsheetResponse;
import dev.naamad.spreadsheet.dto.SpreadsheetRequest;

public interface SheetService {

    String createSheetBySchemaDefinition(SpreadsheetRequest spreadsheetRequest);

    SpreadsheetResponse getSheetById(String sheetId);

    SpreadsheetResponse setCellValue(CellValueRequest cellValueRequest);
}
