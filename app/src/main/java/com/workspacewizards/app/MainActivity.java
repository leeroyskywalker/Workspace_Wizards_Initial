package com.workspacewizards.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    private ImageView btnMenu;
    private Button btnNextPreview;
    private Button btnNewJob;
    private RecyclerView rvActiveWorkOrders;
    private TextView tvUserGreeting, tvJobStatusSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Bind XML layout components to Java variables
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        btnMenu = findViewById(R.id.btn_menu);
        btnNextPreview = findViewById(R.id.btnNextPreview);
        btnNewJob = findViewById(R.id.btnNewJob);
        rvActiveWorkOrders = findViewById(R.id.rvActiveWorkOrders);
        tvUserGreeting = findViewById(R.id.tvUserGreeting);
        tvJobStatusSummary = findViewById(R.id.tvJobStatusSummary);

        // Initialize with sample data
        updateUserUI("alex.field@workspacewizards.com", 4);

        // 2. Set click listener to open the navigation drawer
        btnMenu.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        // 3. Set click listener for Screen 04 preview
        btnNextPreview.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Screen04Activity.class);
            startActivity(intent);
        });

        // 4. Set click listener for Screen 05 (New Job)
        btnNewJob.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Screen05Activity.class);
            startActivity(intent);
        });

        // 5. Setup drawer items
        setupDrawerItemClicks();

        // 6. Setup bottom navigation clicks
        setupBottomNavClicks();
    }

    /**
     * Updates the UI with dynamic user greeting and job status.
     *
     * @param userEmail      The email of the logged-in user.
     * @param activeJobCount The number of active jobs assigned to the user.
     */
    private void updateUserUI(String userEmail, int activeJobCount) {
        if (userEmail == null || userEmail.isEmpty()) return;

        // Extract name from email (e.g. "alex.field" from "alex.field@company.com")
        String namePart = userEmail.split("@")[0];
        // Replace dots/underscores with spaces and capitalize
        String displayName = namePart.replace(".", " ").replace("_", " ");
        String[] nameParts = displayName.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String part : nameParts) {
            if (!part.isEmpty()) {
                sb.append(Character.toUpperCase(part.charAt(0)))
                  .append(part.substring(1))
                  .append(" ");
            }
        }
        displayName = sb.toString().trim();

        // Set dynamic greeting
        tvUserGreeting.setText("Good morning, " + displayName);

        // Set job status summary
        if (activeJobCount > 0) {
            tvJobStatusSummary.setText("You have " + activeJobCount + " active job" + (activeJobCount > 1 ? "s" : "") + " today");
        } else {
            tvJobStatusSummary.setText("You have no active jobs assigned today");
        }
    }

    private void setupDrawerItemClicks() {
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            Intent intent = null;

            if (id == R.id.nav_contacts_drawer) {
                intent = new Intent(MainActivity.this, Screen12Activity.class);
            } else if (id == R.id.nav_companies) {
                intent = new Intent(MainActivity.this, Screen13Activity.class);
            } else if (id == R.id.nav_quotes) {
                intent = new Intent(MainActivity.this, Screen07Activity.class);
            } else if (id == R.id.nav_invoices) {
                intent = new Intent(MainActivity.this, Screen08Activity.class);
            } else if (id == R.id.nav_settings) {
                intent = new Intent(MainActivity.this, Screen17Activity.class);
            }

            if (intent != null) {
                startActivity(intent);
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void setupBottomNavClicks() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            Intent intent = null;

            if (id == R.id.nav_home) {
                // Already on home
                return true;
            } else if (id == R.id.nav_contacts) {
                intent = new Intent(MainActivity.this, Screen12Activity.class);
            } else if (id == R.id.nav_tasks) {
                // Tasks -> Jobs -> Screen07 per prompt
                intent = new Intent(MainActivity.this, Screen07Activity.class);
            } else if (id == R.id.nav_dashboard) {
                intent = new Intent(MainActivity.this, Screen15Activity.class);
            } else if (id == R.id.nav_search) {
                intent = new Intent(MainActivity.this, Screen14Activity.class);
            }

            if (intent != null) {
                startActivity(intent);
            }
            return true;
        });
    }

    // 5. Close the navigation drawer on Back press if it is currently open
    @Override
    public void onBackPressed() {
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
