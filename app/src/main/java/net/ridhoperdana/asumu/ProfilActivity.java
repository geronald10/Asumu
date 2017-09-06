package net.ridhoperdana.asumu;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.hangchen.twittercover.TwitterCoverListView;

public class ProfilActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        ImageView menu_home      = (ImageView)findViewById(R.id.menu_home);
        ImageView menu_search    = (ImageView)findViewById(R.id.menu_search);
        ImageView menu_message   = (ImageView)findViewById(R.id.menu_message);
        ImageView menu_profil    = (ImageView)findViewById(R.id.menu_person);

        menu_profil.setImageTintList(getResources().getColorStateList(R.color.textHitam));
        TwitterCoverListView listView = (TwitterCoverListView) findViewById(R.id.listview_profil_parallax);
        listView.setHeaderImage(BitmapFactory.decodeResource(getResources(), R.drawable.community));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_expandable_list_item_1,
                new String[]{
                        "Item 1", "Item 2", "Item 3", "Item 4", "Item 5",
                        "Item 6", "Item 7", "Item 8", "Item 9", "Item 10",
                        "Item 11", "Item 12", "Item 13", "Item 14", "Item 15",
                        "Item 16", "Item 17", "Item 18", "Item 19", "Item 20"
                }
        );
        listView.setAdapter(adapter);
    }
}
