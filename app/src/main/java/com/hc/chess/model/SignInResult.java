package com.hc.chess.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class SignInResult implements Parcelable {
    private final String userName;
    private final Integer error;

    public SignInResult(Integer error) {
        this.userName = null;
        this.error = error;
    }

    public SignInResult(String userName) {
        this.error = null;
        this.userName = userName;
    }

    protected SignInResult(Parcel in) {
        userName = in.readString();
        if (in.readByte() == 0) {
            error = null;
        } else {
            error = in.readInt();
        }
    }

    public static final Creator<SignInResult> CREATOR = new Creator<SignInResult>() {
        @Override
        public SignInResult createFromParcel(Parcel in) {
            return new SignInResult(in);
        }

        @Override
        public SignInResult[] newArray(int size) {
            return new SignInResult[size];
        }
    };

    public boolean isSuccess() { return this.userName != null; }

    public String getUserName() {
        return userName;
    }

    public Integer getError() {
        return error;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(userName);
        if (error == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(error);
        }
    }
}
