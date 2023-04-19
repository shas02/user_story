package com.user_story.api.dto.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClientPage {
    private int CurrentPage;
    private boolean isLast;
    private long totalElements;
    private List<ClientDto> clients;
}
