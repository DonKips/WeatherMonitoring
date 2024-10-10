package me.looks.weathermonitoring.telegram;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import me.looks.weathermonitoring.Loader;
import me.looks.weathermonitoring.telegram.commands.IBotCommand;
import me.looks.weathermonitoring.telegram.commands.WeatherCommand;

import java.util.HashMap;
import java.util.Map;

public class TelegramBotImpl {
    private final Loader loader;
    private final TelegramBot bot;
    private final HashMap<String, IBotCommand> commands = new HashMap<>();

    public TelegramBotImpl(Loader loader, String token) {
        this.loader = loader;
        bot = new TelegramBot(token);

        registerCommands(new WeatherCommand());
    }

    private void registerCommands(IBotCommand... commands) {
        for (IBotCommand command : commands) {
            this.commands.put(command.getCommandLabel(), command);
        }
    }

    public void registerListener() {
        bot.setUpdatesListener(updates -> {
            for (Update update : updates) {
                String messageText = update.message().text();

                if (messageText != null && messageText.startsWith("/")) {

                    String[] args = messageText.split(" ");
                    IBotCommand command = this.commands.get(args[0]);

                    long chatId = update.message().chat().id();
                    long userId = update.message().from().id();

                    if (command != null) {
                        command.executeCommand(loader, userId, chatId, messageText, args);
                    }
                }

            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        }, error -> {
            if (error.response() != null) {
                System.out.println("Произошла ошибка TelegramBotImpl:");
                System.out.println(error.response().errorCode());
                System.out.println(error.response().description());
            } else {
                error.printStackTrace();
            }
        });
    }

    @SafeVarargs
    public final void sendMessage(long chatId, Message message, Map.Entry<String, String>... placeholders) {
        String stringMessage = message.getMessage();
        for (Map.Entry<String, String> entry : placeholders) {
            stringMessage = stringMessage.replace(entry.getKey(), entry.getValue());
        }
        SendMessage request = new SendMessage(chatId, stringMessage);
        bot.execute(request);
    }

}
