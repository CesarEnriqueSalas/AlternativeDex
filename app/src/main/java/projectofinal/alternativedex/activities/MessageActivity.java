package projectofinal.alternativedex.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import projectofinal.alternativedex.R;
import projectofinal.alternativedex.fragments.MessageFragment;
import projectofinal.alternativedex.models.User;
import projectofinal.alternativedex.utilities.Constants;

public class MessageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        if (savedInstanceState == null) {
            User user = (User) getIntent().getSerializableExtra(Constants.KEY_USUARIO);
            MessageFragment messageFragment = MessageFragment.newInstance(user);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, messageFragment)
                    .commit();
        }
    }
}