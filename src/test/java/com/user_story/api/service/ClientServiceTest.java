package com.user_story.api.service;

import com.user_story.api.data.domain.Client;
import com.user_story.api.data.repository.ClientRepository;
import com.user_story.api.dto.client.ClientCreateDto;
import com.user_story.api.dto.client.ClientDto;
import com.user_story.api.dto.client.ClientPage;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.server.ResponseStatusException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {
    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    @Test
    public void testGetClientsPage() {
        int page = 0;
        int size = 10;
        List<Client> clients = new ArrayList<>();
        clients.add(new Client());
        clients.add(new Client());
        clients.add(new Client());
        Page<Client> clientPage = new PageImpl<>(clients, PageRequest.of(page, size), 3);

        when(clientRepository.findAll(PageRequest.of(page, size)))
                .thenReturn(clientPage);

        ClientPage result = clientService.getClientsPage(page, size);

        assertEquals(3, result.getTotalElements());
        assertEquals(0, result.getCurrentPage());
        assertTrue(result.isLast());
        assertEquals(clients.size(), result.getClients().size());
    }

    @Test
    public void testGetClientDtoById() {
        // Define input and expected output
        Long id = 1L;
        Client client = new Client();
        client.setId(id);
        client.setName("John");
        client.setEmail("john@example.com");
        client.setTransactions(new ArrayList<>());

        when(clientRepository.findById(id)).thenReturn(Optional.of(client));

        ClientDto expectedClientDto = new ClientDto();
        expectedClientDto.setId(id);
        expectedClientDto.setName(client.getName());
        expectedClientDto.setEmail(client.getEmail());
        expectedClientDto.setTransactions(client.getTransactions());

        ClientDto result = clientService.getClientDtoById(id);

        assertEquals(expectedClientDto.getId(), result.getId());
        assertEquals(expectedClientDto.getName(), result.getName());
        assertEquals(expectedClientDto.getEmail(), result.getEmail());
        assertEquals(expectedClientDto.getTransactions(), result.getTransactions());
    }

    @Test
    public void testGetClientDtoByIdNotFound() {
        Long id = 1L;
        when(clientRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () ->  clientService.getClientDtoById(id));
    }

    @Test
    public void testExistsById() {
        Long id = 1L;
        when(clientRepository.existsById(id)).thenReturn(true);
        boolean result = clientService.existsById(id);
        assertTrue(result);
    }

    @Test
    public void testCreateClient() {
        ClientCreateDto clientCreateDto = new ClientCreateDto();
        clientCreateDto.setName("Test");
        clientCreateDto.setEmail("test@test.com");

        Client client = new Client();
        client.setName(clientCreateDto.getName());
        client.setEmail(clientCreateDto.getEmail());
        when(clientRepository.save(client)).thenReturn(client);

        ClientCreateDto result = clientService.createClient(clientCreateDto);
        assertEquals(result.getName(), clientCreateDto.getName());
        assertEquals(result.getEmail(), clientCreateDto.getEmail());
    }

    @Test
    public void testSaveClient() {
        Client client = new Client();
        when(clientRepository.save(client)).thenReturn(client);
        Client result = clientService.saveClient(client);
        assertEquals(result, client);
    }

    @Test
    public void testUpdateClientNotFound() {
        Long clientId = 1L;
        ClientCreateDto client = new ClientCreateDto();
        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> clientService.updateClient(clientId, client));
    }

    @Test
    void testDeleteClient() {
        long id = 1L;
        when(clientRepository.existsById(id)).thenReturn(true);
        assertTrue(clientService.deleteClient(id));
    }

    @Test
    void testDeleteClientById() {
        long id = 1L;
        clientService.deleteClientById(id);
        verify(clientRepository, times(1)).deleteById(id);
    }

}
