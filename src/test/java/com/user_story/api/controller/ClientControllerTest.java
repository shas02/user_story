package com.user_story.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.user_story.api.dto.client.ClientCreateDto;
import com.user_story.api.dto.client.ClientDto;
import com.user_story.api.dto.client.ClientPage;
import com.user_story.api.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.ArrayList;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ClientControllerTest {

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientController clientController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(clientController).build();
    }

    @Test
    public void testGetClientsPage() throws Exception {
        int page = 0;
        int size = 10;
        ClientPage clientPage = new ClientPage();
        clientPage.setClients(new ArrayList<>());
        when(clientService.getClientsPage(page, size)).thenReturn(clientPage);
        mockMvc.perform(get("/clients")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clients", hasSize(0)));
    }

    @Test
    public void testGetClientById() throws Exception {
        Long clientId = 1L;
        ClientDto clientDto = new ClientDto();
        clientDto.setId(1L);
        when(clientService.getClientDtoById(clientId)).thenReturn(clientDto);
        mockMvc.perform(get("/clients/{id}", clientId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(clientDto.getId().intValue())));
    }

    @Test
    public void testCreateClient() throws Exception {
        ClientCreateDto clientCreateDto = new ClientCreateDto();
        ClientCreateDto createdClient = new ClientCreateDto();
        createdClient.setId(1L);
        when(clientService.createClient(clientCreateDto)).thenReturn(createdClient);
        mockMvc.perform(post("/clients")
                        .content(asJsonString(clientCreateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(createdClient.getId().intValue())));
    }

    @Test
    public void testUpdateClient() throws Exception {
        Long clientId = 1L;
        ClientCreateDto clientCreateDto = new ClientCreateDto();
        ClientCreateDto updatedClient = new ClientCreateDto();
        updatedClient.setId(clientId);
        when(clientService.updateClient(clientId, clientCreateDto)).thenReturn(updatedClient);
        mockMvc.perform(put("/clients/{id}", clientId)
                        .content(asJsonString(clientCreateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(updatedClient.getId().intValue())));
    }

    @Test
    public void testDeleteClient() throws Exception {
        Long clientId = 1L;
        when(clientService.deleteClient(clientId)).thenReturn(true);
        mockMvc.perform(delete("/clients/{id}", clientId))
                .andExpect(status().isNoContent());
    }

    private String asJsonString(final Object obj) throws JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
}
