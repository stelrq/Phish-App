package edu.uw.tccs450.phishapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import edu.uw.tccs450.phishapp.R;
import edu.uw.tccs450.phishapp.model.Credentials;

public class SuccessFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_success, container, false);
        final TextView textView = root.findViewById(R.id.text_success_email);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
//        Credentials cred = (Credentials) getArguments().getSerializable(getString(R.string.credentials_key));
//        TextView tv = root.findViewById(R.id.text_success_email);
//        tv.setText(cred.getEmail());
        return root;
    }
//    public void onStart() {
//        super.onStart();
//        View view = getView();
//    }
}