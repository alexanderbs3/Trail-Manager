package com.example.trailmanager.models;

import java.io.Serializable;
import java.util.Date;

public class TrailPoint implements Serializable {
    private long id;
    private long trailId;
    private double latitude;
    private double longitude;
    private double altitude;
    private float accuracy;
    private float speed;
    private Date timestamp;

    public TrailPoint() {}

    public TrailPoint(double latitude, double longitude, double altitude,
                      float accuracy, float speed, Date timestamp) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.accuracy = accuracy;
        this.speed = speed;
        this.timestamp = timestamp;
    }

    // Getters e Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getTrailId() { return trailId; }
    public void setTrailId(long trailId) { this.trailId = trailId; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public double getAltitude() { return altitude; }
    public void setAltitude(double altitude) { this.altitude = altitude; }

    public float getAccuracy() { return accuracy; }
    public void setAccuracy(float accuracy) { this.accuracy = accuracy; }

    public float getSpeed() { return speed; }
    public void setSpeed(float speed) { this.speed = speed; }

    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }

    // Método para calcular distância entre dois pontos (fórmula de Haversine)
    public static double calculateDistance(TrailPoint p1, TrailPoint p2) {
        final int R = 6371000; // Raio da Terra em metros

        double lat1Rad = Math.toRadians(p1.getLatitude());
        double lat2Rad = Math.toRadians(p2.getLatitude());
        double deltaLat = Math.toRadians(p2.getLatitude() - p1.getLatitude());
        double deltaLon = Math.toRadians(p2.getLongitude() - p1.getLongitude());

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c; // Distância em metros
    }
}