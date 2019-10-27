package edu.uw.tccs450.phishapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tccs450.phishapp.model.Credentials;
import edu.uw.tccs450.phishapp.utils.SendPostAsyncTask;


public class LoginFragment extends Fragment {

    private Credentials mCredentials;

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
        updateContent("sterling@sterling.com", "sterling");
        if(getArguments() != null) {
            Credentials cred = (Credentials) getArguments().
                    getSerializable(getString(R.string.credentials_key));
            updateContent(cred.getEmail(), cred.getPassword());
        }
    }
    public void onLoginClicked() {
        EditText fieldEmail = getActivity().findViewById(R.id.field_login_email);
        EditText fieldPw = getActivity().findViewById(R.id.field_login_pw);
        if(MainActivity.checkEmail(fieldEmail) && !fieldPw.getText().toString().isEmpty()) {
            Credentials credentials = new Credentials.Builder(
                    fieldEmail.getText().toString(),
                    fieldPw.getText().toString())
                    .build();
            //build the web service URL
            Uri uri = new Uri.Builder()
                    .scheme("https")
                    .appendPath(getString(R.string.ep_base_url))
                    .appendPath(getString(R.string.ep_login))
                    .build();
            //build the JSONObject
            JSONObject msg = credentials.asJSONObject();
            mCredentials = credentials;
            //instantiate and execute the AsyncTask.
            new SendPostAsyncTask.Builder(uri.toString(), msg)
                    .onPreExecute(this::handleLoginOnPre)
                    .onPostExecute(this::handleLoginOnPost)
                    .onCancelled(this::handleErrorsInTask)
                    .build().execute();
        }

    }
    public void onLoginSuccess(Credentials cred, String jwt) {
        LoginFragmentDirections.ActionLoginFragmentToHomeActivity homeActivity =
                LoginFragmentDirections.actionLoginFragmentToHomeActivity(cred);
        homeActivity.setJwt("Will get a token from the WS later");
        Navigation.findNavController(getView()).navigate(homeActivity);

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
    /**
     * handle errors in Async task.
     * @param result the provided error message
     */
    private void handleErrorsInTask(String result) {
        Log.e("ASYNC_TASK_ERROR", result);
    }
    /**
     * Handle the setup of the UI before the HTTP call to the webservice.
     */
    private void handleLoginOnPre() {
        getActivity().findViewById(R.id.layout_login_wait).setVisibility(View.VISIBLE);
    }
    /**
     * Handle onPostExecute of the AsynceTask. The result from our webservice is
     * a JSON formatted String. Parse it for success or failure.
     * @param result the JSON formatted String response from the web service
     */
    private void handleLoginOnPost(String result) {
        try {
            JSONObject resultsJSON = new JSONObject(result);
            boolean success =
                    resultsJSON.getBoolean(
                            getString(R.string.keys_json_login_success));
            Log.d("results", resultsJSON.toString());
            if (success) {
                LoginFragmentDirections
                        .ActionLoginFragmentToHomeActivity homeActivity =
                        LoginFragmentDirections
                                .actionLoginFragmentToHomeActivity(mCredentials);
                homeActivity.setJwt(
                        resultsJSON.getString(
                                getString(R.string.keys_json_login_jwt)));
                Navigation.findNavController(getView())
                        .navigate(homeActivity);
                return;
            } else {
                //Login was unsuccessful. Don’t switch fragments and
                // inform the user
                Log.d("no success branch", "oop");
                ((TextView) getView().findViewById(R.id.field_login_email))
                        .setError("Login Unsuccessful");
            }
            getActivity().findViewById(R.id.layout_login_wait)
                    .setVisibility(View.GONE);
        } catch (JSONException e) {
            //It appears that the web service did not return a JSON formatted
            //String or it did not have what we expected in it.
            Log.e("JSON_PARSE_ERROR", result
                    + System.lineSeparator()
                    + e.getMessage());
            getActivity().findViewById(R.id.layout_login_wait)
                    .setVisibility(View.GONE);
            ((TextView) getView().findViewById(R.id.field_login_email))
                    .setError("Login Unsuccessful");
        }
    }
}
