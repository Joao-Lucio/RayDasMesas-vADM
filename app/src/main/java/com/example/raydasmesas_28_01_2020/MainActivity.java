package com.example.raydasmesas_28_01_2020;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.example.raydasmesas_28_01_2020.domain.Agendamento;
import com.example.raydasmesas_28_01_2020.domain.Produto;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private NavController navController;
    private AppBarConfiguration appBarConfiguration;
    private DatabaseReference mDatabase;
    private String idAgendamento;
    private String dia;
    private String tipoAluguel;
    private String qtdCadeira;
    private String qtdMesas;
    private static final String NOTIFICATION_CHANNEL_ID = "12345";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        navigationView = (NavigationView) findViewById(R.id.navigationView);

        navController = Navigation.findNavController(this,R.id.nav_host_fragment);

        appBarConfiguration = new AppBarConfiguration.Builder(new int[]{R.id.agendamento,R.id.entregue,R.id.historico,R.id.grafico,R.id.produto,R.id.promocao})
                .setDrawerLayout(drawerLayout)
                .build();
        NavigationUI.setupActionBarWithNavController(this, navController,appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView,navController);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        if(!isConnected(MainActivity.this)){
            Toast.makeText(MainActivity.this, "Conecte-se a uma rede com Internet!", Toast.LENGTH_LONG).show();
        }

        monitoraAgendamento();
        monitoraEntregue();
    }

    public void monitoraAgendamento(){
        idAgendamento = "";
        tipoAluguel = "";
        dia = "";
        qtdCadeira = "";
        qtdCadeira = "";
        mDatabase.child("agendamento").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    Agendamento a = dataSnapshot.getValue(Agendamento.class);
                    if(!a.getUltmodificou().contains("adm")){
                        if(a.getAdmfoinot().contains("nao")){
                            idAgendamento = a.getUid();
                            dia = a.getDataString();
                            tipoAluguel = a.getTipoAluguel();
                            qtdMesas = String.valueOf(a.getQtdMesas());
                            qtdCadeira = String.valueOf(a.getQtdCadeiras());
                            showNotificacao(idAgendamento,1,"agendamento");
                            a.setAdmfoinot("sim");
                            mDatabase.child("agendamento").child(a.getUid()).setValue(a);
                        }
                    }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Agendamento a = dataSnapshot.getValue(Agendamento.class);
                if(a.getAdmfoinot().contains("nao")){
                    idAgendamento = a.getUid();
                    dia = a.getDataString();
                    showNotificacao(idAgendamento,2,"agendamento");
                    a.setAdmfoinot("sim");
                    mDatabase.child("agendamento").child(a.getUid()).setValue(a);
                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Agendamento a = dataSnapshot.getValue(Agendamento.class);
                if(!a.getUltmodificou().contains("adm")){
                    dia = a.getDataString();
                    showNotificacao("nada",3,"agendamento");
                }

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void monitoraEntregue(){
        idAgendamento = "";
        dia = "";
        mDatabase.child("entregue").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Agendamento a = dataSnapshot.getValue(Agendamento.class);
                if (!a.getUltmodificou().contains("adm")){
                    if(a.getAdmfoinot().contains("nao")){
                        idAgendamento = a.getUid();
                        dia = a.getDataString();
                        showNotificacao(idAgendamento,4,"entregue");
                        a.setAdmfoinot("sim");
                        mDatabase.child("entregue").child(a.getUid()).setValue(a);
                    }

                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void showNotificacao(String idAluguel,int idNotificacao,String tela){
        if(tipoAluguel.contains("g")){
            tipoAluguel = "Jogo Completo";
        }else if(tipoAluguel.contains("m")){
            tipoAluguel = "Somente Mesas";
        }else{
            tipoAluguel = "Somente Cadeiras";
        }
        Intent intent = new Intent(MainActivity.this,Tela_Auxiliar_Click_Notificacao.class);
        intent.putExtra("id",idAluguel);
        intent.putExtra("tela",tela);
        int id = idNotificacao;
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(),id,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String notificationChannelId = NOTIFICATION_CHANNEL_ID;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),notificationChannelId);

        String descricao1 = "Novo agendamento para o dia: " + dia;
        String descricao2 = "Tipo de Aluguel: " + tipoAluguel;
        String descricao3 = qtdMesas + " Mesas e " + qtdCadeira + " Cadeiras";
        builder.setAutoCancel(true)
                .setSmallIcon(R.drawable.im_logo)
                .setVibrate(new long[]{150,300,150,600});

        NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();

        if(id == 1){
            String[] desc = new String[]{descricao1,descricao2,descricao3};
            for(int i=0;i<desc.length;i++){
                style.addLine(desc[i]);
            }
            builder.setStyle(style)
                    .setContentText("Novo agendamento para o dia: " + dia +" Tipo de Aluguel: " + tipoAluguel + " " + qtdMesas + " Mesas e " + qtdCadeira + " Cadeiras")
                    .setContentTitle("Novo Agendamento")
                    .setContentIntent(pi);
        }else if(id == 2){
            builder.setContentTitle("Agendamento Alterado")
                    .setContentText("Agendamento do dia " + dia +" foi alterado!")
                    .setContentIntent(pi);
        }else if(id == 3){
            builder.setContentTitle("Agendamento Cancelado")
                    .setContentText("Agendamento do dia " + dia +" foi cancelado!");
        }else if(id == 4){
            String des1 = "Agendamento que foi entregue no";
            String des2 = "dia " + dia + " foi alterado!";
            String[] desc = new String[]{des1,des2};
            for(int i=0;i<desc.length;i++){
                style.addLine(desc[i]);
            }
            builder.setContentTitle("Agendamento Alterado")
                    .setContentText("Agendamento que foi entregue no dia " + dia +" foi alterado!")
                    .setStyle(style)
                    .setContentIntent(pi);
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mNotificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,"NOTIFICATION_CHANNEL_NAME",importance);
            mNotificationChannel.enableLights(false);
            mNotificationChannel.enableVibration(false);
            assert  notificationManager != null;
            builder.setChannelId(NOTIFICATION_CHANNEL_ID);
            notificationManager.createNotificationChannel(mNotificationChannel);
        }

        notificationManager.notify(id,builder.build());

        try{
            Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone toque = RingtoneManager.getRingtone(this,som);
            toque.play();
        }catch (Exception e){}
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController,appBarConfiguration);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else
            super.onBackPressed();
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if ( cm != null ) {
            NetworkInfo ni = cm.getActiveNetworkInfo();

            return ni != null && ni.isConnected();
        }

        return false;
    }

}
