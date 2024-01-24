package dev.naamad.spreadsheet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CellValueRequest {
    private String sheetId;
    private String columnName;
    private int cellIndex;
    private Object cellValue;
}
