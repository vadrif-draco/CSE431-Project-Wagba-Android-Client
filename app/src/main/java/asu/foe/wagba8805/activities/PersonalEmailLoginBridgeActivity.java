package asu.foe.wagba8805.activities;

import static asu.foe.wagba8805.Constants.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;

import asu.foe.wagba8805.databinding.PersonalEmailLoginBridgeBinding;

public class PersonalEmailLoginBridgeActivity extends AppCompatActivity {

  PersonalEmailLoginBridgeBinding plebBinding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    plebBinding = PersonalEmailLoginBridgeBinding.inflate(getLayoutInflater());
    setContentView(plebBinding.getRoot());

    Intent intent = getIntent();
    plebBinding.emailTV.setText(intent.getCharSequenceExtra("email"));

  }

  @Override
  protected void onPause() {
    super.onPause();
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
  }

  // This is what gets triggered when responding to the dynamic link with this activity's android:launchMode set to "singleTask"
  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    FirebaseDynamicLinks.getInstance()
        .getDynamicLink(getIntent())
        .addOnFailureListener(this, e -> Log.w(TAG, "getDynamicLink:onFailure", e))
        .addOnSuccessListener(this, pendingDynamicLinkData -> {

          // Get deep link from result (may be null if no link is found)
          // getDynamicLink() retrieves the link and clears that data so that it is only processed once
          if (pendingDynamicLinkData != null) pendingDynamicLinkData.getLink();

          FirebaseAuth auth = FirebaseAuth.getInstance();
          String emailLink = intent.getData().toString();
          Toast.makeText(this, emailLink, Toast.LENGTH_SHORT).show();

          // Confirm the link is a sign-in with email link.
          if (auth.isSignInWithEmailLink(emailLink)) {

            // The client SDK will parse the code from the link
            auth.signInWithEmailLink(plebBinding.emailTV.getText().toString(), emailLink)
                .addOnCompleteListener(task -> {
                  if (task.isSuccessful()) {
                    Log.d(TAG, "Successfully signed in with email link!");
                    AuthResult result = task.getResult();
                    // You can access the new user via result.getUser()
                    // Additional user info profile *IS NOT* available via: result.getAdditionalUserInfo().getProfile()
                    // You can check if the user is new or existing: result.getAdditionalUserInfo().isNewUser()
                    // -------------------------------------------------------------------------------------------------
                    // TODO: Read authentication stuff from path I guess? Confirm that email matches.
                    // Handle the deep link. For example, open the linked content
                    // TODO: get "authResult" from somewhere and pass it to AuthService
                    // startActivity(new Intent(this, RestaurantsActivity.class)); // Proceed to app
                    // finishAffinity(); // Pop all activities under this activity as well as itself
                  } else {
                    Log.e(TAG, "Error signing in with email link", task.getException());
                  }
                });
          }
        });
  }
}