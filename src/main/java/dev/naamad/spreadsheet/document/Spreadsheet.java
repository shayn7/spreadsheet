package dev.naamad.spreadsheet.document;

import dev.naamad.spreadsheet.model.SheetSchema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "sheets")
public class Spreadsheet {

    @Id
    private String id;
    private Map<String, Object> data;
}
