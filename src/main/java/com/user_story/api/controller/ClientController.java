package com.user_story.api.controller;

import com.user_story.api.dto.client.ClientCreateDto;
import com.user_story.api.dto.client.ClientDto;
import com.user_story.api.dto.client.ClientPage;
import com.user_story.api.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public ResponseEntity<ClientPage> getClientsPage(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size) {
        ClientPage clientsPage = clientService.getClientsPage(page, size);
        return ResponseEntity.ok(clientsPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDto> getClientById(@PathVariable Long id) {
        ClientDto client = clientService.getClientDtoById(id);
        return ResponseEntity.ok(client);
    }

    @PostMapping
    public ResponseEntity<ClientCreateDto> createClient(@RequestBody ClientCreateDto client) {
        ClientCreateDto createdClient = clientService.createClient(client);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdClient);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientCreateDto> updateClient(@PathVariable Long id, @RequestBody ClientCreateDto client) {
        ClientCreateDto updatedClient = clientService.updateClient(id, client);
        return ResponseEntity.ok(updatedClient);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        if (clientService.deleteClient(id)) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }
}
