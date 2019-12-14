package com.monash.user.smarter;

import android.provider.BaseColumns;

public class DBStructure {

    public static abstract class tableEntry implements BaseColumns {
        public static final String TABLE_NAME = "Hourlyusage";
        public static final String COLUMN_ID = "usageid";
        public static final String COLUMN_DATE = "usagedate";
        public static final String COLUMN_HOUR = "usagehour";
        public static final String COLUMN_FRIDGE = "fridgeusage";
        public static final String COLUMN_AC = "acusage";
        public static final String COLUMN_WASHINGMACHINE = "washingmachineusage";
        public static final String COLUMN_TEMP = "temperature";
        public static final String COLUMN_RES = "resid";
    }
}
