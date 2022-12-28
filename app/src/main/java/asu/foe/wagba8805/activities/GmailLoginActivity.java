package asu.foe.wagba8805.activities;

import static asu.foe.wagba8805.Constants.DISABLE_LOGIN_BUTTONS;
import static asu.foe.wagba8805.Constants.ENABLE_LOGIN_BUTTONS;
import static asu.foe.wagba8805.Constants.FINISH;
import static asu.foe.wagba8805.services.AuthService.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.auth.OAuthProvider;

import asu.foe.wagba8805.databinding.GmailLoginBinding;
import asu.foe.wagba8805.interfaces.AuthResponsiveActivity;

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
        OAuthProvider.Builder provider = OAuthProvider.newBuilder("google.com");
        login(GmailLoginActivity.this, provider);
      }
    }, 2000); // Delaying for two seconds for UX purposes
  }

  @Override
  protected void onPause() {
    super.onPause();
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
  }

  @Override
  public void respondToAuth(boolean logged_in) {

    Intent intent = new Intent("toMainActivity");
    if (logged_in) { // Logged in successfully

      intent.putExtra("message", FINISH);
      LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
      startActivity(new Intent(this, RestaurantsActivity.class)); // Proceed to app

    } else { // Failed to login

      intent.putExtra("message", ENABLE_LOGIN_BUTTONS);
      LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
      Toast.makeText(this, "Sign-in cancelled/failed...", Toast.LENGTH_SHORT).show();

    }
    finish();

  }
}