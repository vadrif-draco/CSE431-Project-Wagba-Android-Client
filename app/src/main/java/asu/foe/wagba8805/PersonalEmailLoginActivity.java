package asu.foe.wagba8805;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import asu.foe.wagba8805.databinding.PersonalEmailLoginBinding;

public class PersonalEmailLoginActivity extends AppCompatActivity {

  PersonalEmailLoginBinding pelBinding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    pelBinding = PersonalEmailLoginBinding.inflate(getLayoutInflater());
    setContentView(pelBinding.getRoot());

    pelBinding.loginBtn.setOnClickListener(v -> {
      if (Objects.requireNonNull(pelBinding.emailInput.getText()).toString().equals("admin")
          && Objects.requireNonNull(pelBinding.passwordInput.getText()).toString().equals("admin")) {
        startActivity(new Intent(this, RestaurantsActivity.class));
      }
    });

    pelBinding.registerHyperlink.setOnClickListener(v -> {
      Toast.makeText(this, "Registration is not yet implemented", Toast.LENGTH_SHORT).show();
    });
  }

  @Override
  protected void onPause() {
    super.onPause();
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
  }
}