package asu.foe.wagba8805.rvAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import asu.foe.wagba8805.R;
import asu.foe.wagba8805.activities.DishesActivity;
import asu.foe.wagba8805.databinding.RestaurantsItemBinding;
import asu.foe.wagba8805.pojos.Restaurant;

public class RestaurantsItemListAdapter extends RecyclerView.Adapter<RestaurantsItemListAdapter.ViewHolder> {

  private final Context mContext;
  private List<Restaurant> restaurantItems = new ArrayList<>();

  public RestaurantsItemListAdapter(Context context) {
    this.mContext = context;
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    RestaurantsItemBinding binding;

    public ViewHolder(RestaurantsItemBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(RestaurantsItemBinding.inflate(LayoutInflater.from(mContext), parent, false));
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    Restaurant item = restaurantItems.get(position);
    Glide.with(holder.itemView)
        .load(item.imageUrl)
        .thumbnail(
            Glide.with(holder.itemView)
                .load(R.drawable.restaurant_placeholder)
                .centerCrop()
        )
        .centerCrop()
        .into(holder.binding.img);
    holder.binding.restaurantNameTV.setText(item.name);
    holder.binding.restaurantAddressTV.setText(item.address);
    holder.binding.proceedBtn.setOnClickListener(v -> {
      Intent intent = new Intent(mContext, DishesActivity.class);
      intent.putExtra("restaurant_id", item.id);
      mContext.startActivity(intent);
    });
  }

  @Override
  public int getItemCount() {
    return restaurantItems.size();
  }

  public void set(List<Restaurant> items) {
    this.restaurantItems = items;
    notifyDataSetChanged();
  }
}
