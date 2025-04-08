package com.openclassrooms.projet3.service.impl;

import com.openclassrooms.projet3.dto.request.CreateMessageDto;
import com.openclassrooms.projet3.dto.response.MessageResponse;
import com.openclassrooms.projet3.entity.Message;
import com.openclassrooms.projet3.entity.Rental;
import com.openclassrooms.projet3.entity.User;
import com.openclassrooms.projet3.repository.MessageRepository;
import com.openclassrooms.projet3.repository.RentalRepository;
import com.openclassrooms.projet3.repository.UserRepository;
import com.openclassrooms.projet3.service.MessageService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Data
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final RentalRepository rentalRepository;

    public MessageResponse createMessage(CreateMessageDto createMessageDto) {
        // Get user by id
        User user = userRepository.findById(createMessageDto.getUserId()).orElse(null);
        // Get rental by id
        Rental rental = rentalRepository.findById(createMessageDto.getRentalId()).orElse(null);

        // Create new message object and set its properties
        Message message = new Message();
        message.setMessage(createMessageDto.getMessage()); // Set the message text
        message.setUser(user);       // Associate the user
        message.setRental(rental);   // Associate the rental

        messageRepository.save(message); // Save the message

        // Return a response indicating success
        return new MessageResponse().setMessage("Message send with success");
    }
}