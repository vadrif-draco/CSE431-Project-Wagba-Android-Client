package asu.foe.wagba8805.activities;

import static asu.foe.wagba8805.Constants.DISABLE_LOGIN_BUTTONS;
import static asu.foe.wagba8805.Constants.ENABLE_LOGIN_BUTTONS;
import static asu.foe.wagba8805.Constants.FINISH_YOURSELF;
import static asu.foe.wagba8805.services.FirebaseAuthService.login;

import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import asu.foe.wagba8805.databinding.GmailLoginBinding;
import asu.foe.wagba8805.interfaces.AuthResponsiveActivity;
import asu.foe.wagba8805.services.FirebaseAuthService;

public class GmailLoginActivity extends AppCompatActivity implements AuthResponsiveActivity {

  GmailLoginBinding glBinding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    glBinding = GmailLoginBinding.inflate(getLayoutInflater());
    setContentView(glBinding.getRoot());

    Handler handler = new Handler();
    handler.postDelayed(() -> {
      if (GmailLoginActivity.this.hasWindowFocus()) {
        Intent intent = new Intent("toMainActivity");
        intent.putExtra("message", DISABLE_LOGIN_BUTTONS);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        login(GmailLoginActivity.this);
      }
    }, 2000); // Delaying for two seconds for UX purposes
  }

  @Override
  protected void onPause() {
    super.onPause();
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
  }

  @Override
  public void respondToAuth(Boolean loggedIn, Boolean isNewUser) {

    Intent intent = new Intent("toMainActivity");
    if (loggedIn) { // Logged in successfully

      intent.putExtra("message", FINISH_YOURSELF);
      LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

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

    } else { // Failed to login

      intent.putExtra("message", ENABLE_LOGIN_BUTTONS);
      LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
      Toast.makeText(this, "Sign-in failed; " + FirebaseAuthService.getCurrentError(), Toast.LENGTH_LONG).show();

    }
    finish();

  }
}