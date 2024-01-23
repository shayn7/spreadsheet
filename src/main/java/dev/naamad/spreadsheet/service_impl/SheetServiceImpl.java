package dev.naamad.spreadsheet.service_impl;

import dev.naamad.spreadsheet.document.Schema;
import dev.naamad.spreadsheet.document.Sheet;
import dev.naamad.spreadsheet.dto.SpreadsheetRequest;
import dev.naamad.spreadsheet.dto.SpreadsheetResponse;
import dev.naamad.spreadsheet.exception.SchemaNotFoundException;
import dev.naamad.spreadsheet.exception.SheetNotFoundException;
import dev.naamad.spreadsheet.repository.SchemaRepository;
import dev.naamad.spreadsheet.repository.SheetRepository;
import dev.naamad.spreadsheet.service.SheetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SheetServiceImpl implements SheetService {

    private final SheetRepository sheetRepository;
    private final SchemaRepository schemaRepository;


    @Override
    public String createSheetBySchemaDefinition(SpreadsheetRequest spreadsheetRequest) {
        Optional<Schema> schema = schemaRepository.findById(spreadsheetRequest.getId());
        if(schema.isEmpty()) throw new SchemaNotFoundException("schema not found!");

        Map<String, Object> schemaData = schema.get().getData();
        Map<String, List<Object>> tableData = spreadsheetRequest.getData();

        tableData.forEach((columnName, columnValues) -> {
            if (!schemaData.containsKey(columnName)) {
                throw new IllegalArgumentException("Column name '" + columnName + "' not found in schema!");
            }

            String expectedType = schemaData.get(columnName).toString();
            if (!isValidColumnValues(columnValues, expectedType)) {
                throw new IllegalArgumentException("Column '" + columnName + "' has incorrect type!");
            }

        });

        Sheet sheet = new Sheet();
        sheet.setData(tableData);
        return sheetRepository.save(sheet).getId();
    }

    @Override
    public SpreadsheetResponse getSheetById(String sheetId) {
        Optional<Sheet> sheet = sheetRepository.findById(sheetId);
        if(sheet.isEmpty()) throw new SheetNotFoundException("sheet not found!");
        return mapToSheetResponse(sheet.get());
    }

    private SpreadsheetResponse mapToSheetResponse(Sheet sheet) {
        return SpreadsheetResponse
                .builder()
                .data(sheet.getData())
                .build();
    }

    private boolean isValidColumnValues(List<Object> columnValues, String expectedType) {
       return switch (expectedType) {
            case "boolean" -> columnValues.stream().allMatch(value -> value instanceof Boolean);
            case "int" -> columnValues.stream().allMatch(value -> value instanceof Integer);
            case "double" -> columnValues.stream().allMatch(value -> value instanceof Double);
            case "string" -> columnValues.stream().allMatch(value -> value instanceof String);
            default -> false;
        };
    }
}
