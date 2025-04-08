package com.openclassrooms.projet3.service;

import com.openclassrooms.projet3.dto.request.CreateMessageDto;
import com.openclassrooms.projet3.dto.response.MessageResponse;

public interface MessageService {
    MessageResponse createMessage(CreateMessageDto createMessageDto);
}
