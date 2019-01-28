package com.pindelia.android.vigilsoft.persistence.util;

import android.arch.persistence.room.TypeConverter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTypeConverter {

        @TypeConverter
        public static Date toDate(Long dateInLong){
            return dateInLong == null ? null : new Date(dateInLong);
        }

        @TypeConverter
        public static Long toLong(Date value) {
            return value == null ? null : value.getTime();
        }
}
