package edu.uw.tccs450.phishapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.util.Patterns;
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

public class RegisterFragment extends Fragment  {
    private Credentials mCredentials;
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
        EditText nickName = (EditText) v.findViewById(R.id.field_register_nickname);
        EditText firstName = (EditText) v.findViewById(R.id.field_register_firstname);
        EditText lastName = (EditText) v.findViewById(R.id.field_register_lastname);
        if (MainActivity.checkEmail(email) &&
                confirmPassword(pw, confirmPw) &&
                checkNameFields(firstName, lastName, nickName)) {
            Uri uri = new Uri.Builder().scheme("https").
                    appendPath(getString(R.string.ep_base_url)).
                    appendPath(getString(R.string.ep_register)).build();
            Credentials credentials = new Credentials.Builder
                    (email.getText().toString(), pw.getText().toString()).
                    addFirstName(firstName.getText().toString()).
                    addLastName(lastName.getText().toString()).
                    addUsername(nickName.getText().toString()).build();
            mCredentials = credentials;
            JSONObject toSend = credentials.asJSONObject();
            new SendPostAsyncTask.Builder(uri.toString(), toSend)
                    .onPreExecute(this::handleRegisterOnPre)
                    .onPostExecute(this::handleRegisterOnPost)
                    .onCancelled(this::handleErrorsInTask).build().execute();
        } else {
            Log.d("register not branch", "oop");
        }
//            NavController nc = Navigation.findNavController(getView());
//            Credentials.Builder credBuilder = new Credentials.Builder(email.getText().toString(),
//                    pw.getText().toString());
//            Bundle bun = new Bundle();
//            bun.putSerializable(getString(R.string.credentials_key), credBuilder.build());
//            nc.navigate(R.id.action_registerFragment_to_loginFragment, bun);

    }
    private boolean confirmPassword(EditText password, EditText confirmPassword) {
        boolean flag = MainActivity.checkPassword(password);

        if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
            confirmPassword.setError("Passwords must match");
            flag = false;
        }
        return flag;
    }
    private boolean checkNameFields(EditText firstName, EditText lastName, EditText nickName) {
        boolean flag = true;
        if (firstName.getText().toString().isEmpty()) {
            firstName.setError(getString(R.string.field_empty));
            flag =false;
        }
        if (lastName.getText().toString().isEmpty()) {
            lastName.setError(getString(R.string.field_empty));
            flag = false;
        }
        if (nickName.getText().toString().isEmpty()) {
            nickName.setError(getString(R.string.field_empty));
            flag = false;
        }
        return flag;
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
    private void handleRegisterOnPre() {
        getActivity().findViewById(R.id.layout_register_wait).setVisibility(View.VISIBLE);
    }
    /**
     * Handle onPostExecute of the AsynceTask. The result from our webservice is
     * a JSON formatted String. Parse it for success or failure.
     * @param result the JSON formatted String response from the web service
     */
    private void handleRegisterOnPost(String result) {
        try {
            JSONObject resultsJSON = new JSONObject(result);
            boolean success =
                    resultsJSON.getBoolean(
                            getString(R.string.keys_json_login_success));
            Log.d("results", resultsJSON.toString());
            if (success) {
                RegisterFragmentDirections
                        .ActionRegisterFragmentToHomeActivity homeActivity =
                        RegisterFragmentDirections.actionRegisterFragmentToHomeActivity(mCredentials);
                homeActivity.setJwt(
                        resultsJSON.getString(
                                getString(R.string.keys_json_login_jwt)));
                Navigation.findNavController(getView())
                        .navigate(homeActivity);
                return;
            } else {
                //Registration was unsuccessful. Don’t switch fragments and
                // inform the user
                Log.d("no success branch", "oop");
                ((TextView) getView().findViewById(R.id.field_register_email))
                        .setError("Register Unsuccessful");
            }
            getActivity().findViewById(R.id.layout_login_wait)
                    .setVisibility(View.GONE);
        } catch (JSONException e) {
            //It appears that the web service did not return a JSON formatted
            //String or it did not have what we expected in it.
            Log.e("JSON_PARSE_ERROR", result
                    + System.lineSeparator()
                    + e.getMessage());
            getActivity().findViewById(R.id.layout_register_wait)
                    .setVisibility(View.GONE);
            ((TextView) getView().findViewById(R.id.field_register_email))
                    .setError("Register Unsuccessful");
        }
    }
}
