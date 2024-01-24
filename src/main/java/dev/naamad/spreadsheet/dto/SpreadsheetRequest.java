package dev.naamad.spreadsheet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpreadsheetRequest {
    private String schemaId;
    private Map<String, List<Object>> data;
}
