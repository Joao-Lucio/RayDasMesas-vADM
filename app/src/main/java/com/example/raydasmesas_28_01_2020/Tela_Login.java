package com.example.raydasmesas_28_01_2020;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raydasmesas_28_01_2020.database.Conexao;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Tela_Login extends AppCompatActivity {

    private EditText edEmail, edSenha;
    private TextView tvRecuperarSenha,tvMensagem;
    private RelativeLayout rl_Botao_Login;
    private DatabaseReference mDatabase;

    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela__login);

        if(!MainActivity.isConnected(getApplicationContext())){
            Toast.makeText(getApplicationContext(), "Conecte-se a uma rede com Internet!", Toast.LENGTH_LONG).show();
        }

        inicializaComponentes();
        eventoClicks();

    }

    public void eventoClicks(){

        rl_Botao_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.isConnected(getApplicationContext())){
                    tvMensagem.setText("");
                    String email = edEmail.getText().toString().trim();
                    String senha = edSenha.getText().toString().trim();

                    if(!email.contains("@") || !email.contains(".")){
                        edEmail.requestFocus();
                        tvMensagem.setText("E-mail Inválido, por favor digite um e-mail válido!");
                        return;
                    }

                    if(senha.length() < 6){
                        tvMensagem.setText("E-mail ou senha incorreto!");
                        edEmail.setText("");
                        edSenha.setText("");
                        return;
                    }

                    login(email,senha);
                }else{
                    Toast.makeText(getApplicationContext(), "Conecte-se a uma rede com Internet!", Toast.LENGTH_LONG).show();
                }
            }
        });

        tvRecuperarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.isConnected(getApplicationContext())){
                    AlertDialog.Builder alerta = new AlertDialog.Builder(Tela_Login.this);
                    alerta.setTitle("Aviso")
                            .setIcon(R.drawable.ic_error)
                            .setMessage("Ao confirmar, um e-mail será enviado para você, onde com ele voçê conseguirá recuperar sua senha. Deseja receber o e-mail?")
                            .setCancelable(false)
                            .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    auth.sendPasswordResetEmail("emaildoadministrador@gmail.com")
                                            .addOnCompleteListener(Tela_Login.this, new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(Tela_Login.this, "Um e-mail foi enviado para recuperar sua senha!", Toast.LENGTH_LONG).show();
                                                    }else{
                                                        Toast.makeText(Tela_Login.this, "E-mail não Registrado", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });
                                }
                            });
                    alerta.show();
                }else{
                    Toast.makeText(getApplicationContext(), "Conecte-se a uma rede com Internet!", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    public void login(String email,String senha){
        auth.signInWithEmailAndPassword(email,senha)
                .addOnCompleteListener(Tela_Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            verificaEmail();

                        }else{
                            tvMensagem.setText("E-mail ou senha incorreto!");
                            edEmail.setText("");
                            edSenha.setText("");
                        }
                    }
                });
    }

    public void verificaEmail(){
        auth = Conexao.getFirebaseAuth();
        user = auth.getCurrentUser();
        mDatabase.child("empresa").orderByChild("identificador").equalTo(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() != 0){
                    Intent i = new Intent(Tela_Login.this,MainActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    tvMensagem.setText("E-mail ou senha incorreto!");
                    edEmail.setText("");
                    edSenha.setText("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void inicializaComponentes(){
        edEmail = (EditText) findViewById(R.id.ed_email);
        edSenha = (EditText) findViewById(R.id.ed_password);
        tvRecuperarSenha = (TextView) findViewById(R.id.tv_recuperarSenha);
        rl_Botao_Login = (RelativeLayout) findViewById(R.id.rl_login);
        tvMensagem = (TextView) findViewById(R.id.tv_mensagem);

        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void onStart() {
        super.onStart();

        auth = Conexao.getFirebaseAuth();
    }
}
