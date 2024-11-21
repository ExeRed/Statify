package com.statify.telegramBot;

import com.statify.telegramBot.repo.UserRepository;
import com.statify.telegramBot.table.UserTelegramId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Component
public class StatifyBot extends TelegramLongPollingBot {

    @Autowired
    private UserRepository userRepository;

    public ConcurrentHashMap<Long, Long> userChatIdMap = new ConcurrentHashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(StatifyBot.class);
    private final String botToken;
    

    public StatifyBot() throws IOException {
        Properties properties = new Properties();
        properties.load(getClass().getClassLoader().getResourceAsStream("application.properties"));
        botToken = properties.getProperty("bot.token");
        if (botToken == null) {
            throw new IOException("Error: bot.token not found in application.properties");
        }
    }

    @Override
    public String getBotUsername() {
        return "Statify";
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            long chatId = update.getMessage().getChatId();
            long userId = update.getMessage().getFrom().getId();
            userChatIdMap.put(userId, chatId);


        }
        if (update.hasMessage()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            String username = update.getMessage().getFrom().getUserName(); // Получаем имя пользователя

            // Проверяем, есть ли такой пользователь в базе данных
            Optional<UserTelegramId> existingUser = userRepository.findByTelegramUserId(String.valueOf(chatId));

            // Если пользователя нет в базе данных, сохраняем его
            if (!existingUser.isPresent()) {
                UserTelegramId chatID = new UserTelegramId(String.valueOf(chatId), username);
                userRepository.save(chatID);
            }

            if (messageText.equals("/statify")) {
                sendWebAppLink(chatId);
            }
        }

    }



    public Long getChatId(Long userId) {
        return userChatIdMap.get(userId);
    }

    private void sendWebAppLink(long chatId) {
        String webAppUrl = getWebAppUrl();
        if (webAppUrl == null) {
            System.err.println("Error: Web app URL not found in environment variable.");
            return;
        }

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText("Click to open Statify \n" + webAppUrl);

        try {
            execute(sendMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getWebAppUrl() {
        return "t.me/statify_sbot/statify";
    }

    public void sendImageToUser(Long chatId, File imageFile) {
        InputFile photo = new InputFile(imageFile);

        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId.toString());
        sendPhoto.setPhoto(photo);

        try {
            execute(sendPhoto);
            System.out.println("Image sent successfully to chat ID: " + chatId);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
