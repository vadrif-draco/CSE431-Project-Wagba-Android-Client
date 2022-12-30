package asu.foe.wagba8805.services;

import static asu.foe.wagba8805.Constants.TAG;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import asu.foe.wagba8805.pojos.ImgurResponse;

// For guidance refer to: https://apidocs.imgur.com/
public class ImgurUploadService extends AsyncTask<Bitmap, Void, ImgurResponse> {

  public interface ImgurUploadServiceResponsiveActivity {
    public void respondToUploadBegin();
    public void respondToUploadEnd(ImgurResponse response);
  }

  private final ImgurUploadServiceResponsiveActivity parentActivity;

  public ImgurUploadService(ImgurUploadServiceResponsiveActivity activity) {
    parentActivity = activity;
  }

  private static final String CLIENT_ID = "bf267e5782f79fd";

  private ImgurResponse uploadBitmapToImgur(Bitmap bitmap) {

    try {

      HttpURLConnection conn = (HttpURLConnection) new URL("https://api.imgur.com/3/image").openConnection();
      conn.setRequestMethod("POST");
      conn.setRequestProperty("Authorization", "Client-ID " + CLIENT_ID);
      conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      conn.setDoOutput(true); // First we send the image
      conn.setDoInput(true); // Then we retrieve response

      OutputStream os = conn.getOutputStream();
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
      HashMap<String, String> params = new HashMap<>();
      params.put("image", getBase64ForBitmap(bitmap));
      writer.write(getPostDataString(params));
      writer.flush();
      writer.close();
      os.close();

      int responseCode = conn.getResponseCode();
      if (responseCode == HttpsURLConnection.HTTP_OK) {
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) response.append(line);
        return new Gson().fromJson(response.toString(), (Type) ImgurResponse.class);
      }

    } catch (Exception e) {

      e.printStackTrace();

    }

    return null;

  }

  private String getBase64ForBitmap(Bitmap bitmap) {
    ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOut);
    return Base64.encodeToString(byteArrayOut.toByteArray(), Base64.DEFAULT);
  }

  private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
    StringBuilder data = new StringBuilder();
    for (Map.Entry<String, String> entry : params.entrySet()) {
      data.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
      data.append("=");
      data.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
      data.append("&");
    }
    data.deleteCharAt(data.length() - 1); // Remove the last &
    return data.toString();
  }

  @Override
  protected void onPreExecute() {
    super.onPreExecute();
    parentActivity.respondToUploadBegin();
  }

  @Override
  protected void onPostExecute(ImgurResponse imgurResponse) {
    super.onPostExecute(imgurResponse);
    parentActivity.respondToUploadEnd(imgurResponse);
  }

  @Override
  protected ImgurResponse doInBackground(Bitmap... bitmaps) {
    ImgurResponse ir = uploadBitmapToImgur(bitmaps[0]);
    Log.d(TAG, "doInBackground: " + ir.data.link);
    return ir;
  }
}
