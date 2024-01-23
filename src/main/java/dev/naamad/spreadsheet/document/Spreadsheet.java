package dev.naamad.spreadsheet.document;

import dev.naamad.spreadsheet.model.Column;
import dev.naamad.spreadsheet.model.SheetSchema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "sheets")
public class Spreadsheet {

    @Id
    private String id;
    private Map<String, Object> data;

    
    public Optional<Object> getColumnValueByName(String columnName) {
        if (data != null && data.containsKey(columnName)) {
            return Optional.ofNullable(data.get(columnName));
        }
        return Optional.empty();
    }

    public void setCellValue(String columnName, Object cellValue) {
        if (data == null) {
            data = new HashMap<>();
        }
        data.put(columnName, cellValue);
    }
}
