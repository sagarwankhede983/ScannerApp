package com.example.restimplementation;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
public class Dashboard_Nav extends AppCompatActivity implements ScannerFragment.OnFragmentInteractionListener {
    private AppBarConfiguration mAppBarConfiguration;
    String email;
    TextView tv_email_header;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Name = "nameKey";
    public static final String UserId = "UserIDKey";
    public static final String Email = "emailKey";
    public static final String LoginStatus = "login";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard__nav);
        getApplicationContext();
        Toolbar toolbar = findViewById(R.id.toolbar);
        MenuItem logout_icon = findViewById(R.id.logout_tool);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_send, R.id.nav_scanner,R.id.nav_excel,R.id.nav_pdf)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        //Add Text like username @ Email to Navigation Header
        sharedpreferences = getApplicationContext().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.usernameTextView);
        navUsername.setText(sharedpreferences.getString(Name,""));
        TextView navemail=headerView.findViewById(R.id.emailHeaderTextView);
        navemail.setText("("+sharedpreferences.getString(Email," ")+")");


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard__nav, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logout_tool:
                sharedpreferences.edit().clear().apply();
                Intent backto_log_in=new Intent(getApplicationContext(), MainActivity.class);
                startActivity(backto_log_in);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    @Override
    public void onFragmentInteraction(Uri uri) {
    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }
}
