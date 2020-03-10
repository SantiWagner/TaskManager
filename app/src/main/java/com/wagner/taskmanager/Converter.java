package com.wagner.taskmanager;

import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.util.Calendar;
@TypeConverters({Converter.class})
public class Converter {
    @TypeConverter
    public static Calendar toCalendar(Long l) {
        if(l == null)
            return null;
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(l);
        return c;
    }

    @TypeConverter
    public static Long fromCalendar(Calendar c){
        return c == null ? null : c.getTime().getTime();
    }
}