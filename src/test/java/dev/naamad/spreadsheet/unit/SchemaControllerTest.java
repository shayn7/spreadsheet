package dev.naamad.spreadsheet.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.naamad.spreadsheet.controller.SchemaController;
import dev.naamad.spreadsheet.dto.SchemaResponse;
import dev.naamad.spreadsheet.service.SchemaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class SchemaControllerTest {

    @Mock
    private SchemaService schemaService;

    @InjectMocks
    private SchemaController schemaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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

        // When
        when(schemaService.createSchema(jsonPayload)).thenReturn("schemaId123");
        String result = schemaController.createSchema(jsonPayload);

        // Then
        assertEquals("schemaId123", result);
        verify(schemaService, times(1)).createSchema(jsonPayload);
    }

    @Test
    void testGetSchemaById() {
        // Given
        String schemaId = "schemaId123";
        SchemaResponse mockResponse = new SchemaResponse();
        mockResponse.setData(Map.of(
                "A", "boolean",
                "B", "int",
                "C", "double",
                "D", "string"
        ));

        // When
        when(schemaService.getSchemaById(schemaId)).thenReturn(mockResponse);
        SchemaResponse result = schemaController.getSchemaById(schemaId);

        // Then
        assertEquals(mockResponse, result);
        verify(schemaService, times(1)).getSchemaById(schemaId);
    }
}

