package com.statify.telegramBot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.util.Properties;

public class StatifyBot extends TelegramLongPollingBot {

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
        if (update.hasMessage()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (messageText.equals("/statify")) {
                sendWebAppLink(chatId);
            }
        }
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
}
