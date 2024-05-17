package it.unimib.sportq.util;

public class PermissionManager {
    private static PermissionManager instance;
    private boolean locationPermissionGranted;

    private PermissionManager() {
        // Private constructor to prevent instantiation.
    }

    public static synchronized PermissionManager getInstance() {
        if (instance == null) {
            instance = new PermissionManager();
        }
        return instance;
    }

    public boolean isLocationPermissionGranted() {
        return locationPermissionGranted;
    }

    public void setLocationPermissionGranted(boolean granted) {
        this.locationPermissionGranted = granted;
    }
}
