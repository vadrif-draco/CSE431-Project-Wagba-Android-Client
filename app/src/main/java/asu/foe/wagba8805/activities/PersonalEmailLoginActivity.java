package asu.foe.wagba8805.activities;

import static asu.foe.wagba8805.services.AuthService.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import asu.foe.wagba8805.R;
import asu.foe.wagba8805.databinding.PersonalEmailLoginBinding;
import asu.foe.wagba8805.interfaces.AuthResponsiveActivity;
import asu.foe.wagba8805.services.AuthService;

public class PersonalEmailLoginActivity extends AppCompatActivity implements AuthResponsiveActivity {

  public PersonalEmailLoginBinding pelBinding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    pelBinding = PersonalEmailLoginBinding.inflate(getLayoutInflater());
    setContentView(pelBinding.getRoot());

    pelBinding.loginBtn.setOnClickListener(v -> {
      if (Objects.requireNonNull(pelBinding.emailInput.getText()).toString().equals("admin")) {

        startActivity(new Intent(this, RestaurantsActivity.class));
        finishAffinity();
        // TODO: Make an actual user for the "admin" in RoomDB

      } else if (Objects.requireNonNull(pelBinding.emailInput.getText()).toString().isEmpty()) {

        Toast.makeText(this, "Sign-in failed; email field is empty!", Toast.LENGTH_LONG).show();

      } else {

        pelBinding.loginBtn.setEnabled(false);
        pelBinding.loginBtn.setText(R.string.loginBtnPleaseWaitText);
        pelBinding.loginBtn.setAlpha(0.5f);
        // TODO: Provide wait time to allow user to cancel in case they notice the email is wrong before invoking login
        login(this);

      }
    });

    pelBinding.registerHyperlink.setOnClickListener(v -> {
      // TODO: Implement it
      Toast.makeText(this, "Registration is not yet implemented", Toast.LENGTH_SHORT).show();
    });
  }

  @Override
  protected void onPause() {
    super.onPause();
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
  }

  @Override
  public void respondToAuth(boolean email_sent) {

    if (email_sent) {

      Intent intent = new Intent(this, PersonalEmailLoginBridgeActivity.class);
      intent.putExtra("email", pelBinding.emailInput.getText());
      startActivity(intent);

    } else { // Failed to login

      Toast.makeText(this, "Sign-in failed; " + AuthService.error, Toast.LENGTH_LONG).show();
      pelBinding.loginBtn.setAlpha(1f);
      pelBinding.loginBtn.setText(R.string.loginBtnText);
      pelBinding.loginBtn.setEnabled(true);

    }

  }
}