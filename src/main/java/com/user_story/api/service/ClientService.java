package com.user_story.api.service;

import com.user_story.api.data.domain.Client;
import com.user_story.api.data.repository.ClientRepository;
import com.user_story.api.dto.client.ClientCreateDto;
import com.user_story.api.dto.client.ClientDto;
import com.user_story.api.dto.client.ClientPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public ClientPage getClientsPage(int page, int size) {
        final Page<Client> clients = clientRepository.findAll(PageRequest.of(page, size));
        ClientPage clientPage = new ClientPage();
        clientPage.setClients(clients.getContent().stream().map(client -> {
            ClientDto clientDto = new ClientDto();
            clientDto.setId(client.getId());
            clientDto.setCreateDate(client.getCreateDate());
            clientDto.setLastModifiedDate(client.getLastModifiedDate());
            clientDto.setVersion(client.getVersion());
            clientDto.setName(client.getName());
            clientDto.setEmail(client.getEmail());
            clientDto.setTransactions(client.getTransactions());
            return clientDto;
        }).collect(Collectors.toList()));
        clientPage.setCurrentPage(clients.getNumber());
        clientPage.setLast(clients.isLast());
        clientPage.setTotalElements(clients.getTotalElements());
        return clientPage;
    }

    public Client getClientById(Long id) {
        return clientRepository.findById(id).orElse(null);
    }

    public ClientDto getClientDtoById(Long id) {
        Client client = getClientById(id);
        if (client == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client does not exist!");
        ClientDto clientDto = new ClientDto();
        clientDto.setId(client.getId());
        clientDto.setCreateDate(client.getCreateDate());
        clientDto.setLastModifiedDate(client.getLastModifiedDate());
        clientDto.setVersion(client.getVersion());
        clientDto.setName(client.getName());
        clientDto.setEmail(client.getEmail());
        clientDto.setTransactions(client.getTransactions());
        return clientDto;
    }

    public boolean existsById(Long id) {
        return clientRepository.existsById(id);
    }

    public ClientCreateDto createClient(ClientCreateDto clientCreateDto) {
        Client client = new Client();
        client.setName(clientCreateDto.getName());
        client.setEmail(clientCreateDto.getEmail());
        final Client savedClient = saveClient(client);
        clientCreateDto.setId(savedClient.getId());
        return clientCreateDto;
    }

    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }

    public ClientCreateDto updateClient(Long id, ClientCreateDto client) {
        Client existingClient = getClientById(id);
        if (existingClient == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client does not exist!");
        existingClient.setName(client.getName());
        existingClient.setEmail(client.getEmail());
        client.setId(existingClient.getId());
        clientRepository.save(existingClient);
        return client;
    }

    public void deleteClientById(Long id) {
        clientRepository.deleteById(id);
    }

    public boolean deleteClient(Long id) {
        if (!existsById(id)) return false;
        clientRepository.deleteById(id);
        return true;
    }
}





