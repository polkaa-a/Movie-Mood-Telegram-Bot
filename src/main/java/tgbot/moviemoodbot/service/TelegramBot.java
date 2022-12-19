package tgbot.moviemoodbot.service;

import com.vdurmont.emoji.EmojiParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tgbot.moviemoodbot.config.BotConfig;
import tgbot.moviemoodbot.model.BotUser;
import tgbot.moviemoodbot.model.Film;
import tgbot.moviemoodbot.questionnaire.KeyboardManager;
import tgbot.moviemoodbot.questionnaire.buttons.enums.GenreButton;
import tgbot.moviemoodbot.questionnaire.buttons.enums.YearButton;
import tgbot.moviemoodbot.repository.BotUserRepository;
import tgbot.moviemoodbot.storage.StorageManager;

import javax.annotation.PostConstruct;
import javax.persistence.EntityExistsException;
import java.io.File;
import java.sql.Timestamp;
import java.util.*;

import static tgbot.moviemoodbot.questionnaire.buttons.enums.CountryButton.*;
import static tgbot.moviemoodbot.questionnaire.buttons.enums.GenreButton.*;
import static tgbot.moviemoodbot.questionnaire.buttons.enums.RatingButton.*;
import static tgbot.moviemoodbot.questionnaire.buttons.enums.YearButton.YEAR_NO;
import static tgbot.moviemoodbot.questionnaire.buttons.enums.YearButton.YEAR_YES;
import static tgbot.moviemoodbot.service.provider.QuestionsProvider.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

    private static final String HELP_TEXT =
            "/start - command for registration " +
                    "(after it MovieMoodBot knows you and is ready to recommend you some film " +
                    EmojiParser.parseToUnicode(":blush:") + ")" +
                    "\n\n/film - command that will launch a questionnaire " +
                    "and ask the MovieMoodBot to advise you on a good movie " +
                    EmojiParser.parseToUnicode(":film_frames:");
    private static final String USER_EXIST_EXC = "user this such id already exist";
    private final BotConfig config;
    private final BotUserRepository botUserRepository;
    private final FilmService filmService;
    private final GenreService genreService;
    private final CountryService countryService;
    private final KeyboardManager keyboardManager;

    Map<Long, List<Film>> filmsMap = new HashMap<>();
    Map<Long, Boolean> isCastQuestionMap = new HashMap<>();

    @PostConstruct
    private void createMenu() {
        List<BotCommand> commands = new ArrayList<>();
        commands.add(new BotCommand("/start", "get welcome message"));
        commands.add(new BotCommand("/help", "info how to use this bot"));
        commands.add(new BotCommand("/film", "get a movie"));

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
            var firstName = message.getChat().getFirstName();

            switch (messageText) {
                case "/start" -> {
                    registerUser(message);
                    startCommandReceived(chatId, firstName);
                }
                case "/help" -> sendMessage(getMessage(chatId, HELP_TEXT));
                case "/film" -> {
                    if (botUserRepository.findById(message.getChatId()).isEmpty()) {
                        sendMessage(getMessage(chatId,
                                "click /start - MovieMoodBot does not know you, register please" +
                                        EmojiParser.parseToUnicode(":blush:")));
                    } else {
                        isCastQuestionMap.put(chatId, false);
                        runQuestions(chatId);
                    }
                }
                default -> {
                    if (isCastQuestionMap.get(chatId)) {
                        try {
                            int castImageId = Integer.parseInt(messageText);
                            List<Film> films = filmsMap.get(chatId);
                            Optional<Integer> optionalId = films.stream().map(Film::getCastImageId).filter(id -> id == castImageId).findFirst();

                            List<Integer> ids = films.stream().map(Film::getCastImageId).toList();
                            for (Integer id : ids) {
                                StorageManager.deleteLoadedImage("src/main/resources/" + id + StorageManager.FILE_EXTENSION);
                            }

                            if (optionalId.isPresent()) {
                                Film result = filmService.findByCastImageId(castImageId);
                                SendMessage sendMessage = getMessage(chatId,
                                        "<b>Original name: </b>" + result.getOriginalName() + "\n" +
                                                "<b>Russian name: </b>" + result.getRussianName() + "\n" +
                                                "<b>Year: </b>" + result.getYear() + "\n" +
                                                "<b>Rating: </b>" + result.getRating() + "\n\n" +

                                                "<b>QUOTE: </b> \n\n" + "\"" + result.getQuote() + "\"" + "\n\n");
                                sendMessage.enableHtml(true);
                                sendMessage(sendMessage);
                                isCastQuestionMap.put(chatId, false);
                            } else {
                                sendMessage(getMessage(chatId, "Please, type a number from pictures"));
                            }
                        } catch (NumberFormatException e) {
                            sendMessage(getMessage(chatId, "Please, type a number"));
                        }
                    } else {
                        sendMessage(getMessage(chatId,
                                "Bot does not know such a command" +
                                        EmojiParser.parseToUnicode(":frowning_face:")));
                    }
                }
            }
        } else if (update.hasCallbackQuery()) {
            var callbackData = update.getCallbackQuery().getData();
            var chatId = update.getCallbackQuery().getMessage().getChatId();

            filterRating(callbackData, chatId);
            filterGenre(callbackData, chatId);
            filterYear(callbackData, chatId);
            filterCountry(callbackData, chatId);
        }
    }

    private void startCommandReceived(long chatId, String name) {
        var answer = "Hi, " + name + ", nice to meet you! " + EmojiParser.parseToUnicode(":purple_heart:");
        sendMessage(getMessage(chatId, answer));

        log.info("Replied to user " + name);
    }

    private SendMessage getMessage(long chatId, String textToSend) {
        var message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textToSend);
        return message;
    }

    private void sendMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }

    private void sendPhoto(SendPhoto photo) {
        try {
            execute(photo);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }

    private void sendMediaGroup(SendMediaGroup group) {
        try {
            execute(group);
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
                .registeredAt(new Timestamp(System.currentTimeMillis()))
                .build();

        botUserRepository.save(user);

        log.info("User registered: " + user.getUsername());
    }

    private void runQuestions(long chatId) {
        filmsMap.put(chatId, filmService.findAll());
        askRating(chatId);
    }

    private void askRating(long chatId) {
        var message = getMessage(chatId, ASK_RATING);
        sendMessage(keyboardManager.bindRatingQuestion(message));
    }

    private void askChooseRating(long chatId) {
        var message = getMessage(chatId, CHOOSE_RATING);
        sendMessage(keyboardManager.bindChooseRatingQuestion(message));
    }

    public void filterRating(String callbackData, long chatId) {
        if (callbackData.equals(RATING_YES.getId())) {
            askChooseRating(chatId);
        } else if (callbackData.equals(RATING_NO.getId()) || callbackData.equals(RATING_ANY.getId())) {
            askGenre(chatId);
        } else if (callbackData.contains(rating)) {
            filterFilms(filmService.findAllByRating(Float.parseFloat(getCallbackDataValue(callbackData))), chatId);
            askGenre(chatId);
        }
    }

    public void askGenre(long chatId) {
        var message = getMessage(chatId, ASK_GENRE);
        sendMessage(keyboardManager.bindChooseGenreQuestion(message));
    }

    public void askYear(long chatId) {
        var message = getMessage(chatId, ASK_YEAR);
        sendMessage(keyboardManager.bindYearQuestion(message));
    }

    public void askCountry(long chatId) {
        var message = getMessage(chatId, ASK_COUNTRY);
        sendMessage(keyboardManager.bindCountryQuestion(message));
    }

    public void askChooseYear(long chatId) {
        var message = getMessage(chatId, CHOOSE_YEAR);
        sendMessage(keyboardManager.bindChooseYearQuestion(message));
    }

    public void filterGenre(String callbackData, long chatId) {
        if (callbackData.equals(GENRE_YES.getId())) {
            askGenre(chatId);
        } else if (callbackData.equals(GENRE_NO.getId()) || callbackData.equals(GENRE_ANY.getId())) {
            askYear(chatId);
        } else if (callbackData.contains(GenreButton.genre)) {
            filterFilms(filmService.findAllByGenre(genreService.findByName(getCallbackDataValue(callbackData))), chatId);
            askGenreLoop(chatId);
        }
    }

    public void askGenreLoop(long chatId) {
        var message = getMessage(chatId, ASK_GENRE_LOOP);
        sendMessage(keyboardManager.bindGenreQuestion(message));
    }

    public void askChooseCountry(long chatId) {
        var message = getMessage(chatId, CHOOSE_COUNTRY);
        sendMessage(keyboardManager.bindChooseCountryQuestion(message));
    }

    public void askChooseCast(long chatId) {
        isCastQuestionMap.put(chatId, true);
        var message = getMessage(chatId, CHOOSE_CAST);
        sendMessage(message);

        List<Integer> ids = filmsMap.get(chatId).stream().map(Film::getCastImageId).toList();
        System.out.println(ids);

        if (ids.size() == 0) {
            sendMessage(getMessage(chatId, "Sorry, no movies found for your request"));
            return;
        } else if (ids.size() == 1) {
            sendMessage(getMessage(chatId, " . . .  loading photos, wait please  . . . "));
            StorageManager.loadCastImage(ids.get(0));
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setPhoto(new InputFile(new File("src/main/resources/" + ids.get(0) + StorageManager.FILE_EXTENSION)));
            sendPhoto.setChatId(chatId);
            sendPhoto(sendPhoto);
        } else {
            sendMessage(getMessage(chatId, " . . .  loading photos, wait please  . . . "));

            List<InputMedia> photos = new ArrayList<>();

            for (Integer id : ids) {
                StorageManager.loadCastImage(id);
                InputMedia photo = new InputMediaPhoto();
                String fileName = id + StorageManager.FILE_EXTENSION;
                photo.setMedia(new File("src/main/resources/" + fileName), fileName);
                photos.add(photo);
            }

            SendMediaGroup group = new SendMediaGroup(String.valueOf(chatId), photos);
            sendMediaGroup(group);
        }


        var messageWithNumbers = getMessage(chatId, CHOOSE_CAST_NUMBERS);
        sendMessage(messageWithNumbers);

    }

    public void filterYear(String callbackData, long chatId) {
        if (callbackData.equals(YEAR_YES.getId())) {
            askChooseYear(chatId);
        } else if (callbackData.equals(YEAR_NO.getId())) {
            askCountry(chatId);
        } else if (callbackData.contains(YearButton.year)) {
            List<Film> filmsByYear = filmService.findAllByYear(Integer.parseInt(getCallbackDataValue(callbackData)));
            filterFilms(filmsByYear, chatId);
            askCountry(chatId);
        }
    }

    public void filterCountry(String callbackData, long chatId) {
        if (callbackData.equals(COUNTRY_YES.getId())) {
            askChooseCountry(chatId);
        } else if (callbackData.equals(COUNTRY_NO.getId()) || callbackData.equals(COUNTRY_ANY.getId())) {
            askChooseCast(chatId);
        } else if (callbackData.contains(country)) {
            List<Film> filmsByCountry = filmService.findAllByCountry(countryService.findByName(getCallbackDataValue(callbackData)));
            filterFilms(filmsByCountry, chatId);
            askChooseCast(chatId);
        }
    }

    private void filterFilms(List<Film> sample, long chatId) {
        List<Film> films = filmsMap.get(chatId);
        System.out.println("FILMS before filter method - " + films);
        List<Film> toRemove = new ArrayList<>();
        for (Film film : films) {
            if (!sample.contains(film)) {
                toRemove.add(film);
            }
        }
        films.removeAll(toRemove);
        System.out.println("FILMS to remove - " + toRemove);
        System.out.println("FILMS after filter method - " + films);
    }

    private String getCallbackDataValue(String callbackData) {
        return callbackData.split("_")[1];
    }
}
