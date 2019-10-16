package edu.uw.tccs450.phishapp;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import edu.uw.tccs450.phishapp.model.Credentials;

public class RegisterFragment extends Fragment  {

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        Button b = view.findViewById(R.id.button_register_register);
        b.setOnClickListener(butt -> onRegisterClicked());

    }


    public void onRegisterClicked() {
        View v = getView();
        EditText email = (EditText) v.findViewById(R.id.field_register_email);
        EditText pw = (EditText) v.findViewById(R.id.field_register_pw);
        EditText confirmPw = (EditText) v.findViewById(R.id.field_register_confirmpw);
        if (!MainActivity.validateForms(email, pw, confirmPw)) {
            return;
        }
        NavController nc = Navigation.findNavController(v);

//        if (nc.getCurrentDestination().getId() != R.id.loginFragment) {
//            nc.navigateUp();
//        }
        Credentials.Builder credBuilder = new Credentials.Builder(email.getText().toString(),
                pw.getText().toString());
        Bundle bun = new Bundle();
        bun.putSerializable(getString(R.string.credentials_key), credBuilder.build());
        nc.navigate(R.id.action_registerFragment_to_loginFragment, bun);

    }
}
