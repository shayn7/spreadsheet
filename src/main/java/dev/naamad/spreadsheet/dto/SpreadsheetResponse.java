package dev.naamad.spreadsheet.dto;

import dev.naamad.spreadsheet.model.SheetSchema;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class SpreadsheetResponse {

    private Map<String, Object> data;

}
