package net.ridhoperdana.asumu;

import android.os.Bundle;
import android.widget.ImageView;

public class SearchActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ImageView menu_home      = (ImageView)findViewById(R.id.menu_home);
        ImageView menu_search    = (ImageView)findViewById(R.id.menu_search);
        ImageView menu_message   = (ImageView)findViewById(R.id.menu_message);
        ImageView menu_profil    = (ImageView)findViewById(R.id.menu_person);

        menu_search.setImageTintList(getResources().getColorStateList(R.color.textHitam));
        toolbar.setBackgroundTintList(getResources().getColorStateList(R.color.warnaUtamaMerah));
    }
}
