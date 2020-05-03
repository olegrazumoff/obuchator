package com.bercut.koroleva_anglii;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ForceReply;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TelegramBotService {

    private static final String BOT_TOKEN = "1110407913:AAFEMnRMRxpoyB3ZOXjcLEGeV0-CgacAuS4";

    private TelegramBot telegramBot;
    private GetUpdates getUpdates;

    @PostConstruct
    public void initBot() {
        this.telegramBot = new TelegramBot(BOT_TOKEN);
        this.getUpdates = new GetUpdates().limit(100).offset(0).timeout(0);
    }

    @Scheduled(fixedDelay = 1000)
    public void pollBot() {
        GetUpdatesResponse updatesResponse = telegramBot.execute(getUpdates);
        List<Update> updates = updatesResponse.updates();
        System.out.println(String.format("%s: Polled %d updates", LocalDateTime.now(), updates.size()));
        for (Update update : updates) {
            System.out.println(update.message().text());
            if (update.message().text().equalsIgnoreCase("как дела?")) {
                sendMessage(update.message().chat().id(), "Какие дела?");
            } else {
                sendMessage(update.message().chat().id(), "Принято: " + update.message().text());
            }
            getUpdates.offset(update.updateId() + 1);

        }
    }

    private void sendMessage(Long chatId, String message) {
        SendMessage request = new SendMessage(chatId, message);
        SendResponse sendResponse = telegramBot.execute(request);
        boolean ok = sendResponse.isOk();
        Message responseMessage = sendResponse.message();
    }
}
