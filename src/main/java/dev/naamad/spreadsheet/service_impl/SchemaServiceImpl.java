package dev.naamad.spreadsheet.service_impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.naamad.spreadsheet.document.Schema;
import dev.naamad.spreadsheet.dto.SchemaResponse;
import dev.naamad.spreadsheet.dto.SpreadsheetResponse;
import dev.naamad.spreadsheet.exception.SchemaNotFoundException;
import dev.naamad.spreadsheet.model.Column;
import dev.naamad.spreadsheet.model.SheetSchema;
import dev.naamad.spreadsheet.repository.SchemaRepository;
import dev.naamad.spreadsheet.service.SchemaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SchemaServiceImpl implements SchemaService {

    private final SchemaRepository schemaRepository;
    private final ObjectMapper objectMapper;


    @Override
    public SchemaResponse getSchemaById(String sheetId) {
        Optional<Schema> schema = schemaRepository.findById(sheetId);
        if(schema.isEmpty()) throw new SchemaNotFoundException("schema not found!");
        return mapToSchemaResponse(schema.get());
    }


    public String createSchema(String jsonPayload) throws JsonProcessingException {
        SheetSchema sheetSchema = objectMapper.readValue(jsonPayload, SheetSchema.class);
        Schema schema = new Schema();
        Map<String, Object> data = new HashMap<>();
        List<Column> columns = sheetSchema.getColumns();
        for (Column column : columns) {
            data.put(column.getName(), column.getType());
        }

        schema.setData(data);

        return schemaRepository.save(schema).getId();
    }

    private SchemaResponse mapToSchemaResponse(Schema schema){
        return SchemaResponse
                .builder()
                .data(schema.getData())
                .build();
    }


}

