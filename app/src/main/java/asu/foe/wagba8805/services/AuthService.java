package asu.foe.wagba8805.services;

import static asu.foe.wagba8805.Constants.FACULTY_AZURE_TENANT_ID;
import static asu.foe.wagba8805.Constants.TAG;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.OAuthCredential;
import com.google.firebase.auth.OAuthProvider;

import java.util.Arrays;
import java.util.Objects;

import asu.foe.wagba8805.MainActivity;
import asu.foe.wagba8805.activities.FacultyEmailLoginActivity;
import asu.foe.wagba8805.activities.GmailLoginActivity;
import asu.foe.wagba8805.activities.PersonalEmailLoginActivity;
import asu.foe.wagba8805.interfaces.AuthResponsiveActivity;

public class AuthService {

  public static String accessToken = "";
  public static String idToken = "";
  public static String provider = "";
  public static String error = "undefined error";

  // User signed in successfully.
  private static void respondToSuccess(@Nullable AuthResult authResult, AuthResponsiveActivity activity) {
    if (authResult != null) {
      accessToken = ((OAuthCredential) Objects.requireNonNull(authResult.getCredential())).getAccessToken();
      idToken = ((OAuthCredential) authResult.getCredential()).getIdToken();
      provider = authResult.getCredential().getProvider();
      SharedPreferences.Editor sharedPrefsEditor = MainActivity.sharedPrefs.edit();
      sharedPrefsEditor.putString("accessToken", accessToken);
      sharedPrefsEditor.putString("idToken", idToken);
      sharedPrefsEditor.putString("provider", provider);
      sharedPrefsEditor.apply();
    }
    activity.respondToAuth(true);
  }

  // User failed to sign in.
  private static void respondToFailure(@Nullable Exception e, AuthResponsiveActivity activity) {
    String err = (e != null) ? e.getMessage() : null;
    error = (err != null) ? err.toLowerCase() : "undefined error";
    Log.e(TAG, "Exception in authentication:\n" + e);
    activity.respondToAuth(false);
  }

  private static void handlePendingAuth(OAuthProvider.Builder provider, AuthResponsiveActivity activity) {

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

  public static void login(AuthResponsiveActivity activity) {

    if (activity instanceof FacultyEmailLoginActivity) {

      OAuthProvider.Builder provider = OAuthProvider.newBuilder("microsoft.com")
          .addCustomParameter("prompt", "consent")
          .addCustomParameter("tenant", FACULTY_AZURE_TENANT_ID);
      // .addCustomParameter("login_hint", "id@eng.asu.edu.eg");

      handlePendingAuth(provider, activity);

    } else if (activity instanceof GmailLoginActivity) {

      OAuthProvider.Builder provider = OAuthProvider.newBuilder("google.com")
          .setScopes(Arrays.asList("profile", "email", "openid"));

      handlePendingAuth(provider, activity);

    } else if (activity instanceof PersonalEmailLoginActivity) {

      String email = String.valueOf(((PersonalEmailLoginActivity) activity).pelBinding.emailInput.getText());

      ActionCodeSettings actionCodeSettings =
          ActionCodeSettings.newBuilder()
              .setAndroidPackageName("asu.foe.wagba8805", false, null)
              .setUrl("https://wagba8805.page.link/authentication?email=" + email) // Payload for dynamic link
              .setDynamicLinkDomain("wagba8805.page.link")
              .setHandleCodeInApp(true)
              .build();

      FirebaseAuth.getInstance().sendSignInLinkToEmail(email, actionCodeSettings)
          .addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
              respondToSuccess(null, activity);
            } else {
              respondToFailure(task.getException(), activity);
            }
          });

    } // No other activities are expected to call this method
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
