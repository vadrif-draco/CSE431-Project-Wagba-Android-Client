package asu.foe.wagba8805.services;

import static asu.foe.wagba8805.Constants.TAG;

import android.net.Uri;
import android.util.Log;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.google.firebase.auth.UserInfo;

public class ProfileService {

  public static Uri getPhotoUri(UserInfo userInfo) {
    return userInfo.getPhotoUrl();
  }

  public static GlideUrl getGlideUrl(UserInfo userInfo) {
    String provider = userInfo.getProviderId();
    Log.d(TAG, provider);
    switch (provider) {
      case "google.com":
        return new GlideUrl(String.valueOf(getPhotoUri(userInfo)).split("=")[0]);

      case "microsoft.com":
        return new GlideUrl(
            "https://graph.microsoft.com/v1.0/users/" + userInfo.getUid() + "/photo/$value",
            new LazyHeaders.Builder().addHeader("Authorization", AuthService.accessToken).build());

      default:
        return null;
    }
  }

  public static String getDisplayName(UserInfo userInfo) {
    return userInfo.getDisplayName();
  }

}
