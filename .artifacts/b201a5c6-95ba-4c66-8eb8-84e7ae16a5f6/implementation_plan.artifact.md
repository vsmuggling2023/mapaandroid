# Implementación de Google Maps y Geolocalización en la misma pantalla

Integrar Google Maps (`MapView`) y geolocalización por GPS (`FusedLocationProviderClient`) manteniendo el esquema actual de OSMDroid, marcadores personalizados, eventos de toque en el mapa y el botón "ir a google".

## User Review Required

> [!IMPORTANT]
> Se agregará el `MapView` de Google Maps y la lógica de permisos de ubicación (`ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION`) directamente en la aplicación manteniendo intacta toda la lógica existente de OSMDroid y marcadores.

## Open Questions

- Ninguna. Se respetará la estructura actual de `MainActivity.java` y `activity_main.xml`.

## Proposed Changes

### Android Manifest & Gradle
- `app/src/main/AndroidManifest.xml`: Permisos de ubicación y API Key de Google Maps ya agregados.
- `app/build.gradle`: Dependencia `play-services-location` ya agregada.

---

### UI & Layout

#### [MODIFY] [activity_main.xml](file:///C:/Users/SantoTomas/Desktop/Mapa2/app/src/main/res/layout/activity_main.xml)
- Agregar el `com.google.android.gms.maps.MapView` oculto por defecto (`android:visibility="gone"`).
- Mantener el botón `btnGoogle` con su icono y texto actual.

---

### Lógica de Actividad

#### [MODIFY] [MainActivity.java](file:///C:/Users/SantoTomas/Desktop/Mapa2/app/src/main/java/com/example/mapa2/MainActivity.java)
- Agregar inicialización y ciclo de vida del `googleMapView`.
- Implementar `FusedLocationProviderClient` para solicitar permisos de ubicación en tiempo de ejecución y centrar la vista en la ubicación actual del usuario tanto en OSMDroid como en Google Maps.
- Mantener los marcadores existentes, eventos de toque y el botón de alternancia entre mapas en la misma pantalla.

## Verification Plan

### Automated Tests
- Ejecutar `gradle_build` (`app:assembleDebug`) para verificar que no existan errores de compilación.

### Manual Verification
- Solicitar al usuario compilar y probar en un dispositivo/emulador con permisos de ubicación y el botón de alternancia de mapas.
