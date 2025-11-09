package com.example.trailmanager.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Trail implements Serializable {
    private long id;
    private String name;
    private Date startTime;
    private Date endTime;
    private double totalDistance; // em metros
    private double maxSpeed; // em m/s
    private double avgSpeed; // em m/s
    private double caloriesBurned;
    private List<TrailPoint> points;

    public Trail() {
        this.points = new ArrayList<>();
    }

    public Trail(String name, Date startTime) {
        this.name = name;
        this.startTime = startTime;
        this.points = new ArrayList<>();
        this.totalDistance = 0;
        this.maxSpeed = 0;
        this.avgSpeed = 0;
        this.caloriesBurned = 0;
    }

    // Getters e Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public double getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(double avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public double getCaloriesBurned() {
        return caloriesBurned;
    }

    public void setCaloriesBurned(double caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }

    public List<TrailPoint> getPoints() {
        return points;
    }

    public void setPoints(List<TrailPoint> points) {
        this.points = points;
    }

    public void addPoint(TrailPoint point) {
        this.points.add(point);
    }

    // Métodos auxiliares
    public long getDuration() {
        if (startTime != null && endTime != null) {
            return endTime.getTime() - startTime.getTime();
        }
        return 0;
    }

    public String getFormattedDuration() {
        long duration = getDuration();
        long seconds = (duration / 1000) % 60;
        long minutes = (duration / (1000 * 60)) % 60;
        long hours = (duration / (1000 * 60 * 60));

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public String getFormattedDistance() {
        if (totalDistance < 1000) {
            return String.format("%.1f m", totalDistance);
        } else {
            return String.format("%.2f km", totalDistance / 1000);
        }
    }

    public String getFormattedAvgSpeed() {
        return String.format("%.2f km/h", avgSpeed * 3.6);
    }

    public String getFormattedMaxSpeed() {
        return String.format("%.2f km/h", maxSpeed * 3.6);
    }

    public String getFormattedCalories() {
        return String.format("%.0f kcal", caloriesBurned);
    }
}