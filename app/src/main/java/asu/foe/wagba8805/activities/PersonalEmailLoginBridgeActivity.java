package asu.foe.wagba8805.activities;

import static asu.foe.wagba8805.Constants.SHARED_PREFS;
import static asu.foe.wagba8805.services.FirebaseAuthService.login;

import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import asu.foe.wagba8805.MainActivity;
import asu.foe.wagba8805.databinding.PersonalEmailLoginBridgeBinding;
import asu.foe.wagba8805.interfaces.AuthResponsiveActivity;
import asu.foe.wagba8805.services.FirebaseAuthService;

public class PersonalEmailLoginBridgeActivity extends AppCompatActivity implements AuthResponsiveActivity {

  PersonalEmailLoginBridgeBinding plebBinding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    // Since this is another plausible entry point to the application, need to initialize shared preferences variable
    MainActivity.sharedPrefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

    plebBinding = PersonalEmailLoginBridgeBinding.inflate(getLayoutInflater());
    setContentView(plebBinding.getRoot());

    plebBinding.emailTV.setText(FirebaseAuthService.getCurrentSignInEmail());

    login(this);

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
    login(this);
  }

  @Override
  public void respondToAuth(Boolean loggedIn, Boolean isNewUser) {

    if (loggedIn) {

      TaskStackBuilder tsb = TaskStackBuilder.create(this);
      Intent proceedToAppIntent = new Intent(this, RestaurantsActivity.class);
      tsb.addNextIntent(proceedToAppIntent);

      if (isNewUser) { // If a new user, direct first to profile page
        proceedToAppIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        Intent isNewUserIntent = new Intent(this, ProfilePageActivity.class);
        isNewUserIntent.putExtra("isNewUser", true);
        tsb.addNextIntent(isNewUserIntent);
      }

      tsb.startActivities();
      finishAffinity(); // Pop all activities under this activity as well as itself

    } else {

      Toast.makeText(this, "Sign-in failed; " + FirebaseAuthService.getCurrentError(), Toast.LENGTH_LONG).show();
      // Since this is an entry point of its own, simply finishing it won't get us back to main activity in some cases, so:
      startActivity(new Intent(this, PersonalEmailLoginActivity.class)); // Back to main activity
      finishAffinity(); // Pop all activities under this activity as well as itself

    }
    // TODO: Clear salt and email (in AuthService!!!!! NOT HERE)

  }
}