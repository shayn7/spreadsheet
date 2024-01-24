package dev.naamad.spreadsheet.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.naamad.spreadsheet.controller.SheetController;
import dev.naamad.spreadsheet.dto.SpreadsheetRequest;
import dev.naamad.spreadsheet.dto.SpreadsheetResponse;
import dev.naamad.spreadsheet.service.SheetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class SheetControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SheetService sheetService;

    @InjectMocks
    private SheetController sheetController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(sheetController).build();
    }

    @Test
    void createSheetBySchemaDefinition() throws Exception {
        SpreadsheetRequest request = new SpreadsheetRequest();
        request.setSchemaId("65b0db31a3fa7d7f63ab0418");

        Map<String, List<Object>> requestData = new HashMap<>();
        requestData.put("A", Arrays.asList(true, false, true));
        requestData.put("B", Arrays.asList(1, 2, 3));
        requestData.put("C", Arrays.asList(1.5, 2.0, 3.5));
        requestData.put("D", Arrays.asList("apple", "banana", "orange"));
        request.setData(requestData);

        when(sheetService.createSheetBySchemaDefinition(any(SpreadsheetRequest.class))).thenReturn("sheetId");

        mockMvc.perform(post("/api/sheet/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().string("sheetId"));
    }


    @Test
    void getSheetById() throws Exception {
        String sheetId = "65b0db4da3fa7d7f63ab0419";
        SpreadsheetResponse response = new SpreadsheetResponse();
        response.setId(sheetId);

        Map<String, List<Object>> responseData = new HashMap<>();
        responseData.put("A", Arrays.asList(true, false, false));
        responseData.put("B", Arrays.asList(1, 2, 3));
        responseData.put("C", Arrays.asList(1.5, 2.0, 3.5));
        responseData.put("D", Arrays.asList("apple", "banana", "orange"));
        response.setData(responseData);

        when(sheetService.getSheetById(sheetId)).thenReturn(response);

        mockMvc.perform(get("/api/sheet/{sheetId}", sheetId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(sheetId))
                .andExpect(jsonPath("$.data.A[0]").value(true))
                .andExpect(jsonPath("$.data.A[1]").value(false))
                .andExpect(jsonPath("$.data.B[0]").value(1))
                .andExpect(jsonPath("$.data.B[1]").value(2))
                .andExpect(jsonPath("$.data.C[0]").value(1.5))
                .andExpect(jsonPath("$.data.C[1]").value(2.0))
                .andExpect(jsonPath("$.data.D[0]").value("apple"))
                .andExpect(jsonPath("$.data.D[1]").value("banana"))
                .andExpect(jsonPath("$.data.D[2]").value("orange"));
    }

}
