package asu.foe.wagba8805.services;

import static asu.foe.wagba8805.Constants.FACULTY_AZURE_TENANT_ID;
import static asu.foe.wagba8805.Constants.TAG;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.OAuthCredential;
import com.google.firebase.auth.OAuthProvider;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;

import java.util.Arrays;
import java.util.Objects;

import asu.foe.wagba8805.MainActivity;
import asu.foe.wagba8805.activities.FacultyEmailLoginActivity;
import asu.foe.wagba8805.activities.GmailLoginActivity;
import asu.foe.wagba8805.activities.PersonalEmailLoginActivity;
import asu.foe.wagba8805.activities.PersonalEmailLoginBridgeActivity;
import asu.foe.wagba8805.interfaces.AuthResponsiveActivity;

public class FirebaseAuthService {

  private static String error = "undefined error";

  // User signed in successfully.
  private static void respondToSuccess(@Nullable AuthResult authResult, AuthResponsiveActivity activity) {
    if (authResult != null) {
      SharedPreferences.Editor sharedPrefsEditor = MainActivity.sharedPrefs.edit();
      sharedPrefsEditor.putString("accessToken", ((OAuthCredential) Objects.requireNonNull(authResult.getCredential())).getAccessToken());
      sharedPrefsEditor.putString("idToken", ((OAuthCredential) authResult.getCredential()).getIdToken());
      sharedPrefsEditor.putString("provider", authResult.getCredential().getProvider());
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

  public static String getCurrentAccessToken() {
    return MainActivity.sharedPrefs.getString("accessToken", "");
  }

  public static void clearCurrentAccessToken() {
    MainActivity.sharedPrefs.edit().putString("accessToken", "").apply();
  }

  public static String getCurrentIdToken() {
    return MainActivity.sharedPrefs.getString("idToken", "");
  }

  public static void clearCurrentIdToken() {
    MainActivity.sharedPrefs.edit().putString("idToken", "").apply();
  }

  public static String getCurrentProvider() {
    return MainActivity.sharedPrefs.getString("provider", "");
  }

  public static void clearCurrentProvider() {
    MainActivity.sharedPrefs.edit().putString("provider", "").apply();
  }

  public static String getCurrentSignInEmail() {
    return MainActivity.sharedPrefs.getString("signInEmail", "");
  }

  public static void clearCurrentSignInEmail() {
    MainActivity.sharedPrefs.edit().putString("signInEmail", "").apply();
  }

  public static String getCurrentSignInSalt() {
    return MainActivity.sharedPrefs.getString("signInSalt", "");
  }

  public static void clearCurrentSignInSalt() {
    MainActivity.sharedPrefs.edit().putString("signInSalt", "").apply();
  }

  public static String getCurrentError() {
    return error;
  }

  public static void clearFirebaseAuthServiceSharedPrefs() {
    clearCurrentAccessToken();
    clearCurrentIdToken();
    clearCurrentProvider();
    clearCurrentSignInEmail();
    clearCurrentSignInSalt();
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
      String salt = HashingService.createSalt();

      ActionCodeSettings actionCodeSettings =
          ActionCodeSettings.newBuilder()
              .setAndroidPackageName("asu.foe.wagba8805", false, null)
              // In the case where the code is sent directly to the app and the app is installed,
              // this is the continueURL query parameter in the dynamic link payload.
              // Otherwise, when the code is handled by the widget itself, it is the payload itself.
              // Accordingly, for my use-case, I have no website to fall back to, I only want the dynamic link payload.
              .setUrl("https://wagba8805.authenticate/" + HashingService.createSHA256Hash(salt + email))
              .setHandleCodeInApp(true)
              .build();

      FirebaseAuth.getInstance().sendSignInLinkToEmail(email, actionCodeSettings)
          .addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
              SharedPreferences.Editor sharedPrefsEditor = MainActivity.sharedPrefs.edit();
              sharedPrefsEditor.putString("signInEmail", email);
              sharedPrefsEditor.putString("signInSalt", salt);
              sharedPrefsEditor.apply();
              respondToSuccess(null, activity);
            } else {
              respondToFailure(task.getException(), activity);
            }
          });

    } else if (activity instanceof PersonalEmailLoginBridgeActivity) {

      Intent intent = ((PersonalEmailLoginBridgeActivity) activity).getIntent();
      Log.d(TAG, "Intent received: " + intent);

      if (intent != null && intent.getData() != null) {

        FirebaseDynamicLinks.getInstance()
            .getDynamicLink(intent)
            .addOnFailureListener((Activity) activity, e -> Log.w(TAG, "getDynamicLink:onFailure", e))
            .addOnSuccessListener((Activity) activity, pendingDynamicLinkData -> {

              // Get deep link from result (may be null if no link is found)
              // getDynamicLink() retrieves the link and clears that data so that it is only processed once
              if (pendingDynamicLinkData != null) pendingDynamicLinkData.getLink();

              String emailLink = intent.getData().toString();
              Log.d(TAG, "Dynamic Link payload as retrieved from intent.getData().toString(): " + emailLink);

              // Confirm the link is a sign-in with email link.
              if (FirebaseAuth.getInstance().isSignInWithEmailLink(emailLink)) {

                String email = FirebaseAuthService.getCurrentSignInEmail();

                if (email != null && !email.isEmpty()) {

                  // The client SDK will parse the code from the link
                  // TODO: If this doesn't work, use the salt and email stored and hash and compare them yourself
                  FirebaseAuth.getInstance().signInWithEmailLink(email, emailLink)
                      .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                          // You can access the new user via result.getUser()
                          // Additional user info profile *IS NOT* available via: result.getAdditionalUserInfo().getProfile()
                          // You can check if the user is new or existing: result.getAdditionalUserInfo().isNewUser()
                          // -------------------------------------------------------------------------------------------------
                          // TODO: Read authentication stuff from path I guess? Confirm that email matches.
                          // Handle the deep link. For example, open the linked content (?????????????)
                          // TODO: Pass "result" to AuthService or do its job or sth idk just handle it... its job is to put user in roomdb
                          Log.d(TAG, "Successfully signed in with email link!");
                          respondToSuccess(task.getResult(), activity);
                        } else {
                          respondToFailure(task.getException(), activity);
                        }
                      });
                } else {
                  respondToFailure(new Exception("an internal error has occurred."), activity);
                }
              }
            });

      }

    } // No other activities are expected to call this method
  }

  public static void signOut() {
    clearFirebaseAuthServiceSharedPrefs();
    FirebaseAuth.getInstance().signOut();
  }
}
