package com.example.gittest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity implements View.OnClickListener{
    EditText id, pass;
    TextView forgot;
    Button btnActivity1, btnBack;
    DatabaseHelper mydb;
    AccountInfo a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        id = findViewById(R.id.txtLogin);
        pass = findViewById(R.id.txtPassword);

        btnBack = (Button)findViewById(R.id.btnBackLogin);
        btnActivity1 = (Button)findViewById(R.id.btnlogin);
        forgot= findViewById(R.id.forgot);

        btnBack.setOnClickListener(this);
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(),FindId.class);
                startActivity(intent);
            }
        });
        mydb = new DatabaseHelper(this);
        btnActivity1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnlogin:

                        String idnum = id.getText().toString();
                        String password = pass.getText().toString();
                        if (idnum.equals("") || password.equals("")) {
                            Toast.makeText(Login.this, "Enter Username or Password", Toast.LENGTH_SHORT).show();
                        } else {
                            boolean check = mydb.checkUser(idnum,password);
                            if(check){
                                a = new AccountInfo();
                                loginID.id = idnum;
                                a = mydb.readUser(idnum);
                                String fn = a.getFn();
                                String ln = a.getLn();
                                Intent intent1= new Intent(getApplicationContext(),Home.class);
                                intent1.putExtra("userid_key", idnum);
                                intent1.putExtra("firstname", fn);
                                startActivity(intent1);
                                Toast.makeText(Login.this, "Welcome "+ fn +" "+ ln + "!", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(Login.this, "Incorrect Username or Password", Toast.LENGTH_SHORT).show();
                                id.setText("");
                                pass.setText("");
                            }

                        }
                        break;

                    default:
                        throw new IllegalStateException("Unexpected value: " + v.getId());
                }
            }
        });

        }
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnBackLogin:
                    Toast.makeText(Login.this, "Going back to Main Menu!", Toast.LENGTH_SHORT).show();
                    Intent Back = new Intent(this, MainActivity.class);
                    startActivity(Back);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + v.getId());
            }
        }
}