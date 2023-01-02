package asu.foe.wagba8805.rvAdapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import asu.foe.wagba8805.MainActivity;
import asu.foe.wagba8805.R;
import asu.foe.wagba8805.databinding.DishesItemBinding;
import asu.foe.wagba8805.pojos.Dish;
import asu.foe.wagba8805.viewmodels.DishViewModel;

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

    if (item.inCartQuantity == 0) {
      holder.binding.proceedBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.add_icon, 0);
    } else {
      holder.binding.proceedBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.remove_icon, 0);
      Drawable drawable = holder.binding.proceedBtn.getCompoundDrawables()[2]; // 2 accounts for right(end) drawable
      drawable.setColorFilter(
          new PorterDuffColorFilter(
              ContextCompat.getColor(
                  holder.binding.proceedBtn.getContext(),
                  com.google.android.material.R.color.design_default_color_error
              ), PorterDuff.Mode.SRC_IN
          )
      );
    }

    holder.binding.proceedBtn.setOnClickListener(v -> {

          if (item.inCartQuantity == 0) { // i.e., it's not in cart; add it!

            item.inCartQuantity = 1;

            if (!MainActivity.sharedPrefs.getBoolean("addToCartPromptDismissedForever", false)) {
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

          } else { // It's in cart! Remove it.

            item.inCartQuantity = 0;

          }

          DishViewModel dishViewModel = new ViewModelProvider((ViewModelStoreOwner) mContext).get(DishViewModel.class);
          LiveData<List<Dish>> dishLiveData = dishViewModel.getDish(item.id, item.restaurant_id);
          Observer<List<Dish>> dishObserver = new Observer<List<Dish>>() {
            @Override
            public void onChanged(List<Dish> dishes) {
              Dish dish = dishes.get(0);
              dish.inCartQuantity = item.inCartQuantity;
              dishViewModel.updateDishData(dish);
              dishLiveData.removeObserver(this);
            }
          };
          dishLiveData.observeForever(dishObserver);

        }

    );
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
