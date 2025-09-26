package com.room_rent.Room_Rent_Application.locale;

import java.util.Locale;

public class LocaleThreadStorage {
    private static final ThreadLocal<Locale> locale = new ThreadLocal<>();

    public static void setLocale(Locale locale) {
        LocaleThreadStorage.locale.set(locale);
    }

    public static Locale getLocale() {
        return locale.get();
    }

}
