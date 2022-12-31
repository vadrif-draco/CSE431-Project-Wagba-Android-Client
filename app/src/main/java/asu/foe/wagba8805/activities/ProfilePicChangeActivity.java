package asu.foe.wagba8805.activities;

import static asu.foe.wagba8805.Constants.IMAGE_BROWSE_RC;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import asu.foe.wagba8805.R;
import asu.foe.wagba8805.databinding.ProfilePicChangePageBinding;
import asu.foe.wagba8805.pojos.ImgurResponse;
import asu.foe.wagba8805.pojos.User;
import asu.foe.wagba8805.services.FirebaseProfileService;
import asu.foe.wagba8805.services.ImgurUploadService;
import asu.foe.wagba8805.viewmodels.UserViewModel;

public class ProfilePicChangeActivity extends AppCompatActivity implements ImgurUploadService.ImgurUploadServiceResponsiveActivity {

  ProfilePicChangePageBinding pppBinding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    pppBinding = ProfilePicChangePageBinding.inflate(getLayoutInflater());
    setContentView(pppBinding.getRoot());

    pppBinding.browseBtn.setOnClickListener(v -> {
      Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
      startActivityForResult(i, IMAGE_BROWSE_RC);
    });

    pppBinding.uploadBtn.setOnClickListener(v -> {
      ImgurUploadService imgurUploadService = new ImgurUploadService(this);
      imgurUploadService.execute(((BitmapDrawable) pppBinding.img.getDrawable()).getBitmap());
    });

    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

      pppBinding.gridLayout.setOrientation(LinearLayout.HORIZONTAL);

    } else {

      pppBinding.gridLayout.setOrientation(LinearLayout.VERTICAL);

    }
  }

  @Override
  protected void onPause() {
    super.onPause();
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
  }


  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == IMAGE_BROWSE_RC && resultCode == RESULT_OK && data != null) {
      Glide.with(this)
          .load(data.getData())
          .centerCrop()
          .apply(RequestOptions.circleCropTransform())
          .into(pppBinding.img);
      pppBinding.uploadBtn.setEnabled(true);
    }
  }

  public void respondToUploadBegin() {
    pppBinding.uploadBtn.setEnabled(false);
    pppBinding.saveBtn.setEnabled(false);
    pppBinding.uploadBtn.setText(R.string.uploadingPrompt);
  }

  public void respondToUploadEnd(ImgurResponse response) {
    pppBinding.uploadBtn.setEnabled(true);
    pppBinding.uploadBtn.setText(R.string.uploadPrompt);
    pppBinding.saveBtn.setEnabled(true);
    pppBinding.saveBtn.setOnClickListener(v -> {
      UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
      LiveData<List<User>> l = userViewModel.getUserByUUID(FirebaseProfileService.getUuid());
      Observer<List<User>> o = new Observer<List<User>>() {
        @Override
        public void onChanged(List<User> users) {
          User user = users.get(0);
          user.imgUrl = response.data.link;
          userViewModel.updateUserData(user);
          l.removeObserver(this);
        }
      };
      l.observeForever(o);
      finish();
    });
  }
}