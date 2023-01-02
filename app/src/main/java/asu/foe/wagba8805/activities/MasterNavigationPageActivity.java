package asu.foe.wagba8805.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import asu.foe.wagba8805.databinding.MasterNavigationPageBinding;

public class MasterNavigationPageActivity extends AppCompatActivity {

  MasterNavigationPageBinding mnpBinding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    mnpBinding = MasterNavigationPageBinding.inflate(getLayoutInflater());
    setContentView(mnpBinding.getRoot());

    mnpBinding.profileBtn.setOnClickListener(v -> startActivity(new Intent(this, ProfilePageActivity.class)));

    mnpBinding.cartBtn.setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));

    // mnpBinding.ordersBtn.setOnClickListener(v -> startActivity(new Intent(this, OrdersPageActivity.class)));
  }

  @Override
  protected void onPause() {
    super.onPause();
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
  }
}