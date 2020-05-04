package com.bercut.koroleva_anglii;

import com.bercut.koroleva_anglii.progress.ModelExecutor;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.MessageEntity;
import com.pengrad.telegrambot.model.Update;
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
    private static final String BOT_LOGIN = "@koroleva_anglii_bot";
    private static final String BOT_NAME = "Королева Англии";

    private TelegramBot telegramBot;
    private GetUpdates getUpdates;

    private final ModelExecutor modelExecutor;

    public TelegramBotService(ModelExecutor modelExecutor) {
        this.modelExecutor = modelExecutor;
    }

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
            getUpdates.offset(update.updateId() + 1);
            if (update.message() != null) {
                //MessageEntity[] entities = update.message().entities();
                //if (entities != null && entities.length > 0 && entities[0].type() == MessageEntity.Type.mention) {
                    String text = update.message().text();
                    //if (text != null && text.contains(BOT_LOGIN)) {
                        System.out.println(update.message().from().firstName() + ": " + text);
                        if (update.message().text().contains("скажи")) {
                            sendMessage(update.message().chat().id(), update.message().text());
                        } else {
                            text = text.replace(BOT_LOGIN, "").trim();
                            String answer = modelExecutor.handleMessage(update.message().from().username(), text);
                            sendMessage(update.message().chat().id(), answer);
                        }
                    //}
                //}
            }
        }
    }

    private void sendMessage(Long chatId, String message) {
        SendMessage request = new SendMessage(chatId, message);
        SendResponse sendResponse = telegramBot.execute(request);
        boolean ok = sendResponse.isOk();
        Message responseMessage = sendResponse.message();
    }
}
