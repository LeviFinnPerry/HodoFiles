package com.example.hodofiles.ui.Itinerary;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ItineraryFolder implements Serializable {  // Implement Serializable
    private String name;
    private List<ItineraryStop> stops;  // Ensure stops is initialized properly
    private long creationDate;

    // Constructor initializes stops and assigns provided values
    public ItineraryFolder(String name, List<ItineraryStop> stops, long creationDate) {
        this.name = name;
        this.stops = (stops != null) ? stops : new ArrayList<>();  // Initialize if null
        this.creationDate = creationDate;
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Getter for stops
    public List<ItineraryStop> getStops() {
        return stops;
    }

    // Getter for creationDate
    public long getCreationDate() {
        return creationDate;
    }

    // Add a new stop to the itinerary, ensuring no null reference issues
    public void addStop(ItineraryStop stop) {
        if (stops == null) {
            stops = new ArrayList<>();  // Safety check to prevent null issues
        }
        stops.add(stop);
    }

    // Nested class for representing individual stops
    public static class ItineraryStop implements Serializable {  // Implement Serializable in ItineraryStop as well
        private String name;
        private String address;
        private long timestamp;

        // Constructor for creating a new stop
        public ItineraryStop(String name, String address, long timestamp) {
            this.name = name;
            this.address = address;
            this.timestamp = timestamp;
        }

        // Getter for stop name
        public String getName() {
            return name;
        }

        // Getter for stop address
        public String getAddress() {
            return address;
        }

        // Getter for timestamp
        public long getTimestamp() {
            return timestamp;
        }
    }
}
