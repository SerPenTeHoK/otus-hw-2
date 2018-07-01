package ru.sergey_gusarov.hw2.util.string.spel;

import java.util.Locale;

public class SpelUserFunctions {
    public static String getLocaleQuestionFile(String input) {
        if (input == null)
            return "${testing.question.file}";
        if (input.equalsIgnoreCase(Locale.getDefault().toLanguageTag()))
            return "${testing.question.file}";
        else
            return "${testing.question.file.en}";
    }
}
