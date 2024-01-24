package dev.naamad.spreadsheet.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.naamad.spreadsheet.document.Schema;
import dev.naamad.spreadsheet.dto.SchemaResponse;
import dev.naamad.spreadsheet.exception.SchemaNotFoundException;
import dev.naamad.spreadsheet.model.Column;
import dev.naamad.spreadsheet.model.SheetSchema;
import dev.naamad.spreadsheet.repository.SchemaRepository;
import dev.naamad.spreadsheet.service_impl.SchemaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class SchemaServiceImplTest {

    @Mock
    private SchemaRepository schemaRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private SchemaServiceImpl schemaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetSchemaById() {
        // Given
        String sheetId = "sheetId123";
        Schema schema = new Schema();
        schema.setId(sheetId);
        schema.setData(Map.of(
                "A", "boolean",
                "B", "int",
                "C", "double",
                "D", "string"
        ));

        // When
        when(schemaRepository.findById(sheetId)).thenReturn(Optional.of(schema));
        SchemaResponse result = schemaService.getSchemaById(sheetId);

        // Then
        assertEquals(schemaService.mapToSchemaResponse(schema), result);
    }

    @Test
    void testGetSchemaByIdSchemaNotFound() {
        // Given
        String sheetId = "nonExistentSheetId";

        // When
        when(schemaRepository.findById(sheetId)).thenReturn(Optional.empty());

        // Then
        assertThrows(SchemaNotFoundException.class, () -> schemaService.getSchemaById(sheetId));
    }

    @Test
    void testCreateSchema() throws JsonProcessingException {
        // Given
        String jsonPayload = """
                {
                    "columns": [
                        {
                            "name": "A",
                            "type": "boolean"
                        },
                        {
                            "name": "B",
                            "type": "int"
                        },
                        {
                            "name": "C",
                            "type": "double"
                        },
                        {
                            "name": "D",
                            "type": "string"
                        }
                    ]
                }""";

        SheetSchema sheetSchema = new SheetSchema(Arrays.asList(
                new Column("A", "boolean"),
                new Column("B", "int"),
                new Column("C", "double"),
                new Column("D", "string")
        ));
        Schema schema = new Schema();
        schema.setId("schemaId123");

        // When
        when(objectMapper.readValue(jsonPayload, SheetSchema.class)).thenReturn(sheetSchema);
        when(schemaRepository.save(any())).thenReturn(schema);
        String result = schemaService.createSchema(jsonPayload);

        // Then
        assertEquals("schemaId123", result);
    }
}

