package restaurant.ui;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/** Looks up UI text from messages.properties and fills in the placeholders. */
public final class Messages {
    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("messages");

    private Messages() {
    }

    public static String get(String key, Object... args) {
        return MessageFormat.format(BUNDLE.getString(key), args);
    }
}