package com.example.trailmanager.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "trail_manager.db";
    private static final int DATABASE_VERSION = 1;

    // Tabela de Trilhas
    public static final String TABLE_TRAILS = "trails";
    public static final String COLUMN_TRAIL_ID = "id";
    public static final String COLUMN_TRAIL_NAME = "name";
    public static final String COLUMN_TRAIL_START_TIME = "start_time";
    public static final String COLUMN_TRAIL_END_TIME = "end_time";
    public static final String COLUMN_TRAIL_DISTANCE = "total_distance";
    public static final String COLUMN_TRAIL_MAX_SPEED = "max_speed";
    public static final String COLUMN_TRAIL_AVG_SPEED = "avg_speed";
    public static final String COLUMN_TRAIL_CALORIES = "calories_burned";

    // Tabela de Pontos da Trilha
    public static final String TABLE_TRAIL_POINTS = "trail_points";
    public static final String COLUMN_POINT_ID = "id";
    public static final String COLUMN_POINT_TRAIL_ID = "trail_id";
    public static final String COLUMN_POINT_LATITUDE = "latitude";
    public static final String COLUMN_POINT_LONGITUDE = "longitude";
    public static final String COLUMN_POINT_ALTITUDE = "altitude";
    public static final String COLUMN_POINT_ACCURACY = "accuracy";
    public static final String COLUMN_POINT_SPEED = "speed";
    public static final String COLUMN_POINT_TIMESTAMP = "timestamp";

    // SQL para criar tabela de trilhas
    private static final String CREATE_TABLE_TRAILS =
            "CREATE TABLE " + TABLE_TRAILS + " (" +
                    COLUMN_TRAIL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TRAIL_NAME + " TEXT NOT NULL, " +
                    COLUMN_TRAIL_START_TIME + " INTEGER NOT NULL, " +
                    COLUMN_TRAIL_END_TIME + " INTEGER, " +
                    COLUMN_TRAIL_DISTANCE + " REAL DEFAULT 0, " +
                    COLUMN_TRAIL_MAX_SPEED + " REAL DEFAULT 0, " +
                    COLUMN_TRAIL_AVG_SPEED + " REAL DEFAULT 0, " +
                    COLUMN_TRAIL_CALORIES + " REAL DEFAULT 0" +
                    ")";

    // SQL para criar tabela de pontos
    private static final String CREATE_TABLE_TRAIL_POINTS =
            "CREATE TABLE " + TABLE_TRAIL_POINTS + " (" +
                    COLUMN_POINT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_POINT_TRAIL_ID + " INTEGER NOT NULL, " +
                    COLUMN_POINT_LATITUDE + " REAL NOT NULL, " +
                    COLUMN_POINT_LONGITUDE + " REAL NOT NULL, " +
                    COLUMN_POINT_ALTITUDE + " REAL DEFAULT 0, " +
                    COLUMN_POINT_ACCURACY + " REAL DEFAULT 0, " +
                    COLUMN_POINT_SPEED + " REAL DEFAULT 0, " +
                    COLUMN_POINT_TIMESTAMP + " INTEGER NOT NULL, " +
                    "FOREIGN KEY(" + COLUMN_POINT_TRAIL_ID + ") REFERENCES " +
                    TABLE_TRAILS + "(" + COLUMN_TRAIL_ID + ") ON DELETE CASCADE" +
                    ")";

    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TRAILS);
        db.execSQL(CREATE_TABLE_TRAIL_POINTS);

        // Criar índice para melhorar performance de consultas
        db.execSQL("CREATE INDEX idx_trail_points_trail_id ON " +
                TABLE_TRAIL_POINTS + "(" + COLUMN_POINT_TRAIL_ID + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRAIL_POINTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRAILS);
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }
}