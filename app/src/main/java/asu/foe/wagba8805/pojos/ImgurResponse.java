package asu.foe.wagba8805.pojos;

public class ImgurResponse {

  public static class Data {

    public String id;
    public String title;
    public String description;
    public Integer datetime;
    public String type;
    public Boolean animated;
    public Integer width;
    public Integer height;
    public Integer size;
    public Integer bandwidth;
    public Integer accountId;
    public Boolean inGallery;
    public String deletehash;
    public String name;
    public String link;

  }


  public Data data;
  public Boolean success;
  public Integer status;

}
