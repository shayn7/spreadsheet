package dev.naamad.spreadsheet.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class SpreadsheetResponse {

    private String id;
    private Map<String, List<Object>> data;

}
