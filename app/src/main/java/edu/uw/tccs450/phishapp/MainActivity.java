package edu.uw.tccs450.phishapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import edu.uw.tccs450.phishapp.model.Credentials;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public static  boolean checkEmail(EditText email) {
        String emailString = email.getText().toString();
        if (emailString.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(emailString).matches()) {
            email.setError("Not a valid email");
            return false;
        }
        return true;
    }
    public static boolean checkPassword(EditText password) {
        String passwordValue = password.getText().toString();
        if (passwordValue.length() < 6) {
            password.setError("Password must be longer than 5 characters");
            return false;
        }
        return true;
    }


}
