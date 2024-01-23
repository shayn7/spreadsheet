package dev.naamad.spreadsheet.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.naamad.spreadsheet.document.Spreadsheet;
import dev.naamad.spreadsheet.dto.SpreadsheetRequest;
import dev.naamad.spreadsheet.dto.SpreadsheetResponse;
import dev.naamad.spreadsheet.model.SheetSchema;

public interface SpreadsheetService {

    String createSheet(String jsonPayload) throws JsonProcessingException;

    SpreadsheetResponse getSheetById(String sheetId);
}
