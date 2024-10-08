package com.statify;

import com.statify.telegramBot.StatifyBot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;

@SpringBootApplication
public class StatifyApplication {

    public static void main(String[] args) throws TelegramApiException, IOException {

        SpringApplication.run(StatifyApplication.class, args);
     //   TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
      //  botsApi.registerBot(new StatifyBot());
    }

}
