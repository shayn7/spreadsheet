package dev.naamad.spreadsheet.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.naamad.spreadsheet.dto.SchemaResponse;
import dev.naamad.spreadsheet.dto.SpreadsheetResponse;
import dev.naamad.spreadsheet.service.SchemaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/api/schema")
@RequiredArgsConstructor
@RestController
public class SchemaController {

    private final SchemaService schemaService;


    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public String createSchema(@RequestBody String jsonPayload) throws JsonProcessingException {
            return schemaService.createSchema(jsonPayload);
        }

    @GetMapping("/{schemaId}")
    @ResponseStatus(HttpStatus.OK)
    public SchemaResponse getSchemaById(@PathVariable String schemaId) {
        return schemaService.getSchemaById(schemaId);
    }
}
