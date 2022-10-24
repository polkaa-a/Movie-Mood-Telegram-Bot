package tgbot.moviemoodbot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tgbot.moviemoodbot.config.BotConfig;
import tgbot.moviemoodbot.model.BotUser;
import tgbot.moviemoodbot.repository.BotUserRepository;

import javax.annotation.PostConstruct;
import javax.persistence.EntityExistsException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig config;
    private final BotUserRepository botUserRepository;

    private final String HELP_TEXT = "some help information";
    private final String USER_EXIST_EXC = "user this such id already exist";

    @PostConstruct
    private void createMenu() {
        List<BotCommand> commands = new ArrayList<>();
        commands.add(new BotCommand("/start", "get welcome message"));
        commands.add(new BotCommand("/help", "info how to use this bot"));

        try {
            this.execute(new SetMyCommands(commands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error occurred while set commands list (menu): " + e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            var message = update.getMessage();
            var messageText = message.getText();
            var chatId = message.getChatId();

            switch (messageText) {
                case "/start" -> {
                    registerUser(message);
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                }
                case "/help" -> sendMessage(chatId, HELP_TEXT);
                default -> sendMessage(chatId, "Sorry, command wasn't recognized");
            }
        }
    }

    private void startCommandReceived(long chatId, String name) {
        var answer = "Hi, " + name + ", nice to meet you!";
        sendMessage(chatId, answer);

        log.info("Replied to user " + name);
    }

    private void sendMessage(long chatId, String textToSend) {
        var message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textToSend);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }

    }

    private void registerUser(Message message) {
        if (botUserRepository.findById(message.getChatId()).isPresent()) {
            throw new EntityExistsException(USER_EXIST_EXC);
        }

        var chat = message.getChat();

        var user = BotUser.builder()
                .chatId(chat.getId())
                .username(chat.getUserName())
                .firstName(chat.getFirstName())
                .lastName(chat.getLastName())
                .build();

        botUserRepository.save(user);

        log.info("User registered: " + user.getUsername());

    }
}
