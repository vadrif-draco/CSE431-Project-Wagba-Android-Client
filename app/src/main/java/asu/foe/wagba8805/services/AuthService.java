package asu.foe.wagba8805.services;

import static asu.foe.wagba8805.Constants.FACULTY_AZURE_TENANT_ID;
import static asu.foe.wagba8805.Constants.TAG;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.OAuthCredential;
import com.google.firebase.auth.OAuthProvider;

import java.util.Arrays;
import java.util.Objects;

import asu.foe.wagba8805.MainActivity;
import asu.foe.wagba8805.activities.FacultyEmailLoginActivity;
import asu.foe.wagba8805.activities.GmailLoginActivity;
import asu.foe.wagba8805.interfaces.AuthResponsiveActivity;

public class AuthService {

  public static String accessToken = "";
  public static String idToken = "";
  public static String provider = "";

  // User signed in successfully.
  private static void respondToSuccess(AuthResult authResult, AuthResponsiveActivity activity) {
    accessToken = ((OAuthCredential) Objects.requireNonNull(authResult.getCredential())).getAccessToken();
    idToken = ((OAuthCredential) authResult.getCredential()).getIdToken();
    provider = authResult.getCredential().getProvider();
    SharedPreferences.Editor sharedPrefsEditor = MainActivity.sharedPrefs.edit();
    sharedPrefsEditor.putString("accessToken", accessToken);
    sharedPrefsEditor.putString("idToken", idToken);
    sharedPrefsEditor.putString("provider", provider);
    sharedPrefsEditor.apply();
    activity.respondToAuth(true);
  }

  // User failed to sign in.
  private static void respondToFailure(Exception e, AuthResponsiveActivity activity) {
    Log.e(TAG, "Exception in authentication:\n" + e);
    activity.respondToAuth(false);
  }

  public static void login(AuthResponsiveActivity activity) {

    OAuthProvider.Builder provider;

    if (activity instanceof FacultyEmailLoginActivity) {

      provider = OAuthProvider.newBuilder("microsoft.com")
          .addCustomParameter("prompt", "consent")
          .addCustomParameter("tenant", FACULTY_AZURE_TENANT_ID);
      // .addCustomParameter("login_hint", "id@eng.asu.edu.eg");

    } else if (activity instanceof GmailLoginActivity) {

      provider = OAuthProvider.newBuilder("google.com")
          .setScopes(Arrays.asList("profile", "email", "openid"));

    } else {

      provider = OAuthProvider.newBuilder("");

    }

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    Task<AuthResult> pendingResultTask = firebaseAuth.getPendingAuthResult();

    if (pendingResultTask != null) { // There's something already here! Finish the sign-in for your user.

      pendingResultTask
          .addOnFailureListener(e -> respondToFailure(e, activity))
          .addOnSuccessListener(authResult -> respondToSuccess(authResult, activity));

    } else { // There's no pending result so you need to start the sign-in flow.

      firebaseAuth
          .startActivityForSignInWithProvider((Activity) activity, provider.build())
          .addOnFailureListener(e -> respondToFailure(e, activity))
          .addOnSuccessListener(authResult -> respondToSuccess(authResult, activity));

    }
  }

  public static void signOut() {
    accessToken = "";
    idToken = "";
    provider = "";
    SharedPreferences.Editor sharedPrefsEditor = MainActivity.sharedPrefs.edit();
    sharedPrefsEditor.putString("accessToken", accessToken);
    sharedPrefsEditor.putString("idToken", idToken);
    sharedPrefsEditor.putString("provider", provider);
    sharedPrefsEditor.apply();
    FirebaseAuth.getInstance().signOut();
  }
}
