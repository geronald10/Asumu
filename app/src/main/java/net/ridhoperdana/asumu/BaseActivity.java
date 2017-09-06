package net.ridhoperdana.asumu;

import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import net.ridhoperdana.asumu.halaman_home.HomeActivity;

public class BaseActivity extends AppCompatActivity {

    ConstraintLayout fullView;
    Toolbar toolbar;
    protected String nama_activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_base);
        nama_activity = "";
    }

    public String getNama_activity() {
        return nama_activity;
    }

    public void setNama_activity(String nama_activity) {
        this.nama_activity = nama_activity;
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        fullView = (ConstraintLayout)getLayoutInflater().inflate(R.layout.activity_base, null);
        FrameLayout frameLayout = (FrameLayout) fullView.findViewById(R.id.base_framelayout);
        getLayoutInflater().inflate(layoutResID, frameLayout, true);
        super.setContentView(fullView);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView menu_home      = (ImageView)findViewById(R.id.menu_home);
        ImageView menu_search    = (ImageView)findViewById(R.id.menu_search);
        ImageView menu_message   = (ImageView)findViewById(R.id.menu_message);
        ImageView menu_profil    = (ImageView)findViewById(R.id.menu_person);

        menu_home.setOnClickListener(tombolMenu);
        menu_search.setOnClickListener(tombolMenu);
        menu_message.setOnClickListener(tombolMenu);
        menu_profil.setOnClickListener(tombolMenu);
        Log.d("nama activity base: ", getNama_activity());
    }

    private View.OnClickListener tombolMenu = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getId()==R.id.menu_home)
            {
                Intent intent = new Intent(getApplication(), HomeActivity.class);
                startActivity(intent);
            }
            else if(view.getId()==R.id.menu_search)
            {
                Intent intent = new Intent(getApplication(), SearchActivity.class);
                startActivity(intent);
            }
            else if(view.getId()==R.id.menu_message)
            {
                Intent intent = new Intent(getApplication(), MessageActivity.class);
                startActivity(intent);
            }
            else if(view.getId()==R.id.menu_person)
            {
                Intent intent = new Intent(getApplication(), ProfilActivity.class);
                startActivity(intent);
            }
        }
    };
}
