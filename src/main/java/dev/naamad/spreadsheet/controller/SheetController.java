package dev.naamad.spreadsheet.controller;

import dev.naamad.spreadsheet.dto.CellValueRequest;
import dev.naamad.spreadsheet.dto.SpreadsheetResponse;
import dev.naamad.spreadsheet.dto.SpreadsheetRequest;
import dev.naamad.spreadsheet.service.SheetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/sheet")
@RequiredArgsConstructor
@RestController
public class SheetController {

    private final SheetService sheetService;


    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public String createSheetBySchemaDefinition(@RequestBody SpreadsheetRequest spreadsheetRequest) {
        return sheetService.createSheetBySchemaDefinition(spreadsheetRequest);
    }

    @GetMapping("/{sheetId}")
    @ResponseStatus(HttpStatus.OK)
    public SpreadsheetResponse getSheetById(@PathVariable String sheetId) {
        return sheetService.getSheetById(sheetId);
    }

    @PutMapping("/set-cell-value")
    @ResponseStatus(HttpStatus.OK)
    public SpreadsheetResponse setCellValue(@RequestBody CellValueRequest cellValueRequest) {
        return sheetService.setCellValue(cellValueRequest);
    }
}
