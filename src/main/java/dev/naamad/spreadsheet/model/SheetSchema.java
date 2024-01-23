package dev.naamad.spreadsheet.model;

import lombok.Data;

import java.util.List;

@Data
public class SheetSchema {
    private List<Column> columns;
}
