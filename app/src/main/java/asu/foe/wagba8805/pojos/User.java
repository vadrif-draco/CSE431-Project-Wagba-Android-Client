package asu.foe.wagba8805.pojos;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_table")
public class User {

  @NonNull
  @PrimaryKey
  public String uuid;

  public String email;
  public String displayName;
  public String imgUrl;
  public Boolean isMale;

  public User(@NonNull String uuid, String email, String displayName, String imgUrl, Boolean isMale) {
    this.uuid = uuid;
    this.email = email;
    this.displayName = displayName;
    this.imgUrl = imgUrl;
    this.isMale = isMale;
  }
}
