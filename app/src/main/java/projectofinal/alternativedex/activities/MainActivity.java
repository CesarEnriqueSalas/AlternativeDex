package projectofinal.alternativedex.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;

import projectofinal.alternativedex.fragments.ChatFragment;
import projectofinal.alternativedex.fragments.SingInFragment;
import projectofinal.alternativedex.fragments.HomeFragment;
import projectofinal.alternativedex.R;
import projectofinal.alternativedex.fragments.TournamentFragment;
import projectofinal.alternativedex.utilities.PreferenceManager;
import projectofinal.alternativedex.utilities.Constants;

public class MainActivity extends AppCompatActivity {

    private FrameLayout frameLayout;
    private BottomNavigationView bottomNavigationView;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferenceManager = new PreferenceManager(getApplicationContext());
        bottomNavigationView = findViewById(R.id.menuInferior);
        frameLayout = findViewById(R.id.frame);
        FirebaseApp.initializeApp(this);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.home) {
                    loadFragment(new HomeFragment(), false);

                } else if (itemId == R.id.tournaments) {
                    loadFragment(new TournamentFragment(), false);

                } else if (itemId == R.id.chat) {
                    if (isUserSignedIn()) {
                        loadFragment(new ChatFragment(), false);
                    } else {
                        loadFragment(new SingInFragment(), false);
                    }
                }

                return true;
            }
        });
        loadFragment(new HomeFragment(), false);
    }

    private boolean isUserSignedIn() {
        return preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN);
    }

    public void loadFragment(Fragment fragment, boolean isAppInitialized) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (isAppInitialized) {
            fragmentTransaction.add(R.id.frame, fragment);
        } else {
            fragmentTransaction.replace(R.id.frame, fragment);
        }
        fragmentTransaction.commit();
    }
}
