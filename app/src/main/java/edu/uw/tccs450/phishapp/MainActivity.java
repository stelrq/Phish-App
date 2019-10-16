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

public class MainActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public static  boolean checkEmail(String email) {
        return !email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public static boolean validateForms(EditText email, EditText password, EditText confirmPassword) {
        boolean flag = true;
        if (!checkEmail(email.getText().toString())) {
            email.setError("Not a valid email");
            flag = false;
        }
        String passwordValue = password.getText().toString();
        if (passwordValue.length() < 6) {
            password.setError("Password must be longer than 5 characters");
            flag = false;
        } else if (confirmPassword != null && !passwordValue.equals(confirmPassword.getText().toString())) {
            confirmPassword.setError("Passwords must match");
            flag = false;
        }
        return flag;
    }


    @Override
    public void onFragmentInteraction(Credentials cred) {
        Intent intent = new Intent(this, HomeActivity.class);
        Bundle bund = new Bundle();
        bund.putSerializable(getString(R.string.credentials_key), cred);
        intent.putExtra("credentials bundle", bund);
        startActivity(intent);
    }
}
