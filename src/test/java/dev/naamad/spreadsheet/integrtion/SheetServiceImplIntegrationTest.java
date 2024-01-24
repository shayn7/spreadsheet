package dev.naamad.spreadsheet.integrtion;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.naamad.spreadsheet.document.Schema;
import dev.naamad.spreadsheet.document.Sheet;
import dev.naamad.spreadsheet.dto.CellValueRequest;
import dev.naamad.spreadsheet.dto.SpreadsheetRequest;
import dev.naamad.spreadsheet.dto.SpreadsheetResponse;
import dev.naamad.spreadsheet.repository.SchemaRepository;
import dev.naamad.spreadsheet.repository.SheetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SheetServiceImplIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private SchemaRepository schemaRepository;

    @Autowired
    private SheetRepository sheetRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // You can add any setup logic here before running each test
    }

    @Test
    void testCreateAndRetrieveSheet() {
        // Given
        Schema schema = new Schema();
        schema.setData(Map.of("A", "string", "B", "int", "C", "boolean"));

        schema = schemaRepository.save(schema);

        SpreadsheetRequest spreadsheetRequest = new SpreadsheetRequest();
        spreadsheetRequest.setSchemaId(schema.getId());

        Map<String, List<Object>> data = new HashMap<>();
        data.put("A", List.of("value1", "value2"));
        data.put("B", List.of(1, 2));
        data.put("C", List.of(true, false));

        spreadsheetRequest.setData(data);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<SpreadsheetRequest> request = new HttpEntity<>(spreadsheetRequest, headers);

        // When
        ResponseEntity<String> createResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/sheet/create",
                request,
                String.class);

        assertEquals(HttpStatus.CREATED.value(), createResponse.getStatusCode().value());
        String sheetId = createResponse.getBody();
        assertNotNull(sheetId);

        ResponseEntity<SpreadsheetResponse> getResponse = restTemplate.exchange(
                "http://localhost:" + port + "/api/sheet/" + sheetId,
                HttpMethod.GET,
                request,
                SpreadsheetResponse.class);

        // Then
        assertEquals(HttpStatus.OK.value(), getResponse.getStatusCode().value());
        assertNotNull(getResponse.getBody());
        assertNotNull(getResponse.getBody().getData());
    }

}
