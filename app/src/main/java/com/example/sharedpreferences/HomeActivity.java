package com.example.sharedpreferences;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

/**
 * Home Activity to fetch data from shared preference and encrypted shared preference
 */
public class HomeActivity extends AppCompatActivity {

    // Declare the TextViews
    private TextView sharedPreferencesTextView;
    private TextView encryptedSharedPreferencesTextView;
    // Declare the SharedPreferences
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize the TextViews
        sharedPreferencesTextView = findViewById(R.id.type1_text_id);
        encryptedSharedPreferencesTextView = findViewById(R.id.type2_text_id);

    }

    /**
     * method to fetch data from shared preference
     *
     * @param view button view of fetch data from shared preference
     */
    public void fetchFromSharedPreference(View view) {

        SharedPreferences sharedPreferences = getSharedPreferences("UserDetailsPreference", MODE_PRIVATE);
        //the value will be default as empty string because for vary
        String username = sharedPreferences.getString("username", "");
        String password = sharedPreferences.getString("password", "");
        sharedPreferencesTextView.setText("Shared Preference Data:\n" + "Username:" + username + " Password:" + password);

        Log.d("UserDetails", "Username:" + username + " Password:" + password);

    }

    /**
     * method to fetch data from encrypted shared preference
     *
     * @param view button view of fetch data from encrypted shared preference
     */
    public void fetchFromEncryptedSharedPreference(View view) {
        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            sharedPreferences = EncryptedSharedPreferences.create(
                    "UserSession",
                    masterKeyAlias,
                    getApplicationContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        String username2 = sharedPreferences.getString("username2", "");
        String password2 = sharedPreferences.getString("password2", "");

        encryptedSharedPreferencesTextView.setText("Encrypted Shared Preference Data:\n" + "Username:" + username2 + " Password:" + password2);

        Log.d("UserDetailsEncrypted", "Username:" + username2 + " Password:" + password2);

    }

    /**
     * method to remove data from shared preference when back pressed
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //remove data from shared preference
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetailsPreference", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}