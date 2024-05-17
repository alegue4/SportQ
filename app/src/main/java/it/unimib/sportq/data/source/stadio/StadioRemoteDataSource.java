package it.unimib.sportq.data.source.stadio;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import it.unimib.sportq.BuildConfig;
import it.unimib.sportq.model.Stadio;
import it.unimib.sportq.util.BitmapUtils;
import it.unimib.sportq.util.PermissionManager;

public class StadioRemoteDataSource extends BaseStadioRemoteDataSource{

    private static final String TAG = StadioRemoteDataSource.class.getSimpleName();
    private PlacesClient placesClient;
    private PermissionManager permissionManager;

    public StadioRemoteDataSource(Context context) {
        Places.initialize(context, BuildConfig.PLACES_API_KEY);
        placesClient = Places.createClient(context);
        permissionManager = PermissionManager.getInstance();
    }

    @Override
    public void getListaStadi() {
        if (permissionManager.isLocationPermissionGranted()) {

            RectangularBounds lombardyBounds = RectangularBounds.newInstance(
                    new LatLng(45.032989, 8.680265), // Angolo sud-ovest della Lombardia
                    new LatLng(46.385172, 10.496217)// Angolo nord-est della Lombardia
            );

            // Utilizza la session token di autocompletamento
            AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

            List<Stadio> stadiumPlaces = new ArrayList<>();

            List<String> queries = Arrays.asList(
                    "campo da gioco",
                    "campo di allenamento",
                    "campo sportivo",
                    "centro polisportivo",
                    "centro ricreativo",
                    "centro sportivo",
                    "club sportivo",
                    "complesso sportivo",
                    "impianto sportivo",
                    "parco sportivo",
                    "stadio",
                    "campo da tennis",
                    "campo da calcio",
                    "campo da basket",
                    "campo da baseball",
                    "campo da football",
                    "campo da pallavolo",
                    "pista di atletica"
            );
            AtomicInteger queriesCount = new AtomicInteger(queries.size());
            for (String query : queries) {
                FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                        .setSessionToken(token)
                        .setLocationBias(lombardyBounds)
                        .setCountries("IT")
                        .setQuery(query)
                        .build();
                placesClient.findAutocompletePredictions(request).addOnSuccessListener((response) -> {
                    int predictionsCount = response.getAutocompletePredictions().size();
                    Log.d(TAG, "Query" + query);
                    Log.d(TAG, "Number of predictions: " + predictionsCount);
                    for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                        String placeId = prediction.getPlaceId();
                        Log.d(TAG, "Place Id: " + placeId);
                        // Effettua una chiamata per ottenere i dettagli completi del luogo utilizzando l'ID del luogo
                        List<Place.Field> placeFields = Arrays.asList(
                                Place.Field.ID,
                                Place.Field.NAME,
                                Place.Field.ADDRESS,
                                Place.Field.LAT_LNG,
                                Place.Field.PHOTO_METADATAS);

                        FetchPlaceRequest placeRequest = FetchPlaceRequest.newInstance(placeId, placeFields);

                        placesClient.fetchPlace(placeRequest).addOnSuccessListener((placeResponse) -> {
                            Place place = placeResponse.getPlace();
                            Log.d(TAG, "Place: " + place.getId());
                            LatLng latLng = place.getLatLng();
                            List<PhotoMetadata> metadata = place.getPhotoMetadatas();
                            if (latLng != null && metadata != null) {

                                PhotoMetadata photoMetadata = metadata.get(0);
                                FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                                        .setMaxWidth(500) // Optional.
                                        .setMaxHeight(300) // Optional.
                                        .build();
                                placesClient.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse) -> {

                                    Bitmap bitmap = fetchPhotoResponse.getBitmap();
                                    String bitmapString = BitmapUtils.bitmapToString(bitmap);
                                    double latitude = latLng.latitude;
                                    double longitude = latLng.longitude;
                                    Stadio stadio = new Stadio(
                                            place.getId(),
                                            place.getName(),
                                            place.getAddress(),
                                            latitude,
                                            longitude,
                                            bitmapString
                                    );
                                    stadiumPlaces.add(stadio);
                                    Log.d(TAG, "Stadio Aggiunto");
                                    if(queriesCount.decrementAndGet()==0)
                                    {
                                        Log.d(TAG, " List stadium size"+ stadiumPlaces.size());
                                        stadioResponseCallback.onSuccessFromFindingStadiums(stadiumPlaces);
                                    }

                                }).addOnFailureListener((exception) -> {
                                    if (exception instanceof ApiException) {
                                        final ApiException apiException = (ApiException) exception;
                                        Log.e(TAG, "Place not found: " + exception.getMessage());
                                        final int statusCode = apiException.getStatusCode();
                                    }
                                });

                            }
                        }).addOnFailureListener((placeException) -> {
                            if (placeException instanceof ApiException) {
                                ApiException apiException = (ApiException) placeException;
                                Log.e(TAG, "Place details not found: " + apiException.getStatusCode());
                            }
                        });
                    }
                }).addOnFailureListener((exception) -> {
                    if (exception instanceof ApiException) {
                        ApiException apiException = (ApiException) exception;
                        Log.e(TAG, "Place not found: " + apiException.getStatusCode());
                    }
                });
            }
        } else {
            stadioResponseCallback.onFailureFromFindingStadiums("Position permission is not granted");
        }
    }

}

