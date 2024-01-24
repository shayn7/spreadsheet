package dev.naamad.spreadsheet.integrtion;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.naamad.spreadsheet.dto.SchemaResponse;
import dev.naamad.spreadsheet.repository.SchemaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SchemaServiceImplIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private SchemaRepository schemaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateAndRetrieveSchema() throws Exception {
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
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);

        ResponseEntity<String> createResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/schema/create",
                request,
                String.class);

        assertEquals(201, createResponse.getStatusCodeValue());

        String schemaId = createResponse.getBody();
        assertNotNull(schemaId);

        ResponseEntity<SchemaResponse> getResponse = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/schema/" + schemaId,
                SchemaResponse.class);

        // Then
        assertEquals(200, getResponse.getStatusCodeValue());
        assertNotNull(getResponse.getBody());
        assertNotNull(getResponse.getBody().getData());
    }
}

