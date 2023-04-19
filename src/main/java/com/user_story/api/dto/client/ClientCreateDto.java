package com.user_story.api.dto.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClientCreateDto {
    private Long id;
    private String name;
    private String email;
}
