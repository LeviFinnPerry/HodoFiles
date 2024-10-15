package com.example.hodofiles.ui.searchfeed;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PlacesResponse {
    @SerializedName("results")
    private List<PlaceResult> results;

    public List<PlaceResult> getResults() {
        return results;
    }

    public class PlaceResult {
        @SerializedName("name")
        private String name;

        @SerializedName("vicinity")
        private String address;

        @SerializedName("geometry")
        private Geometry geometry;

        @SerializedName("photos")
        private List<Photo> photos;  // List to handle multiple photos

        @SerializedName("rating")
        private double rating;  // Average rating for the place

        @SerializedName("user_ratings_total")
        private int userRatingsTotal;  // Number of user ratings

        @SerializedName("opening_hours")
        private OpeningHours openingHours;

        @SerializedName("place_id")
        private String placeId;  // Unique ID for the place

        @SerializedName("price_level")
        private int priceLevel;  // Price level of the place (0-4 scale)

        @SerializedName("types")
        private List<String> types;  // List of place types (e.g., "restaurant", "museum")

        // Getters
        public String getName() {
            return name;
        }

        public String getAddress() {
            return address;
        }

        public Geometry getGeometry() {
            return geometry;
        }

        public List<Photo> getPhotos() {
            return photos;
        }

        public double getRating() {
            return rating;
        }

        public int getUserRatingsTotal() {
            return userRatingsTotal;
        }

        public OpeningHours getOpeningHours() {
            return openingHours;
        }

        public String getPlaceId() {
            return placeId;
        }

        public int getPriceLevel() {
            return priceLevel;
        }

        public List<String> getTypes() {
            return types;
        }

        // Inner class for Geometry
        public class Geometry {
            @SerializedName("location")
            private LatLng location;

            public LatLng getLocation() {
                return location;
            }
        }

        // Inner class for LatLng (location coordinates)
        public class LatLng {
            @SerializedName("lat")
            private double lat;

            @SerializedName("lng")
            private double lng;

            public double getLat() {
                return lat;
            }

            public double getLng() {
                return lng;
            }
        }

        // Inner class for Photo metadata
        public class Photo {
            @SerializedName("photo_reference")
            private String photoReference;

            @SerializedName("height")
            private int height;

            @SerializedName("width")
            private int width;

            public String getPhotoReference() {
                return photoReference;
            }

            public int getHeight() {
                return height;
            }

            public int getWidth() {
                return width;
            }
        }

        // Inner class for Opening Hours
        public class OpeningHours {
            @SerializedName("open_now")
            private boolean openNow;  // Whether the place is open now

            @SerializedName("weekday_text")
            private List<String> weekdayText;  // Opening hours for each weekday

            public boolean isOpenNow() {
                return openNow;
            }

            public List<String> getWeekdayText() {
                return weekdayText;
            }
        }
    }
}

