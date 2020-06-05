package com.example.raydasmesas_28_01_2020;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private Marker currentLocationMarker;
    private LatLng currentLocationLatLong;
    private Double lat, lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Intent it = getIntent();
        lat = it.getDoubleExtra("lat",0.0);
        lon = it.getDoubleExtra("lon",0.0);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        startGettingLocations();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng posicao = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions().position(posicao).title("Localização do Aluguel"));

        CameraPosition cameraPosition = new CameraPosition.Builder().zoom(15).target(posicao).build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onLocationChanged(Location location) {
        if(currentLocationMarker != null){
            currentLocationMarker.remove();
        }

        currentLocationLatLong = new LatLng(location.getLatitude(),location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(currentLocationLatLong);
        markerOptions.title("Localização Atual");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
        currentLocationMarker = mMap.addMarker(markerOptions);

        CameraPosition cameraPosition = new CameraPosition.Builder().zoom(15).target(currentLocationLatLong).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private ArrayList findUnAskedPermissions(ArrayList<String> wanted){
        ArrayList result = new ArrayList();

        for(String perm : wanted){
            if(!hasPermission(perm)){
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission){
        if(canAskPermission()){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canAskPermission(){
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("GPS Desativado");
        alertDialog.setMessage("Ativar GPS?");
        alertDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    private void startGettingLocations(){
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        boolean isGPS = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetwork = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean cancGetLocation = true;
        int ALL_PERMISSIONS_RESULT = 10;
        long MIN_DISTANCE_FOR_UPDATES = 10; // Distancia em Metros
        long MIN_TIME_Bw_UPDATES = 1000 * 10; // Time em milissegundos

        ArrayList<String> permissions = new ArrayList<>();
        ArrayList<String> permissionsToRequest;

        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissionsToRequest = findUnAskedPermissions(permissions);

        // Verificando se o gps e internet tão ativos
        if(!isGPS && !isNetwork){
            showSettingsAlert();
        }else{
            // Verificando Permissions

            // verifique as permissões para versões posteriores
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(permissionsToRequest.size() > 0){
                    requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]),ALL_PERMISSIONS_RESULT);
                    cancGetLocation = false;
                }
            }
        }

        // verifica se a localização exata e a localização aproximada foram concedidas
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Permissão Negada", Toast.LENGTH_SHORT).show();
            return;
        }

        // começa a solicitar atualizações de local
        if(cancGetLocation){
            if(isGPS){
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME_Bw_UPDATES,MIN_DISTANCE_FOR_UPDATES,this);
            }else if(isNetwork){
                // do provedor de rede
                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,MIN_TIME_Bw_UPDATES,MIN_DISTANCE_FOR_UPDATES,this);
            }
        }else{
            Toast.makeText(this, "Não é possível obter a Localização", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
