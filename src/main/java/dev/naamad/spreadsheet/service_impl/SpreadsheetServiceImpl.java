package dev.naamad.spreadsheet.service_impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.naamad.spreadsheet.dto.SpreadsheetResponse;
import dev.naamad.spreadsheet.exception.ColumnNotFoundException;
import dev.naamad.spreadsheet.exception.InvalidCellValueException;
import dev.naamad.spreadsheet.exception.SheetNotFoundException;
import dev.naamad.spreadsheet.document.Spreadsheet;
import dev.naamad.spreadsheet.model.Column;
import dev.naamad.spreadsheet.model.SheetSchema;
import dev.naamad.spreadsheet.repository.SpreadsheetRepository;
import dev.naamad.spreadsheet.service.SpreadsheetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SpreadsheetServiceImpl implements SpreadsheetService {

    private final SpreadsheetRepository spreadsheetRepository;
    private final ObjectMapper objectMapper;


    @Override
    public SpreadsheetResponse getSheetById(String sheetId) {
        Optional<Spreadsheet> spreadsheet = spreadsheetRepository.findById(sheetId);
        if(spreadsheet.isEmpty()) throw new SheetNotFoundException("spreadsheet not found!");
        return mapToSpreadsheetResponse(spreadsheet.get());
    }

    @Override
    public void setCellValue(String sheetId, String columnName, String cellValue) {
        Optional<Spreadsheet> optionalSpreadsheet = spreadsheetRepository.findById(sheetId);

        if (optionalSpreadsheet.isPresent()) {
            Spreadsheet spreadsheet = optionalSpreadsheet.get();
            Optional<Object> optionalCellValue = spreadsheet.getColumnValueByName(columnName);

            if (optionalCellValue.isPresent()) {
                Object existingValue = optionalCellValue.get();

                if (isValidCellValue(existingValue, cellValue)) {
                    spreadsheet.setCellValue(columnName, cellValue);
                    spreadsheetRepository.save(spreadsheet);
                } else {
                    throw new InvalidCellValueException("Invalid value for column '" + columnName + "'.");
                }
            } else {
                throw new ColumnNotFoundException("Column '" + columnName + "' not found.");
            }
        } else {
            throw new SheetNotFoundException("Sheet with ID '" + sheetId + "' not found.");
        }
    }

    public String createSheet(String jsonPayload) throws JsonProcessingException {
        SheetSchema sheetSchema = objectMapper.readValue(jsonPayload, SheetSchema.class);
        Spreadsheet spreadsheet = new Spreadsheet();
        Map<String, Object> data = new HashMap<>();
        List<Column> columns = sheetSchema.getColumns();
        for (Column column : columns) {
            data.put(column.getName(), column.getType());
        }

        spreadsheet.setData(data);

        return spreadsheetRepository.save(spreadsheet).getId();
    }

    private SpreadsheetResponse mapToSpreadsheetResponse(Spreadsheet spreadsheet){
        return SpreadsheetResponse
                .builder()
                .data(spreadsheet.getData())
                .build();
    }

    private boolean isValidCellValue(Object columnType, String cellValue) {
        if (columnType instanceof String columnTypeString) {
            switch (columnTypeString.toLowerCase()) {
                case "boolean":
                    return cellValue.equalsIgnoreCase("true") || cellValue.equalsIgnoreCase("false");
                case "int":
                    try {
                        Integer.parseInt(cellValue);
                        return true;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                case "double":
                    try {
                        Double.parseDouble(cellValue);
                        return true;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                case "string":
                    return true;
                default:
                    return false;
            }
        }
        return false;
    }



}

