package dev.naamad.spreadsheet.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.naamad.spreadsheet.dto.SpreadsheetResponse;
import dev.naamad.spreadsheet.service.SpreadsheetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
}
