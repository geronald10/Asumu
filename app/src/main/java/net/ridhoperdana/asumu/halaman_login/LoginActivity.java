package net.ridhoperdana.asumu.halaman_login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import net.ridhoperdana.asumu.halaman_home.HomeActivity;
import net.ridhoperdana.asumu.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button tombol_masuk_aplikasi = (Button)findViewById(R.id.tombolMasukAplikasi);
        tombol_masuk_aplikasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_masuk_aplikasi = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent_masuk_aplikasi);
            }
        });
    }
}
