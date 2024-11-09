package com.example.sharedpreferences;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

/**
 * main activity class for login page
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    // UI elements for login page
    private EditText username;
    private EditText password;
    private Button login;
    private SharedPreferences sharedPreferences;
    public static String getTAG() {
        return TAG;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Initialize UI elements
        setUsername(findViewById(R.id.username_id));
        setPassword(findViewById(R.id.password_id));
        setLogin(findViewById(R.id.login_id));

        // Set click listener for login button
        getLogin().setOnClickListener(v -> {

            // Get username and password from UI elements
            String usernameText = getUsername().getText().toString();
            String passwordText = getPassword().getText().toString();

            // Check if username and password are empty
            if (usernameText.isEmpty() || passwordText.isEmpty()) {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
            } else {
                // Check if username and password are correct
                if (usernameText.equals("admin") && passwordText.equals("123")) {
                    // Show login successful message
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();

                    //clear the username and password fields
                    getUsername().setText("");
                    getPassword().setText("");

                    //1. Save user data to shared preferences
                    saveUserData(usernameText, passwordText);

                    //2.save user data to Encrypted shared preferences
                    saveUserDataEncrypted(usernameText, passwordText);

                    //navigate to another activity
                    Intent intent = new Intent(this, HomeActivity.class);
                    startActivity(intent);


                } else {
                    // Show login failed message
                    Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Saves user data to shared preferences
     *
     * @param usernameText user name
     * @param passwordText user password
     */
    private void saveUserData(String usernameText, String passwordText) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetailsPreference", MODE_PRIVATE);

        //creating an editor object to edit the shared preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", usernameText);
        editor.putString("password", passwordText);
        editor.apply();
        editor.commit();
    }

    /**
     * Saves user data to encrypted shared preferences
     *
     * @param usernameText user name
     * @param passwordText user password
     */
    private void saveUserDataEncrypted(String usernameText, String passwordText) {

        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            sharedPreferences = EncryptedSharedPreferences.create(
                    "UserSession",
                    masterKeyAlias,
                    getApplicationContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
           );
        }
        catch (Exception e) {
           e.printStackTrace();
        }

        sharedPreferences.edit().putString("username2", usernameText).apply();
        sharedPreferences.edit().putString("password2", passwordText).apply();

    }

    public EditText getUsername() {
        return username;
    }

    public void setUsername(EditText username) {
        this.username = username;
    }

    public EditText getPassword() {
        return password;
    }

    public void setPassword(EditText password) {
        this.password = password;
    }

    public Button getLogin() {
        return login;
    }

    public void setLogin(Button login) {
        this.login = login;
    }
}