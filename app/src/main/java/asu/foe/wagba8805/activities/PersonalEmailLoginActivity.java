package asu.foe.wagba8805.activities;

import static asu.foe.wagba8805.services.FirebaseAuthService.login;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import asu.foe.wagba8805.R;
import asu.foe.wagba8805.databinding.PersonalEmailLoginBinding;
import asu.foe.wagba8805.interfaces.AuthResponsiveActivity;
import asu.foe.wagba8805.services.FirebaseAuthService;

public class PersonalEmailLoginActivity extends AppCompatActivity implements AuthResponsiveActivity {

  public PersonalEmailLoginBinding pelBinding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    pelBinding = PersonalEmailLoginBinding.inflate(getLayoutInflater());
    setContentView(pelBinding.getRoot());

    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

      pelBinding.userInputLayout.setRowCount(1);
      pelBinding.userInputLayout.setColumnCount(2);
      pelBinding.testingArea.setOrientation(LinearLayout.HORIZONTAL);
      pelBinding.testingArea.setPadding(
          pelBinding.testingArea.getLeft(),
          (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics()),
          pelBinding.testingArea.getRight(),
          (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics())
      );

    } else {

      pelBinding.userInputLayout.setRowCount(2);
      pelBinding.userInputLayout.setColumnCount(1);
      pelBinding.testingArea.setOrientation(LinearLayout.VERTICAL);
      pelBinding.testingArea.setPadding(
          pelBinding.testingArea.getLeft(),
          (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()),
          pelBinding.testingArea.getRight(),
          (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics())
      );

    }

    pelBinding.loginBtn.setOnClickListener(v -> {
      if (Objects.requireNonNull(pelBinding.emailInput.getText()).toString().equals("admin")) {

        FirebaseAuthService.admin = true;
        startActivity(new Intent(this, RestaurantsActivity.class));
        finishAffinity();

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
  }

  @Override
  protected void onPause() {
    super.onPause();
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
  }

  @Override
  public void respondToAuth(boolean email_sent) {

    if (email_sent) {

      startActivity(new Intent(this, PersonalEmailLoginBridgeActivity.class));

    } else { // Email wasn't sent

      Toast.makeText(this, "Sign-in failed; " + FirebaseAuthService.getCurrentError(), Toast.LENGTH_LONG).show();
      pelBinding.loginBtn.setAlpha(1f);
      pelBinding.loginBtn.setText(R.string.loginBtnText);
      pelBinding.loginBtn.setEnabled(true);

    }

  }
}