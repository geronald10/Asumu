package net.ridhoperdana.asumu.halaman_home;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.widget.ImageView;

import net.ridhoperdana.asumu.BaseActivity;
import net.ridhoperdana.asumu.R;

public class HomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        ViewPager viewPager = (ViewPager)findViewById(R.id.viewpager_home);
        viewPager.setOffscreenPageLimit(2);
        setupViewPager(viewPager);
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tab_home);
        tabLayout.setupWithViewPager(viewPager);
        ImageView menu_home      = (ImageView)findViewById(R.id.menu_home);
        ImageView menu_search    = (ImageView)findViewById(R.id.menu_search);
        ImageView menu_message   = (ImageView)findViewById(R.id.menu_message);
        ImageView menu_profil    = (ImageView)findViewById(R.id.menu_person);
        menu_home.setImageTintList(getResources().getColorStateList(R.color.textHitam));
    }

    private void setupViewPager(ViewPager viewPager)
    {
        CommunityPagerAdapter adapter = new CommunityPagerAdapter(getSupportFragmentManager());
        FavoritCommunityFragment fragment_fav = new FavoritCommunityFragment();
        adapter.addFragment(fragment_fav, "FAVORIT");
        PopularCommunityFragment fragment_popular = new PopularCommunityFragment();
        adapter.addFragment(fragment_popular, "POPULAR");
        viewPager.setAdapter(adapter);
    }

}
