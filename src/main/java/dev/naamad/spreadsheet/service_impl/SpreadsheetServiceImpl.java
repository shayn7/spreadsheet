package dev.naamad.spreadsheet.service_impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.naamad.spreadsheet.dto.SpreadsheetRequest;
import dev.naamad.spreadsheet.dto.SpreadsheetResponse;
import dev.naamad.spreadsheet.exception.SpreadsheetRequestException;
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
        if(spreadsheet.isEmpty()) throw new SpreadsheetRequestException("spreadsheet not found!");
        return mapToSpreadsheetResponse(spreadsheet.get());
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
}
