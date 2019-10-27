package edu.uw.tccs450.phishapp;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import edu.uw.tccs450.phishapp.setlist.SetList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SetListFragment extends Fragment {

    private SetList mSetList;

    public SetListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setlist, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        if (args != null) {
            mSetList = (SetList) args.getSerializable(getString(R.string.keys_setlist));
        }
        View v = getView();
        ((TextView)v.findViewById(R.id.text_setlist_date)).setText(mSetList.mShowDate);
        ((TextView)v.findViewById(R.id.text_setlist_location))
                .setText(HomeActivity.parseHTML(mSetList.mLocation));
        ((TextView)v.findViewById(R.id.text_setlist_data))
                .setText(HomeActivity.parseHTML(mSetList.mData));
        ((TextView)v.findViewById(R.id.text_setlist_notes))
                .setText(HomeActivity.parseHTML(mSetList.mNotes));
        ((Button)v.findViewById(R.id.button_setlist_url))
                .setOnClickListener(butt -> fullSetListClicked(mSetList.mUrl));

    }
    
    private void fullSetListClicked(String uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);
    }
}