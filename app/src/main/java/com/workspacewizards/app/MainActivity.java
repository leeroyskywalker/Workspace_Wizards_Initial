package com.workspacewizards.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    private ImageView btnMenu;
    private Button btnNewJob;
    private Button btnOrderStatus;
    private RecyclerView rvActiveWorkOrders;
    private View layoutEmptyState;
    private TextView tvUserGreeting, tvJobStatusSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // 1. Bind XML layout components to Java variables
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        btnMenu = findViewById(R.id.btn_menu);
        btnNewJob = findViewById(R.id.btnNewJob);
        btnOrderStatus = findViewById(R.id.btnOrderStatus);
        rvActiveWorkOrders = findViewById(R.id.rvActiveWorkOrders);
        layoutEmptyState = findViewById(R.id.layoutEmptyState);
        tvUserGreeting = findViewById(R.id.tvUserGreeting);
        tvJobStatusSummary = findViewById(R.id.tvJobStatusSummary);

        // 2. Dynamic User Greeting based on system time and authenticated user
        setupDynamicUserGreeting();

        // 3. Load active work orders statistics dynamically from Firestore
        loadActiveWorkOrdersFromFirestore();

        // 4. Set click listener to open the navigation drawer
        if (btnMenu != null) {
            btnMenu.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
        }

        // 5. Set click listener for New Job button
        if (btnNewJob != null) {
            btnNewJob.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, Screen05Activity.class);
                startActivity(intent);
            });
        }

        // 6. Set click listener for Order Status button
        if (btnOrderStatus != null) {
            btnOrderStatus.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, Screen04Activity.class);
                startActivity(intent);
            });
        }

        // 7. Setup drawer items
        setupDrawerItemClicks();

        // 8. Setup bottom navigation clicks
        setupBottomNavClicks();

        // 9. Back press dispatcher callback
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadActiveWorkOrdersFromFirestore();
    }

    private void setupDynamicUserGreeting() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail = null;

        if (currentUser != null && currentUser.getEmail() != null && !currentUser.getEmail().isEmpty()) {
            userEmail = currentUser.getEmail();
        } else if (getIntent() != null && getIntent().hasExtra("EXTRA_USER_EMAIL")) {
            userEmail = getIntent().getStringExtra("EXTRA_USER_EMAIL");
        }

        String displayName = "User";
        if (userEmail != null && userEmail.contains("@")) {
            String namePart = userEmail.split("@")[0];
            String formattedName = namePart.replace(".", " ").replace("_", " ");
            String[] parts = formattedName.split(" ");
            StringBuilder sb = new StringBuilder();
            for (String part : parts) {
                if (!part.isEmpty()) {
                    sb.append(Character.toUpperCase(part.charAt(0)))
                      .append(part.substring(1))
                      .append(" ");
                }
            }
            displayName = sb.toString().trim();
        } else if (currentUser != null && currentUser.getDisplayName() != null && !currentUser.getDisplayName().isEmpty()) {
            displayName = currentUser.getDisplayName();
        }

        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        String timeGreeting;
        if (hour < 12) {
            timeGreeting = "Good Morning";
        } else if (hour < 18) {
            timeGreeting = "Good Afternoon";
        } else {
            timeGreeting = "Good Evening";
        }

        if (tvUserGreeting != null) {
            tvUserGreeting.setText(timeGreeting + ", " + displayName);
        }
    }

    private void loadActiveWorkOrdersFromFirestore() {
        if (db == null) return;

        db.collection("workOrders")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    int activeCount = 0;
                    if (queryDocumentSnapshots != null) {
                        for (DocumentSnapshot doc : queryDocumentSnapshots) {
                            String status = doc.getString("status");
                            if (status == null || !status.equalsIgnoreCase("COMPLETED")) {
                                activeCount++;
                            }
                        }
                    }

                    if (activeCount > 0) {
                        if (tvJobStatusSummary != null) {
                            tvJobStatusSummary.setText("You have " + activeCount + " active job" + (activeCount > 1 ? "s" : "") + " today");
                        }
                        if (rvActiveWorkOrders != null) rvActiveWorkOrders.setVisibility(View.VISIBLE);
                        if (layoutEmptyState != null) layoutEmptyState.setVisibility(View.GONE);
                    } else {
                        if (tvJobStatusSummary != null) {
                            tvJobStatusSummary.setText("You have no active jobs assigned today");
                        }
                        if (rvActiveWorkOrders != null) rvActiveWorkOrders.setVisibility(View.GONE);
                        if (layoutEmptyState != null) layoutEmptyState.setVisibility(View.VISIBLE);
                    }
                })
                .addOnFailureListener(e -> {
                    if (tvJobStatusSummary != null) {
                        tvJobStatusSummary.setText("You have no active jobs assigned today");
                    }
                    if (rvActiveWorkOrders != null) rvActiveWorkOrders.setVisibility(View.GONE);
                    if (layoutEmptyState != null) layoutEmptyState.setVisibility(View.VISIBLE);
                });
    }

    private void setupDrawerItemClicks() {
        if (navigationView == null) return;

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
            if (drawerLayout != null) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
            return true;
        });
    }

    private void setupBottomNavClicks() {
        if (bottomNavigationView == null) return;

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            Intent intent = null;

            if (id == R.id.nav_home) {
                // Already on home
                return true;
            } else if (id == R.id.nav_contacts) {
                intent = new Intent(MainActivity.this, Screen12Activity.class);
            } else if (id == R.id.nav_tasks) {
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
}