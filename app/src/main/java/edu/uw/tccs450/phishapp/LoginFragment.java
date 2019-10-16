package edu.uw.tccs450.phishapp;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import edu.uw.tccs450.phishapp.model.Credentials;


public class LoginFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    public LoginFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        Button b = view.findViewById(R.id.button_login_register);
        b.setOnClickListener(butt -> onRegisterClicked());
        b = view.findViewById((R.id.button_login_login));
        b.setOnClickListener(butt -> onLoginClicked());

    }
    @Override
    public void onStart() {
        super.onStart();
        //hardcoded login info for testing
        updateContent("Sterling@Sterling.stir", "faaked");
        if(getArguments() != null) {
            Credentials cred = (Credentials) getArguments().
                    getSerializable(getString(R.string.credentials_key));
            updateContent(cred.getEmail(), cred.getPassword());
        }
    }
    public void onLoginClicked() {
        EditText fieldEmail = getActivity().findViewById(R.id.field_login_email);
        EditText fieldPw = getActivity().findViewById(R.id.field_login_pw);
        if(MainActivity.validateForms(fieldEmail, fieldPw, null)) {
            Credentials.Builder builder = new Credentials.Builder(fieldEmail.getText().toString(), fieldPw.getText().toString());
            onLoginSuccess(builder.build(), null);
        }

    }
    public void onLoginSuccess(Credentials cred, String jwt) {
//        NavController nc = Navigation.findNavController(getView());
//        Bundle bun = new Bundle();
//        bun.putSerializable(getString(R.string.credentials_key), cred);
//        nc.navigate(R.id.nav_home, bun);
        mListener.onFragmentInteraction(cred);
    }

    public void onRegisterClicked() {
        NavController nc = Navigation.findNavController(getView());
        nc.navigate(R.id.action_loginFragment_to_registerFragment);
    }
    public void updateContent(String email, String pw) {
        EditText fieldEmail = getActivity().findViewById(R.id.field_login_email);
        EditText fieldPw = getActivity().findViewById(R.id.field_login_pw);
        fieldEmail.setText(email);
        fieldPw.setText(pw);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Credentials cred);
    }

}
