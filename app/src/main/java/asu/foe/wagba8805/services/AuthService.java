package asu.foe.wagba8805.services;

import static asu.foe.wagba8805.Constants.TAG;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.OAuthCredential;
import com.google.firebase.auth.OAuthProvider;

import asu.foe.wagba8805.MainActivity;
import asu.foe.wagba8805.interfaces.AuthResponsiveActivity;

public class AuthService {

  public static String accessToken = "";
  public static String idToken = "";

  // User signed in successfully.
  private static void respondToSuccess(AuthResult authResult, AuthResponsiveActivity activity) {
    authResult.getAdditionalUserInfo().getProfile(); // IdP data
    accessToken = ((OAuthCredential) authResult.getCredential()).getAccessToken(); // OAuth access token
    idToken = ((OAuthCredential) authResult.getCredential()).getIdToken(); // OAuth ID token
    SharedPreferences.Editor sharedPrefsEditor = MainActivity.sharedPrefs.edit();
    sharedPrefsEditor.putString("accessToken", accessToken);
    sharedPrefsEditor.putString("idToken", idToken);
    sharedPrefsEditor.apply();
    activity.respondToAuth(true);
  }

  // User failed to sign in.
  private static void respondToFailure(Exception e, AuthResponsiveActivity activity) {
    Log.e(TAG, "Exception in authentication:\n" + e);
    activity.respondToAuth(false);
  }

  public static void login(AuthResponsiveActivity activity, OAuthProvider.Builder provider) {

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
    FirebaseAuth.getInstance().signOut();
  }
}
