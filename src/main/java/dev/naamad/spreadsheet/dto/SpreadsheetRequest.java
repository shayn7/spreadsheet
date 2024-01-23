package dev.naamad.spreadsheet.dto;

import dev.naamad.spreadsheet.model.SheetSchema;
import lombok.Data;


@Data
public class SpreadsheetRequest {
    private SheetSchema schema;
}
