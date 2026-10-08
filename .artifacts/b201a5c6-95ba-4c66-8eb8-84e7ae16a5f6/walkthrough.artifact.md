# Walkthrough - Integración de Google Maps y Geolocalización en la misma pantalla

Se ha integrado exitosamente Google Maps y la geolocalización por GPS (`FusedLocationProviderClient`) manteniendo intacta toda la lógica previa de OSMDroid y marcadores en la misma pantalla.

## Cambios Realizados

### UI & Layout

#### [MODIFY] [activity_main.xml](file:///C:/Users/SantoTomas/Desktop/Mapa2/app/src/main/res/layout/activity_main.xml)
- Se añadió `com.google.android.gms.maps.MapView` superpuesto en el `RelativeLayout`, configurado inicialmente como `android:visibility="gone"`.
- Se mantuvo el botón inferior con el icono y texto para alternar entre mapas.

---

### Lógica de Actividad

#### [MODIFY] [MainActivity.java](file:///C:/Users/SantoTomas/Desktop/Mapa2/app/src/main/java/com/example/mapa2/MainActivity.java)
- **Ciclo de vida**: Se agregaron los métodos requeridos por Google `MapView` (`onResume`, `onStart`, `onStop`, `onPause`, `onDestroy`, `onLowMemory`, `onSaveInstanceState`).
- **Geolocalización**: Se integró `FusedLocationProviderClient` para solicitar permisos de ubicación en tiempo de ejecución (`ACCESS_FINE_LOCATION` / `ACCESS_COARSE_LOCATION`) y obtener la posición actual del usuario.
- **Centrado y Marcadores**: La posición obtenida actualiza tanto OSMDroid (centrando el mapa y añadiendo un marcador) como Google Maps (moviendo la cámara y activando el punto azul nativo `setMyLocationEnabled(true)`).
- **Botón de Alternancia**: Al hacer clic en el botón, alterna dinámicamente la visibilidad entre el mapa de OSMDroid y Google Maps en la misma pantalla.

## Resultados de Verificación

### Compilación
- Se ejecutó `app:assembleDebug` obteniendo **`Build finished successfully.`**.
