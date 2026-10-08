package com.example.mapa2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;

public class MainActivity extends AppCompatActivity {

    private org.osmdroid.views.MapView map = null;
    private MapView googleMapView;
    private GoogleMap gMap;
    private boolean mostrandoGoogle = false;
    private Marker markerSeleccionado;
    private com.google.android.gms.maps.model.Marker markerGoogleSeleccionado;

    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar FusedLocationClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // --- 1. CONFIGURACIÓN OSMDROID ---
        Configuration.getInstance().setUserAgentValue("Mapa/ benja@gmail.com");

        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.WIKIMEDIA);
        map.setMultiTouchControls(true);

        GeoPoint startPoint = new GeoPoint(-33.498895, -70.616617);
        GeoPoint punto2 = new GeoPoint(-33.498720, -70.616130);
        GeoPoint punto3 = new GeoPoint(-33.498561, -70.615666);

        map.getController().setZoom(20.0);
        Toast.makeText(this, "Tengo el codigo de la discodia", Toast.LENGTH_SHORT).show();

        map.getController().setCenter(startPoint);

        Marker maker = new Marker(map);
        maker.setPosition(startPoint);
        maker.setTitle("Hola");
        maker.setSnippet("Repartidor cerca");

        Marker maker2 = new Marker(map);
        maker2.setPosition(punto2);
        maker2.setIcon(
                ContextCompat.getDrawable(
                        this,
                        R.mipmap.ic_launcher_repartidor_round)
        );
        maker2.setTitle("Hola");
        maker2.setSnippet("Repartidor cerca");

        Marker maker3 = new Marker(map);
        maker3.setPosition(punto3);
        maker3.setIcon(
                ContextCompat.getDrawable(
                        this,
                        R.mipmap.ic_launcher_poli)
        );
        maker3.setTitle("Hola");
        maker3.setSnippet("Repartidor cerca");

        map.getOverlays().add(maker);
        map.getOverlays().add(maker2);
        map.getOverlays().add(maker3);
        map.invalidate();

        MapEventsReceiver puntoSelecionado = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                double lat = p.getLatitude();
                double lon = p.getLongitude();

                Log.d("MAPA", "Latitud " + lat + " Longitud" + lon);

                if (markerSeleccionado != null ) {
                    map.getOverlays().remove(markerSeleccionado);
                }

                markerSeleccionado = new Marker(map);
                markerSeleccionado.setPosition(p);
                markerSeleccionado.setTitle("ACA");
                markerSeleccionado.setAnchor(
                        Marker.ANCHOR_CENTER,
                        Marker.ANCHOR_BOTTOM
                );
                map.getOverlays().add(markerSeleccionado);
                map.invalidate();

                return true;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        };
        MapEventsOverlay eventsOverlay = new MapEventsOverlay(puntoSelecionado);
        map.getOverlays().add(eventsOverlay);

        // --- 2. CONFIGURACIÓN GOOGLE MAPVIEW ---
        googleMapView = findViewById(R.id.googleMapView);
        googleMapView.onCreate(savedInstanceState);
        googleMapView.getMapAsync(googleMap -> {
            gMap = googleMap;
            LatLng punto4 = new LatLng(-33.498720, -70.616130);
            LatLng punto5 = new LatLng(-33.498561, -70.615666);

            Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.policia);
            float scale = getResources().getDisplayMetrics().density;
            int widthPx = (int) (48 * scale + 0.5f);
            int heightPx = (int) (48 * scale + 0.5f);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(imageBitmap, widthPx, heightPx, false);
            com.google.android.gms.maps.model.BitmapDescriptor customIcon = BitmapDescriptorFactory.fromBitmap(scaledBitmap);

            gMap.addMarker(new MarkerOptions()
                    .position(punto4)
                    .title("Hola")
                    .snippet("Repartidor cerca")
                    .icon(customIcon));

            // 2. Segundo marcador
            gMap.addMarker(new MarkerOptions()
                    .position(punto5)
                    .title("Hola")
                    .snippet("Repartidor cerca")
                    .icon(customIcon));

            gMap.setOnMapClickListener(latLng -> {
                double lat = latLng.latitude;
                double lon = latLng.longitude;

                Log.d("GOOGLE_MAPA", "Latitud " + lat + " Longitud " + lon);

                if (markerGoogleSeleccionado != null) {
                    markerGoogleSeleccionado.remove();
                }

                markerGoogleSeleccionado = gMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("ACA"));
            });
        });

        // --- 3. BOTÓN PARA ALTERNAR ENTRE OSMDROID Y GOOGLE MAPS ---
        MaterialButton btnGoogle = findViewById(R.id.btnGoogle);
        btnGoogle.setOnClickListener(v -> {
            if (!mostrandoGoogle) {
                // Ocultar OSMDroid y mostrar Google Maps
                map.setVisibility(View.GONE);
                googleMapView.setVisibility(View.VISIBLE);
                btnGoogle.setText("Ver OSM");
                mostrandoGoogle = true;
            } else {
                // Mostrar OSMDroid y ocultar Google Maps
                googleMapView.setVisibility(View.GONE);
                map.setVisibility(View.VISIBLE);
                btnGoogle.setText("ir a google");
                mostrandoGoogle = false;
            }
        });

        // Solicitar ubicación actual y permisos
        verificarYPedirPermisosUbicacion();
    }

    private void verificarYPedirPermisosUbicacion() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            obtenerUbicacionActual();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                obtenerUbicacionActual();
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void obtenerUbicacionActual() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                double lat = location.getLatitude();
                double lon = location.getLongitude();

                // Centrar OSMDroid
                GeoPoint miUbicacionOsm = new GeoPoint(lat, lon);
                map.getController().setCenter(miUbicacionOsm);
                map.getController().setZoom(18.0);

                Marker markerUsuario = new Marker(map);
                markerUsuario.setPosition(miUbicacionOsm);
                markerUsuario.setTitle("Mi ubicación actual");
                map.getOverlays().add(markerUsuario);
                map.invalidate();

                // Centrar Google Maps y activar punto azul
                if (gMap != null) {
                    LatLng miUbicacionGoogle = new LatLng(lat, lon);
                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(miUbicacionGoogle, 18f));
                    gMap.setMyLocationEnabled(true);
                }
            } else {
                Toast.makeText(this, "No se pudo obtener la ubicación actual", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ==========================================
    // CICLO DE VIDA DE GOOGLE MAPVIEW
    // ==========================================
    @Override
    protected void onResume() {
        super.onResume();
        if (googleMapView != null) googleMapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (googleMapView != null) googleMapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (googleMapView != null) googleMapView.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (googleMapView != null) googleMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (googleMapView != null) googleMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (googleMapView != null) googleMapView.onLowMemory();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (googleMapView != null) googleMapView.onSaveInstanceState(outState);
    }
}
