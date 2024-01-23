package dev.naamad.spreadsheet.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class SchemaResponse {
    private Map<String, Object> data;

}
