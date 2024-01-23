package dev.naamad.spreadsheet.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;


@Data
public class SpreadsheetRequest {
    private String id;
    private Map<String, List<Object>> data;
}
