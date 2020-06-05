package com.example.raydasmesas_28_01_2020;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.example.raydasmesas_28_01_2020.database.Conexao;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Tela_Splash extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela__splash);

        // Metodo que espera determinado tem para executar o que está dentro dele
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(user == null){
                    proximaTelaLogin();
                }else{
                    proximaTelaMain();
                }
            }
        },2000);

    }

    // Metodo que chama proxima tela
    public void proximaTelaLogin(){
        Intent it = new Intent(Tela_Splash.this, Tela_Login.class);
        startActivity(it);
        finish();
    }

    // Metodo que chama proxima tela
    public void proximaTelaMain(){
        Intent it = new Intent(Tela_Splash.this, MainActivity.class);
        startActivity(it);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        auth = Conexao.getFirebaseAuth();
        user = auth.getCurrentUser();

    }
}
