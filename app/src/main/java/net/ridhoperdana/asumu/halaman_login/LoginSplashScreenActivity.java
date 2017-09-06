package net.ridhoperdana.asumu.halaman_login;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import net.ridhoperdana.asumu.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class LoginSplashScreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_splash_screen);

        Button masuk_aplikasi = (Button)findViewById(R.id.tombolMasuk);
        masuk_aplikasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogLogin();
            }
        });
    }

    public void showDialogLogin()
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.login_dialog, null);
        dialogBuilder.setView(dialogView);

        ConstraintLayout layout_bl = (ConstraintLayout) dialogView.findViewById(R.id.tombolLoginBukalapak);
        ConstraintLayout layout_google = (ConstraintLayout) dialogView.findViewById(R.id.tombolLoginGoogle);
        ConstraintLayout layout_fb = (ConstraintLayout) dialogView.findViewById(R.id.tombolLoginFacebook);

        layout_bl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_login_bl = new Intent(LoginSplashScreenActivity.this, LoginActivity.class);
                startActivity(intent_login_bl);
            }
        });

//        dialogBuilder.setCancelable(true);
        AlertDialog show_dialog = dialogBuilder.create();
        show_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        show_dialog.show();
    }
}
