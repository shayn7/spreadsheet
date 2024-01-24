package dev.naamad.spreadsheet.service_impl;

import dev.naamad.spreadsheet.document.Schema;
import dev.naamad.spreadsheet.document.Sheet;
import dev.naamad.spreadsheet.dto.CellValueRequest;
import dev.naamad.spreadsheet.dto.SpreadsheetRequest;
import dev.naamad.spreadsheet.dto.SpreadsheetResponse;
import dev.naamad.spreadsheet.exception.ColumnNotFoundException;
import dev.naamad.spreadsheet.exception.InvalidCellValueException;
import dev.naamad.spreadsheet.exception.SchemaNotFoundException;
import dev.naamad.spreadsheet.exception.SheetNotFoundException;
import dev.naamad.spreadsheet.repository.SchemaRepository;
import dev.naamad.spreadsheet.repository.SheetRepository;
import dev.naamad.spreadsheet.service.SheetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Optional<Schema> schema = schemaRepository.findById(spreadsheetRequest.getSchemaId());
        if(schema.isEmpty()) throw new SchemaNotFoundException("schema not found!");

        Map<String, Object> schemaData = schema.get().getData();
        Map<String, List<Object>> tableData = spreadsheetRequest.getData();

        tableData.forEach((columnName, columnValues) -> {
            if (!schemaData.containsKey(columnName)) {
                throw new ColumnNotFoundException("Column name '" + columnName + "' not found in schema!");
            }

            String expectedType = schemaData.get(columnName).toString();
            if (!isValidColumnValues(columnValues, expectedType)) {
                throw new InvalidCellValueException("Column '" + columnName + "' has incorrect type!");
            }

        });

        Sheet sheet = new Sheet();
        sheet.setSchema(schema.get());
        sheet.setData(tableData);
        return sheetRepository.save(sheet).getId();
    }

    @Override
    public SpreadsheetResponse getSheetById(String sheetId) {
        Optional<Sheet> sheet = sheetRepository.findById(sheetId);
        if(sheet.isEmpty()) throw new SheetNotFoundException("sheet not found!");
        return mapToSheetResponse(sheet.get());
    }

    @Transactional
    @Override
    public SpreadsheetResponse setCellValue(CellValueRequest cellValueRequest) {
        String sheetId = cellValueRequest.getSheetId();
        String columnName = cellValueRequest.getColumnName();
        int cellIndex = cellValueRequest.getCellIndex();
        Object cellValue = cellValueRequest.getCellValue();

        Sheet sheet = sheetRepository.findById(sheetId)
                .orElseThrow(() -> new SheetNotFoundException("Sheet not found!"));

        Schema schema = sheet.getSchema();
        if (schema == null) {
            throw new SchemaNotFoundException("Schema not found for sheet with ID: " + sheetId);
        }

        validateCellValue(cellValue, columnName, schema);
        sheet.setCellValue(columnName, cellIndex, cellValue);
        sheetRepository.save(sheet);

        return mapToSheetResponse(sheet);    }

    private SpreadsheetResponse mapToSheetResponse(Sheet sheet) {
        return SpreadsheetResponse
                .builder()
                .id(sheet.getId())
                .data(sheet.getData())
                .build();
    }

    private void validateCellValue(Object cellValue, String columnName, Schema schema) {
        Object expectedType = schema.getData().get(columnName);
        if (expectedType == null) {
            throw new ColumnNotFoundException("Column name '" + columnName + "' not found in schema!");
        }
        if (!isValueOfType(cellValue, expectedType.toString())) {
            throw new InvalidCellValueException("Cell value has incorrect type for column '" + columnName + "'!");
        }
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

    private boolean isValueOfType(Object value, String expectedType) {
        return switch (expectedType) {
            case "boolean" -> value instanceof Boolean;
            case "int" -> value instanceof Integer;
            case "double" -> value instanceof Double;
            case "string" -> value instanceof String;
            default -> false;
        };
    }
}
