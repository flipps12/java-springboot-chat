package com.example.demo;

import com.example.demo.Repository.UsersRepository;
import com.example.demo.Users.Component.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@AllArgsConstructor
public class ChatHandler extends TextWebSocketHandler {
    private final JwtUtil jwtUtil;
    private final UsersRepository usersRepository;

    private final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<WebSocketSession>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        for (WebSocketSession webSocketSession : sessions) {
            try {
                String[] messages = message.getPayload().split(",", 2);
                String usernameToken = jwtUtil.extractUsername(messages[0]); // validar errores con una api

                String nameUser = usersRepository.findByUsername(usernameToken).get().getName();
                ChatMenssages chat = ChatMenssages.builder()
                        .menssage(messages[1])
                        .data(usernameToken)
                        .build();
                System.out.println(chat.data + ": " + chat.menssage);
                webSocketSession.sendMessage(new TextMessage(chat.data + ": " + chat.menssage));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
