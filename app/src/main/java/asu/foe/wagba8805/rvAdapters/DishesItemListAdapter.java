package asu.foe.wagba8805.rvAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import asu.foe.wagba8805.MainActivity;
import asu.foe.wagba8805.R;
import asu.foe.wagba8805.databinding.DishesItemBinding;
import asu.foe.wagba8805.pojos.Dish;

public class DishesItemListAdapter extends RecyclerView.Adapter<DishesItemListAdapter.ViewHolder> {

  private final Context mContext;
  private List<Dish> dishes = new ArrayList<>();

  public DishesItemListAdapter(Context context) {
    this.mContext = context;
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    DishesItemBinding binding;

    public ViewHolder(DishesItemBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(DishesItemBinding.inflate(LayoutInflater.from(mContext), parent, false));
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    Dish item = dishes.get(position);
    Glide.with(holder.itemView)
        .load(item.imageUrl)
        .thumbnail(
            Glide.with(holder.itemView)
                .load(R.drawable.dish_placeholder)
                .centerCrop()
        )
        .centerCrop()
        .into(holder.binding.img);
    holder.binding.dishNameTV.setText(item.name);
    holder.binding.dishDescTV.setText(item.description);
    if (!item.available) {
      holder.binding.rootLayout.setAlpha(0.75f);
      holder.binding.proceedBtn.setVisibility(View.GONE);
      holder.binding.outOfStockPrompt.setVisibility(View.VISIBLE);
    } else {
      holder.binding.rootLayout.setAlpha(1f);
      holder.binding.proceedBtn.setVisibility(View.VISIBLE);
      holder.binding.outOfStockPrompt.setVisibility(View.GONE);
    }
    // TODO: Get cart viewmodel, if item in items in cart, make the drawable - , otherwise +
    holder.binding.proceedBtn.setOnClickListener(v -> {
      if (!MainActivity.sharedPrefs.getBoolean("addToCartPromptDismissedForever", false)) {
        // TODO: Add to cart
        new AlertDialog.Builder(holder.itemView.getContext())
            .setCancelable(false)
            .setTitle("Added to Cart!")
            .setMessage("Hi there!\n\n" +
                "You've just added a dish to your cart!\n\n" +
                "You can view and customize your cart by touching your profile pic at the top right corner. " +
                "You can also proceed with creating an order once you're satisfied with the selection in your cart. " +
                "This is also where you can track your current orders or view your past orders.")
            .setPositiveButton("Okay", (dialogInterface, i) -> dialogInterface.dismiss())
            .setNegativeButton("Don't Show Again", (dialogInterface, i) -> {
              MainActivity.sharedPrefs.edit().putBoolean("addToCartPromptDismissedForever", true).apply();
              dialogInterface.dismiss();
            })
            .create()
            .show();
      }
    });
  }

  @Override
  public int getItemCount() {
    return dishes.size();
  }

  public void set(List<Dish> items) {
    this.dishes = items;
    notifyDataSetChanged();
  }
}
