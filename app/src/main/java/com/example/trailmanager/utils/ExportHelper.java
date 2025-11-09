package com.example.trailmanager.utils;

import com.example.trailmanager.models.Trail;
import com.example.trailmanager.models.TrailPoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ExportHelper {

    private static final SimpleDateFormat ISO_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);

    /**
     * Exportar trilha para formato GPX
     */
    public static String toGPX(Trail trail) {
        StringBuilder gpx = new StringBuilder();

        gpx.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        gpx.append("<gpx version=\"1.1\" creator=\"Trail Manager\">\n");
        gpx.append("  <metadata>\n");
        gpx.append("    <name>").append(trail.getName()).append("</name>\n");
        gpx.append("    <time>").append(ISO_FORMAT.format(trail.getStartTime())).append("</time>\n");
        gpx.append("  </metadata>\n");
        gpx.append("  <trk>\n");
        gpx.append("    <name>").append(trail.getName()).append("</name>\n");
        gpx.append("    <trkseg>\n");

        for (TrailPoint point : trail.getPoints()) {
            gpx.append("      <trkpt lat=\"").append(point.getLatitude())
                    .append("\" lon=\"").append(point.getLongitude()).append("\">\n");
            gpx.append("        <ele>").append(point.getAltitude()).append("</ele>\n");
            gpx.append("        <time>").append(ISO_FORMAT.format(point.getTimestamp())).append("</time>\n");
            gpx.append("      </trkpt>\n");
        }

        gpx.append("    </trkseg>\n");
        gpx.append("  </trk>\n");
        gpx.append("</gpx>");

        return gpx.toString();
    }

    /**
     * Exportar trilha para formato KML
     */
    public static String toKML(Trail trail) {
        StringBuilder kml = new StringBuilder();

        kml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        kml.append("<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n");
        kml.append("  <Document>\n");
        kml.append("    <name>").append(trail.getName()).append("</name>\n");
        kml.append("    <description>\n");
        kml.append("      Data: ").append(trail.getStartTime()).append("\n");
        kml.append("      Distância: ").append(trail.getFormattedDistance()).append("\n");
        kml.append("      Duração: ").append(trail.getFormattedDuration()).append("\n");
        kml.append("      Velocidade Média: ").append(trail.getFormattedAvgSpeed()).append("\n");
        kml.append("    </description>\n");
        kml.append("    <Placemark>\n");
        kml.append("      <name>").append(trail.getName()).append("</name>\n");
        kml.append("      <LineString>\n");
        kml.append("        <coordinates>\n");

        for (TrailPoint point : trail.getPoints()) {
            kml.append("          ")
                    .append(point.getLongitude()).append(",")
                    .append(point.getLatitude()).append(",")
                    .append(point.getAltitude()).append("\n");
        }

        kml.append("        </coordinates>\n");
        kml.append("      </LineString>\n");
        kml.append("    </Placemark>\n");
        kml.append("  </Document>\n");
        kml.append("</kml>");

        return kml.toString();
    }

    /**
     * Exportar trilha para formato JSON
     */
    public static String toJSON(Trail trail) {
        try {
            JSONObject json = new JSONObject();
            json.put("name", trail.getName());
            json.put("startTime", ISO_FORMAT.format(trail.getStartTime()));
            json.put("endTime", ISO_FORMAT.format(trail.getEndTime()));
            json.put("totalDistance", trail.getTotalDistance());
            json.put("maxSpeed", trail.getMaxSpeed());
            json.put("avgSpeed", trail.getAvgSpeed());
            json.put("caloriesBurned", trail.getCaloriesBurned());
            json.put("duration", trail.getDuration());

            JSONArray pointsArray = new JSONArray();
            for (TrailPoint point : trail.getPoints()) {
                JSONObject pointJson = new JSONObject();
                pointJson.put("latitude", point.getLatitude());
                pointJson.put("longitude", point.getLongitude());
                pointJson.put("altitude", point.getAltitude());
                pointJson.put("accuracy", point.getAccuracy());
                pointJson.put("speed", point.getSpeed());
                pointJson.put("timestamp", ISO_FORMAT.format(point.getTimestamp()));
                pointsArray.put(pointJson);
            }
            json.put("points", pointsArray);

            return json.toString(2); // Indentação de 2 espaços
        } catch (JSONException e) {
            e.printStackTrace();
            return "{}";
        }
    }

    /**
     * Exportar trilha para formato CSV
     */
    public static String toCSV(Trail trail) {
        StringBuilder csv = new StringBuilder();

        // Cabeçalho
        csv.append("latitude,longitude,altitude,accuracy,speed,timestamp\n");

        // Dados
        for (TrailPoint point : trail.getPoints()) {
            csv.append(point.getLatitude()).append(",");
            csv.append(point.getLongitude()).append(",");
            csv.append(point.getAltitude()).append(",");
            csv.append(point.getAccuracy()).append(",");
            csv.append(point.getSpeed()).append(",");
            csv.append(ISO_FORMAT.format(point.getTimestamp())).append("\n");
        }

        return csv.toString();
    }

    /**
     * Exportar trilha para texto simples
     */
    public static String toText(Trail trail) {
        StringBuilder text = new StringBuilder();

        text.append("=== ").append(trail.getName()).append(" ===\n\n");
        text.append("Data/Hora Início: ").append(trail.getStartTime()).append("\n");
        text.append("Data/Hora Fim: ").append(trail.getEndTime()).append("\n");
        text.append("Duração: ").append(trail.getFormattedDuration()).append("\n");
        text.append("Distância Total: ").append(trail.getFormattedDistance()).append("\n");
        text.append("Velocidade Média: ").append(trail.getFormattedAvgSpeed()).append("\n");
        text.append("Velocidade Máxima: ").append(trail.getFormattedMaxSpeed()).append("\n");
        text.append("Calorias Queimadas: ").append(trail.getFormattedCalories()).append("\n");
        text.append("Total de Pontos: ").append(trail.getPoints().size()).append("\n\n");

        text.append("Compartilhado via Trail Manager\n");

        return text.toString();
    }
}