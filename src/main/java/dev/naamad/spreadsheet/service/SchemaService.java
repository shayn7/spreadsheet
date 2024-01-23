package dev.naamad.spreadsheet.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.naamad.spreadsheet.dto.SchemaResponse;
import dev.naamad.spreadsheet.dto.SpreadsheetResponse;

public interface SchemaService {

    String createSchema(String jsonPayload) throws JsonProcessingException;

    SchemaResponse getSchemaById(String schemaId);
}
