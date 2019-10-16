package edu.uw.tccs450.phishapp;


import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import edu.uw.tccs450.phishapp.blog.BlogPost;


/**
 * A simple {@link Fragment} subclass.
 */
public class BlogPostFragment extends Fragment {


    public BlogPostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();;
        Bundle args = getArguments();
        if(args != null) {
            BlogPost blogPost = (BlogPost) args.getSerializable("blog post key");
            updateContent(blogPost);
        }


    }
    public void updateContent(BlogPost blogPost) {
        View view = getView();
        ((TextView) view.findViewById(R.id.text_post_publish_date)).setText(blogPost.getPubDate());
        ((TextView) view.findViewById(R.id.text_post_title)).setText(blogPost.getTitle());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ((TextView) view.findViewById(R.id.text_post_teaser)).
                    setText(Html.fromHtml(blogPost.getTeaser(), Html.FROM_HTML_MODE_LEGACY));
        } else
            ((TextView) view.findViewById(R.id.text_post_teaser)).
                    setText(Html.fromHtml(blogPost.getTeaser()));
        ((Button) view.findViewById(R.id.button_post_full)).
                setOnClickListener(butt -> fullPostClicked(blogPost.getUrl()));
    }
    public void fullPostClicked(String uriString) {
        Uri uri = Uri.parse(uriString); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blog_post, container, false);
    }

}
