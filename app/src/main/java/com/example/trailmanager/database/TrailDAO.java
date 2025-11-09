package com.example.trailmanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.trailmanager.models.Trail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TrailDAO {

    private DatabaseHelper dbHelper;
    private TrailPointDAO pointDAO;

    public TrailDAO(Context context) {
        this.dbHelper = DatabaseHelper.getInstance(context);
        this.pointDAO = new TrailPointDAO(context);
    }

    // Inserir nova trilha
    public long insertTrail(Trail trail) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TRAIL_NAME, trail.getName());
        values.put(DatabaseHelper.COLUMN_TRAIL_START_TIME, trail.getStartTime().getTime());

        if (trail.getEndTime() != null) {
            values.put(DatabaseHelper.COLUMN_TRAIL_END_TIME, trail.getEndTime().getTime());
        }

        values.put(DatabaseHelper.COLUMN_TRAIL_DISTANCE, trail.getTotalDistance());
        values.put(DatabaseHelper.COLUMN_TRAIL_MAX_SPEED, trail.getMaxSpeed());
        values.put(DatabaseHelper.COLUMN_TRAIL_AVG_SPEED, trail.getAvgSpeed());
        values.put(DatabaseHelper.COLUMN_TRAIL_CALORIES, trail.getCaloriesBurned());

        long id = db.insert(DatabaseHelper.TABLE_TRAILS, null, values);
        trail.setId(id);

        return id;
    }

    // Atualizar trilha existente
    public int updateTrail(Trail trail) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TRAIL_NAME, trail.getName());
        values.put(DatabaseHelper.COLUMN_TRAIL_START_TIME, trail.getStartTime().getTime());

        if (trail.getEndTime() != null) {
            values.put(DatabaseHelper.COLUMN_TRAIL_END_TIME, trail.getEndTime().getTime());
        }

        values.put(DatabaseHelper.COLUMN_TRAIL_DISTANCE, trail.getTotalDistance());
        values.put(DatabaseHelper.COLUMN_TRAIL_MAX_SPEED, trail.getMaxSpeed());
        values.put(DatabaseHelper.COLUMN_TRAIL_AVG_SPEED, trail.getAvgSpeed());
        values.put(DatabaseHelper.COLUMN_TRAIL_CALORIES, trail.getCaloriesBurned());

        String whereClause = DatabaseHelper.COLUMN_TRAIL_ID + " = ?";
        String[] whereArgs = { String.valueOf(trail.getId()) };

        return db.update(DatabaseHelper.TABLE_TRAILS, values, whereClause, whereArgs);
    }

    // Deletar trilha por ID
    public int deleteTrail(long trailId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Os pontos serão deletados automaticamente devido ao ON DELETE CASCADE
        String whereClause = DatabaseHelper.COLUMN_TRAIL_ID + " = ?";
        String[] whereArgs = { String.valueOf(trailId) };

        return db.delete(DatabaseHelper.TABLE_TRAILS, whereClause, whereArgs);
    }

    // Deletar trilhas em um intervalo de datas
    public int deleteTrailsByDateRange(long startTime, long endTime) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String whereClause = DatabaseHelper.COLUMN_TRAIL_START_TIME + " >= ? AND " +
                DatabaseHelper.COLUMN_TRAIL_START_TIME + " <= ?";
        String[] whereArgs = { String.valueOf(startTime), String.valueOf(endTime) };

        return db.delete(DatabaseHelper.TABLE_TRAILS, whereClause, whereArgs);
    }

    // Deletar todas as trilhas
    public int deleteAllTrails() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(DatabaseHelper.TABLE_TRAILS, null, null);
    }

    // Buscar trilha por ID
    public Trail getTrailById(long trailId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] columns = {
                DatabaseHelper.COLUMN_TRAIL_ID,
                DatabaseHelper.COLUMN_TRAIL_NAME,
                DatabaseHelper.COLUMN_TRAIL_START_TIME,
                DatabaseHelper.COLUMN_TRAIL_END_TIME,
                DatabaseHelper.COLUMN_TRAIL_DISTANCE,
                DatabaseHelper.COLUMN_TRAIL_MAX_SPEED,
                DatabaseHelper.COLUMN_TRAIL_AVG_SPEED,
                DatabaseHelper.COLUMN_TRAIL_CALORIES
        };

        String selection = DatabaseHelper.COLUMN_TRAIL_ID + " = ?";
        String[] selectionArgs = { String.valueOf(trailId) };

        Cursor cursor = db.query(DatabaseHelper.TABLE_TRAILS, columns,
                selection, selectionArgs, null, null, null);

        Trail trail = null;
        if (cursor.moveToFirst()) {
            trail = cursorToTrail(cursor);
            // Carregar pontos da trilha
            trail.setPoints(pointDAO.getPointsByTrailId(trailId));
        }

        cursor.close();
        return trail;
    }

    // Buscar todas as trilhas
    public List<Trail> getAllTrails() {
        List<Trail> trails = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] columns = {
                DatabaseHelper.COLUMN_TRAIL_ID,
                DatabaseHelper.COLUMN_TRAIL_NAME,
                DatabaseHelper.COLUMN_TRAIL_START_TIME,
                DatabaseHelper.COLUMN_TRAIL_END_TIME,
                DatabaseHelper.COLUMN_TRAIL_DISTANCE,
                DatabaseHelper.COLUMN_TRAIL_MAX_SPEED,
                DatabaseHelper.COLUMN_TRAIL_AVG_SPEED,
                DatabaseHelper.COLUMN_TRAIL_CALORIES
        };

        String orderBy = DatabaseHelper.COLUMN_TRAIL_START_TIME + " DESC";

        Cursor cursor = db.query(DatabaseHelper.TABLE_TRAILS, columns,
                null, null, null, null, orderBy);

        if (cursor.moveToFirst()) {
            do {
                Trail trail = cursorToTrail(cursor);
                trails.add(trail);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return trails;
    }

    // Buscar trilhas por intervalo de datas
    public List<Trail> getTrailsByDateRange(long startTime, long endTime) {
        List<Trail> trails = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] columns = {
                DatabaseHelper.COLUMN_TRAIL_ID,
                DatabaseHelper.COLUMN_TRAIL_NAME,
                DatabaseHelper.COLUMN_TRAIL_START_TIME,
                DatabaseHelper.COLUMN_TRAIL_END_TIME,
                DatabaseHelper.COLUMN_TRAIL_DISTANCE,
                DatabaseHelper.COLUMN_TRAIL_MAX_SPEED,
                DatabaseHelper.COLUMN_TRAIL_AVG_SPEED,
                DatabaseHelper.COLUMN_TRAIL_CALORIES
        };

        String selection = DatabaseHelper.COLUMN_TRAIL_START_TIME + " >= ? AND " +
                DatabaseHelper.COLUMN_TRAIL_START_TIME + " <= ?";
        String[] selectionArgs = { String.valueOf(startTime), String.valueOf(endTime) };
        String orderBy = DatabaseHelper.COLUMN_TRAIL_START_TIME + " DESC";

        Cursor cursor = db.query(DatabaseHelper.TABLE_TRAILS, columns,
                selection, selectionArgs, null, null, orderBy);

        if (cursor.moveToFirst()) {
            do {
                Trail trail = cursorToTrail(cursor);
                trails.add(trail);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return trails;
    }

    // Converter cursor para objeto Trail
    private Trail cursorToTrail(Cursor cursor) {
        Trail trail = new Trail();

        trail.setId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRAIL_ID)));
        trail.setName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRAIL_NAME)));
        trail.setStartTime(new Date(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRAIL_START_TIME))));

        int endTimeIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRAIL_END_TIME);
        if (!cursor.isNull(endTimeIndex)) {
            trail.setEndTime(new Date(cursor.getLong(endTimeIndex)));
        }

        trail.setTotalDistance(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRAIL_DISTANCE)));
        trail.setMaxSpeed(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRAIL_MAX_SPEED)));
        trail.setAvgSpeed(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRAIL_AVG_SPEED)));
        trail.setCaloriesBurned(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TRAIL_CALORIES)));

        return trail;
    }

    // Atualizar apenas o nome da trilha
    public int updateTrailName(long trailId, String newName) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TRAIL_NAME, newName);

        String whereClause = DatabaseHelper.COLUMN_TRAIL_ID + " = ?";
        String[] whereArgs = { String.valueOf(trailId) };

        return db.update(DatabaseHelper.TABLE_TRAILS, values, whereClause, whereArgs);
    }

    // Contar total de trilhas
    public int getTrailCount() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_TRAILS, null);

        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();
        return count;
    }
}