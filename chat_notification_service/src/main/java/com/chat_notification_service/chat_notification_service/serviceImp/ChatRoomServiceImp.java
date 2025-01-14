package com.chat_notification_service.chat_notification_service.serviceImp;

import com.chat_notification_service.chat_notification_service.entity.ChatRoom;
import com.chat_notification_service.chat_notification_service.repo.ChatRoomRepository;
import com.chat_notification_service.chat_notification_service.service.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChatRoomServiceImp implements ChatRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Override
    public Optional<String> getChatId(String senderId, String recipientId, boolean createIfNotExist) {
        return chatRoomRepository.findBySenderIdAndRecipientId(senderId, recipientId)
                .map(ChatRoom::getChatId)
                .or(() -> {
                    if (!createIfNotExist) {
                        return Optional.empty();
                    }
                    var chatId = String.format("%s_%s", senderId, recipientId);
                    ChatRoom senderRecipient = ChatRoom.builder()
                            .chatId(chatId)
                            .senderId(senderId)
                            .recipientId(recipientId)
                            .build();
                    ChatRoom recipientSender = ChatRoom.builder()
                            .chatId(chatId)
                            .senderId(recipientId)
                            .recipientId(senderId)
                            .build();
                    chatRoomRepository.save(senderRecipient);
                    chatRoomRepository.save(recipientSender);
                    return Optional.of(chatId);
                });
    }
}
