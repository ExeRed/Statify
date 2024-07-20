package com.statify.telegramBot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

@RestController
@RequestMapping("/sendToTelegram")
public class TelegramController {

    private static final Logger logger = LoggerFactory.getLogger(TelegramController.class);
    private final StatifyBot statifyBot;

    @Autowired
    public TelegramController(StatifyBot statifyBot) {
        this.statifyBot = statifyBot;
    }

    @PostMapping("/send-image")
    public void sendImageToTelegram(@RequestBody ImageRequest imageRequest) {
        Long userId = imageRequest.getUserId();
        Long chatId = statifyBot.getChatId(userId);

        // Декодируем Base64 изображение и сохраняем его во временный файл
        File imageFile = new File("src/main/resources/static/images/statify-logo.png");




        statifyBot.sendImageToUser(chatId, imageFile);

    }


}

class ImageRequest {
    private Long userId;

    // getters and setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
