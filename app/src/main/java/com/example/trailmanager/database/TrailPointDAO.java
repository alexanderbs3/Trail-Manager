package com.example.trailmanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.trailmanager.models.TrailPoint;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TrailPointDAO {

    private DatabaseHelper dbHelper;

    public TrailPointDAO(Context context) {
        this.dbHelper = DatabaseHelper.getInstance(context);
    }

    // Inserir novo ponto
    public long insertPoint(TrailPoint point) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_POINT_TRAIL_ID, point.getTrailId());
        values.put(DatabaseHelper.COLUMN_POINT_LATITUDE, point.getLatitude());
        values.put(DatabaseHelper.COLUMN_POINT_LONGITUDE, point.getLongitude());
        values.put(DatabaseHelper.COLUMN_POINT_ALTITUDE, point.getAltitude());
        values.put(DatabaseHelper.COLUMN_POINT_ACCURACY, point.getAccuracy());
        values.put(DatabaseHelper.COLUMN_POINT_SPEED, point.getSpeed());
        values.put(DatabaseHelper.COLUMN_POINT_TIMESTAMP, point.getTimestamp().getTime());

        long id = db.insert(DatabaseHelper.TABLE_TRAIL_POINTS, null, values);
        point.setId(id);

        return id;
    }

    // Inserir múltiplos pontos em lote (mais eficiente)
    public void insertPoints(List<TrailPoint> points) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.beginTransaction();
        try {
            for (TrailPoint point : points) {
                ContentValues values = new ContentValues();
                values.put(DatabaseHelper.COLUMN_POINT_TRAIL_ID, point.getTrailId());
                values.put(DatabaseHelper.COLUMN_POINT_LATITUDE, point.getLatitude());
                values.put(DatabaseHelper.COLUMN_POINT_LONGITUDE, point.getLongitude());
                values.put(DatabaseHelper.COLUMN_POINT_ALTITUDE, point.getAltitude());
                values.put(DatabaseHelper.COLUMN_POINT_ACCURACY, point.getAccuracy());
                values.put(DatabaseHelper.COLUMN_POINT_SPEED, point.getSpeed());
                values.put(DatabaseHelper.COLUMN_POINT_TIMESTAMP, point.getTimestamp().getTime());

                long id = db.insert(DatabaseHelper.TABLE_TRAIL_POINTS, null, values);
                point.setId(id);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    // Buscar todos os pontos de uma trilha
    public List<TrailPoint> getPointsByTrailId(long trailId) {
        List<TrailPoint> points = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] columns = {
                DatabaseHelper.COLUMN_POINT_ID,
                DatabaseHelper.COLUMN_POINT_TRAIL_ID,
                DatabaseHelper.COLUMN_POINT_LATITUDE,
                DatabaseHelper.COLUMN_POINT_LONGITUDE,
                DatabaseHelper.COLUMN_POINT_ALTITUDE,
                DatabaseHelper.COLUMN_POINT_ACCURACY,
                DatabaseHelper.COLUMN_POINT_SPEED,
                DatabaseHelper.COLUMN_POINT_TIMESTAMP
        };

        String selection = DatabaseHelper.COLUMN_POINT_TRAIL_ID + " = ?";
        String[] selectionArgs = { String.valueOf(trailId) };
        String orderBy = DatabaseHelper.COLUMN_POINT_TIMESTAMP + " ASC";

        Cursor cursor = db.query(DatabaseHelper.TABLE_TRAIL_POINTS, columns,
                selection, selectionArgs, null, null, orderBy);

        if (cursor.moveToFirst()) {
            do {
                TrailPoint point = cursorToTrailPoint(cursor);
                points.add(point);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return points;
    }

    // Deletar pontos de uma trilha específica
    public int deletePointsByTrailId(long trailId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String whereClause = DatabaseHelper.COLUMN_POINT_TRAIL_ID + " = ?";
        String[] whereArgs = { String.valueOf(trailId) };

        return db.delete(DatabaseHelper.TABLE_TRAIL_POINTS, whereClause, whereArgs);
    }

    // Buscar ponto por ID
    public TrailPoint getPointById(long pointId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] columns = {
                DatabaseHelper.COLUMN_POINT_ID,
                DatabaseHelper.COLUMN_POINT_TRAIL_ID,
                DatabaseHelper.COLUMN_POINT_LATITUDE,
                DatabaseHelper.COLUMN_POINT_LONGITUDE,
                DatabaseHelper.COLUMN_POINT_ALTITUDE,
                DatabaseHelper.COLUMN_POINT_ACCURACY,
                DatabaseHelper.COLUMN_POINT_SPEED,
                DatabaseHelper.COLUMN_POINT_TIMESTAMP
        };

        String selection = DatabaseHelper.COLUMN_POINT_ID + " = ?";
        String[] selectionArgs = { String.valueOf(pointId) };

        Cursor cursor = db.query(DatabaseHelper.TABLE_TRAIL_POINTS, columns,
                selection, selectionArgs, null, null, null);

        TrailPoint point = null;
        if (cursor.moveToFirst()) {
            point = cursorToTrailPoint(cursor);
        }

        cursor.close();
        return point;
    }

    // Contar pontos de uma trilha
    public int getPointCountByTrailId(long trailId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_TRAIL_POINTS +
                " WHERE " + DatabaseHelper.COLUMN_POINT_TRAIL_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(trailId)});

        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();
        return count;
    }

    // Buscar o último ponto de uma trilha
    public TrailPoint getLastPointByTrailId(long trailId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] columns = {
                DatabaseHelper.COLUMN_POINT_ID,
                DatabaseHelper.COLUMN_POINT_TRAIL_ID,
                DatabaseHelper.COLUMN_POINT_LATITUDE,
                DatabaseHelper.COLUMN_POINT_LONGITUDE,
                DatabaseHelper.COLUMN_POINT_ALTITUDE,
                DatabaseHelper.COLUMN_POINT_ACCURACY,
                DatabaseHelper.COLUMN_POINT_SPEED,
                DatabaseHelper.COLUMN_POINT_TIMESTAMP
        };

        String selection = DatabaseHelper.COLUMN_POINT_TRAIL_ID + " = ?";
        String[] selectionArgs = { String.valueOf(trailId) };
        String orderBy = DatabaseHelper.COLUMN_POINT_TIMESTAMP + " DESC";
        String limit = "1";

        Cursor cursor = db.query(DatabaseHelper.TABLE_TRAIL_POINTS, columns,
                selection, selectionArgs, null, null, orderBy, limit);

        TrailPoint point = null;
        if (cursor.moveToFirst()) {
            point = cursorToTrailPoint(cursor);
        }

        cursor.close();
        return point;
    }

    // Converter cursor para objeto TrailPoint
    private TrailPoint cursorToTrailPoint(Cursor cursor) {
        TrailPoint point = new TrailPoint();

        point.setId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_POINT_ID)));
        point.setTrailId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_POINT_TRAIL_ID)));
        point.setLatitude(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_POINT_LATITUDE)));
        point.setLongitude(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_POINT_LONGITUDE)));
        point.setAltitude(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_POINT_ALTITUDE)));
        point.setAccuracy(cursor.getFloat(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_POINT_ACCURACY)));
        point.setSpeed(cursor.getFloat(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_POINT_SPEED)));
        point.setTimestamp(new Date(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_POINT_TIMESTAMP))));

        return point;
    }
}