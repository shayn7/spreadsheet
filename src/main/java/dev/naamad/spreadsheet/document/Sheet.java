package dev.naamad.spreadsheet.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "sheet")
public class Sheet {

    @Id
    private String id;
    private Map<String, List<Object>> data;
    @DBRef
    private Schema schema;

    public void setCellValue(String columnName, int cellIndex, Object value) {
        List<Object> columnValues = data.get(columnName);

        if (columnValues == null || cellIndex < 0 || cellIndex >= columnValues.size()) {
            throw new IllegalArgumentException("Invalid column name or cell index");
        }

        columnValues.set(cellIndex, value);
    }

}
