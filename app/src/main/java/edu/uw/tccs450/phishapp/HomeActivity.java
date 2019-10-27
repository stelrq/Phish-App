package edu.uw.tccs450.phishapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tccs450.phishapp.blog.BlogPost;
import edu.uw.tccs450.phishapp.setlist.SetList;
import edu.uw.tccs450.phishapp.ui.home.SuccessFragment;
import edu.uw.tccs450.phishapp.utils.GetAsyncTask;

public class HomeActivity extends AppCompatActivity {

    private String mJwToken;
    private AppBarConfiguration mAppBarConfiguration;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//        if(savedInstanceState == null) {
//            if(findViewById(R.id.drawer_layout) != null) {
//                Intent intent = getIntent();
//                Bundle bund = intent.getBundleExtra("credentials bundle");
//                SuccessFragment suc = new SuccessFragment();
//                suc.setArguments(bund);
//                getSupportFragmentManager().beginTransaction()
//                        .add(R.id.drawer_layout, suc)
//                        .commit();
//            }
//        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_successFragment, R.id.nav_blogFragment, R.id.nav_setFragment)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.setGraph(R.navigation.mobile_navigation, getIntent().getExtras());
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        HomeActivityArgs args = HomeActivityArgs.fromBundle(getIntent().getExtras());
        mJwToken = args.getJwt();
        navigationView.setNavigationItemSelectedListener(this::onNavigationSelected);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    protected static Spanned parseHTML(String htmlString) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(htmlString, Html.FROM_HTML_MODE_LEGACY);
        } else
            return Html.fromHtml(htmlString);
    }
    private void handleBlogGetOnPostExecute(final String result) {
//parse JSON
        try {
            JSONObject root = new JSONObject(result);
            if (root.has(getString(R.string.keys_json_blogs_response))) {
                JSONObject response = root.getJSONObject(
                        getString(R.string.keys_json_blogs_response));
                if (response.has(getString(R.string.keys_json_blogs_data))) {
                    JSONArray data = response.getJSONArray(
                            getString(R.string.keys_json_blogs_data));
                    BlogPost[] blogs = new BlogPost[data.length()];
                    for(int i = 0; i < data.length(); i++) {
                        JSONObject jsonBlog = data.getJSONObject(i);
                        blogs[i] = (new BlogPost.Builder(
                                jsonBlog.getString(
                                        getString(R.string.keys_json_blogs_pubdate)),
                                jsonBlog.getString(
                                        getString(R.string.keys_json_blogs_title)))
                                .addTeaser(jsonBlog.getString(
                                        getString(R.string.keys_json_blogs_teaser))).addUrl(jsonBlog.getString(
                                        getString(R.string.keys_json_blogs_url)))
                                .build());
                    }
                    MobileNavigationDirections.ActionGlobalNavBlogFragment directions
                            = BlogFragmentDirections.actionGlobalNavBlogFragment(blogs);
                    Navigation.findNavController(this, R.id.nav_host_fragment)
                            .navigate(directions);
                } else {
                    Log.e("ERROR!", "No data array");
                }
            } else {
                Log.e("ERROR!", "No response");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
        }
    }
    private void handleSetGetOnPostExecute(final String result) {
//parse JSON
        try {
            JSONObject root = new JSONObject(result);
            if (root.has(getString(R.string.keys_json_sets_response))) {
                JSONObject response = root.getJSONObject(
                        getString(R.string.keys_json_sets_response));
                if (response.has(getString(R.string.keys_json_sets_data))) {
                    JSONArray data = response.getJSONArray(
                            getString(R.string.keys_json_sets_data));
                    SetList[] sets = new SetList[data.length()];
                    for(int i = 0; i < data.length(); i++) {
                        JSONObject jsonSet = data.getJSONObject(i);
                        sets[i] = (new SetList.Builder(
                                jsonSet.getString(
                                        getString(R.string.keys_json_sets_date)),
                                jsonSet.getString(
                                        getString(R.string.keys_json_sets_location)),
                                (jsonSet.getString(
                                        getString(R.string.keys_json_sets_venue))))
                                        .addUrl(jsonSet.getString(
                                        getString(R.string.keys_json_sets_url)))
                                        .addData(jsonSet.getString(getString(
                                                R.string.keys_json_sets_listdata)))
                                        .addNotes(jsonSet.getString(getString(
                                                R.string.keys_json_sets_notes)))
                                .build());
                    }
                    MobileNavigationDirections.ActionGlobalNavSetFragment directions
                            = SetFragmentDirections.actionGlobalNavSetFragment(sets);
                    Navigation.findNavController(this, R.id.nav_host_fragment)
                            .navigate(directions);
                } else {
                    Log.e("ERROR!", "No data array");
                }
            } else {
                Log.e("ERROR!", "No response");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
        }
    }
    private boolean onNavigationSelected(final MenuItem menuItem) {
        NavController navController =
                Navigation.findNavController(this, R.id.nav_host_fragment);
        switch (menuItem.getItemId()) {
            case R.id.nav_successFragment:
                navController.navigate(R.id.nav_successFragment, getIntent().getExtras());
                break;
            case R.id.nav_blogFragment:
                Uri bloguri = new Uri.Builder()
                        .scheme("https")
                        .appendPath(getString(R.string.ep_base_url))
                        .appendPath(getString(R.string.ep_phish))
                        .appendPath(getString(R.string.ep_blog))
                        .appendPath(getString(R.string.ep_get))
                        .build();
                new GetAsyncTask.Builder(bloguri.toString())
                        .onPostExecute(this::handleBlogGetOnPostExecute)
                        .addHeaderField("authorization", mJwToken) //add the JWT as a header
                        .build().execute();
                break;
            case R.id.nav_setFragment:
                Uri seturi = new Uri.Builder()
                        .scheme("https")
                        .appendPath(getString(R.string.ep_base_url))
                        .appendPath(getString(R.string.ep_phish))
                        .appendPath(getString(R.string.ep_set))
                        .appendPath(getString(R.string.ep_recent))
                        .build();
                new GetAsyncTask.Builder(seturi.toString())
                        .onPostExecute(this::handleSetGetOnPostExecute)
                        .addHeaderField("authorization", mJwToken) //add the JWT as a header
                        .build().execute();
                break;
        }
//Close the drawer
        ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawers();
        return true;
    }
}
