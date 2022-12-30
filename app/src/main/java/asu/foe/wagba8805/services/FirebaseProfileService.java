package asu.foe.wagba8805.services;

import static asu.foe.wagba8805.Constants.TAG;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.Nullable;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

public class FirebaseProfileService {

  @Nullable
  private static UserInfo getUserInfo() {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    // 1 stands for the actual provider after firebase, in our case google.com and microsoft.com
    // 0 stands for firebase itself
    return (user == null) ? null : user.getProviderData().get((user.getProviderData().size() > 0) ? 1 : 0);
  }

  @Nullable
  public static String getUuid() {
    if (FirebaseAuthService.admin) return "1";
    UserInfo userInfo = getUserInfo();
    return (userInfo == null) ? null : userInfo.getUid();
  }

  @Nullable
  public static String getEmail() {
    UserInfo userInfo = getUserInfo();
    return (userInfo == null) ? null : userInfo.getEmail();
  }

  @Nullable
  private static Uri getPhotoUri() {
    UserInfo userInfo = getUserInfo();
    return (userInfo == null) ? null : userInfo.getPhotoUrl();
  }

  @Nullable
  public static GlideUrl getGlideUrl() {
    UserInfo userInfo = getUserInfo();
    if (userInfo == null) return null;
    String provider = userInfo.getProviderId();
    Log.d(TAG, provider);
    switch (provider) {
      case "google.com":
        return new GlideUrl(String.valueOf(getPhotoUri()).split("=")[0]);

      case "microsoft.com":
        return new GlideUrl(
            "https://graph.microsoft.com/v1.0/users/" + userInfo.getUid() + "/photo/$value",
            new LazyHeaders.Builder().addHeader("Authorization", FirebaseAuthService.getCurrentAccessToken()).build());

      case "password": // Password-less sign-in method
      default:
        return null;
    }
  }

  @Nullable
  public static String getDisplayName() {
    UserInfo userInfo = getUserInfo();
    return (userInfo == null) ? null : userInfo.getDisplayName();
  }

}
