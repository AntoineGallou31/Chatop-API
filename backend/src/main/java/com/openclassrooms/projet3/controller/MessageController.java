package com.openclassrooms.projet3.controller;

import com.openclassrooms.projet3.dto.request.CreateMessageDto;
import com.openclassrooms.projet3.dto.response.MessageResponse;
import com.openclassrooms.projet3.entity.Message;
import com.openclassrooms.projet3.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@Tag(name = "Messages", description = "Operations related to messages")
public class MessageController {

    private final MessageService messageService;

    // Endpoint to create a new message
    @PostMapping
    @Operation(summary = "Create a new message", responses = {
            @ApiResponse(responseCode = "200", description = "Message created successfully", content = @Content(schema = @Schema(implementation = Message.class))),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public MessageResponse createMessage(@RequestBody CreateMessageDto createMessageDto) {
        return this.messageService.createMessage(createMessageDto);
    }
}