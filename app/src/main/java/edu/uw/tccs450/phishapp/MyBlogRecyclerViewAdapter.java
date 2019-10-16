package edu.uw.tccs450.phishapp;

import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import edu.uw.tccs450.phishapp.BlogFragment.OnListFragmentInteractionListener;
import edu.uw.tccs450.phishapp.blog.BlogPost;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyBlogRecyclerViewAdapter extends RecyclerView.Adapter<MyBlogRecyclerViewAdapter.ViewHolder> {

    private final List<BlogPost> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyBlogRecyclerViewAdapter(List<BlogPost> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_blog, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mBlogPost = mValues.get(position);
        holder.mPublishDateView.setText(mValues.get(position).getPubDate());
        holder.mTitleView.setText(mValues.get(position).getTitle());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.mTeaserView.setText(Html.fromHtml(mValues.get(position).getTeaser(), Html.FROM_HTML_MODE_LEGACY));
        } else
            holder.mTeaserView.setText(Html.fromHtml(mValues.get(position).getTeaser()));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mBlogPost);
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
        public final TextView mPublishDateView;
        public final TextView mTitleView;
        public final TextView mTeaserView;
        public BlogPost mBlogPost;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mPublishDateView = (TextView) view.findViewById(R.id.publish_date);
            mTitleView = (TextView) view.findViewById(R.id.title);
            mTeaserView = (TextView) view.findViewById(R.id.teaser);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }
}
