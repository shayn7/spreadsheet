package dev.naamad.spreadsheet.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.naamad.spreadsheet.dto.SpreadsheetResponse;
import dev.naamad.spreadsheet.exception.InvalidCellValueException;
import dev.naamad.spreadsheet.exception.SheetNotFoundException;
import dev.naamad.spreadsheet.service.SpreadsheetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/api/spreadsheets")
@RequiredArgsConstructor
@RestController
public class SpreadsheetController {

    private final SpreadsheetService spreadsheetService;


    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public String createSheet(@RequestBody String jsonPayload) throws JsonProcessingException {
            return spreadsheetService.createSheet(jsonPayload);
        }

    @GetMapping("/{sheetId}")
    @ResponseStatus(HttpStatus.OK)
    public SpreadsheetResponse getSheetById(@PathVariable String sheetId) {
        return spreadsheetService.getSheetById(sheetId);
    }

    @PutMapping("/{sheetId}/set-cell-value")
    public ResponseEntity<String> setCellValue(
            @PathVariable String sheetId,
            @RequestParam String columnName,
            @RequestParam String cellValue) {

        try {
            // Call the service method to set cell value
            spreadsheetService.setCellValue(sheetId, columnName, cellValue);
            return ResponseEntity.ok("Cell value set successfully.");
        } catch (InvalidCellValueException e) {
            // Handle the case where the cell value is not valid for the column
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (SheetNotFoundException e) {
            // Handle the case where the sheet is not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
