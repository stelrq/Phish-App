package edu.uw.tccs450.phishapp.setlist;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 summarizes a setlist for the band phish.
 */
public class SetList implements Serializable, Parcelable {

    public final String mShowDate;
    public final String mLocation;
    public final String mVenue;
    public final String mData;
    public final String mNotes;
    public final String mUrl;

    protected SetList(Parcel in) {
        mShowDate = in.readString();
        mLocation = in.readString();
        mVenue = in.readString();
        mData = in.readString();
        mNotes = in.readString();
        mUrl = in.readString();
    }
    public static final Creator<SetList> CREATOR = new Creator<SetList>() {
        @Override
        public SetList createFromParcel(Parcel in) {
            return new SetList(in);
        }

        @Override
        public SetList[] newArray(int size) {
            return new SetList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mShowDate);
        dest.writeString(mLocation);
        dest.writeString(mVenue);
        dest.writeString(mData);
        dest.writeString(mNotes);
        dest.writeString(mUrl);
    }

    /**
     * Helper class for building Credentials.
     *
     * @author Charles Bryan
     */
    public static class Builder {
        private String mShowDate;
        private String mLocation;
        private String mVenue;
        private String mData;
        private String mNotes;
        private String mUrl;


        /**
         * Constructs a new Builder.
         *
         * @param showDate the date of the show
         * @param location location of the show
         * @param venue the venue
         */
        public Builder(String showDate, String location, String venue) {
            this.mShowDate = showDate;
            this.mLocation = location;
            this.mVenue = venue;
        }

        public Builder addUrl(final String val) {
            mUrl = val;
            return this;
        }
        //add the actual setlist
        public Builder addData(final String val) {
            mData = val;
            return this;
        }

        public Builder addNotes(final String val) {
            mNotes = val;
            return this;
        }
        public SetList build() {
            return new SetList(this);
        }

    }

    private SetList(final Builder builder) {
        this.mShowDate = builder.mShowDate;
        this.mLocation = builder.mLocation;
        this.mVenue = builder.mVenue;
        this.mUrl = builder.mUrl;
        this.mData = builder.mData;
        this.mNotes = builder.mNotes;
    }


}
