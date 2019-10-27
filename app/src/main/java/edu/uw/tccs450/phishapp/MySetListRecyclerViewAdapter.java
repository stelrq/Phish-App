package edu.uw.tccs450.phishapp;

import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.uw.tccs450.phishapp.SetFragment.OnListFragmentInteractionListener;
import edu.uw.tccs450.phishapp.setlist.SetList;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link edu.uw.tccs450.phishapp.setlist.SetList} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MySetListRecyclerViewAdapter extends RecyclerView.Adapter<MySetListRecyclerViewAdapter.ViewHolder> {

    private final List<SetList> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MySetListRecyclerViewAdapter(List<SetList> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_set, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            ((TextView) view.findViewById(R.id.text_post_teaser)).
//                    setText(Html.fromHtml(blogPost.getTeaser(), Html.FROM_HTML_MODE_LEGACY));
//        } else
//            ((TextView) view.findViewById(R.id.text_post_teaser)).
//                    setText(Html.fromHtml(blogPost.getTeaser()));
        holder.mDateView.setText(mValues.get(position).mShowDate);
        holder.mLocationView.setText(mValues.get(position).mLocation);
        holder.mVenueView.setText(HomeActivity.parseHTML(mValues.get(position).mVenue));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mDateView;
        public final TextView mLocationView;
        public final TextView mVenueView;
        public SetList mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mDateView = (TextView) view.findViewById(R.id.text_setfragment_date);
            mLocationView = (TextView) view.findViewById(R.id.text_setfragment_location);
            mVenueView = (TextView) view.findViewById(R.id.text_setfragment_venue);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mLocationView.getText() + "'";
        }
    }
}
