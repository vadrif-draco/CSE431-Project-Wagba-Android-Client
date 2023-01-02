package asu.foe.wagba8805.activities;

import static asu.foe.wagba8805.Constants.TAG;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import asu.foe.wagba8805.databinding.OrderBinding;
import asu.foe.wagba8805.pojos.Order;
import asu.foe.wagba8805.rvAdapters.OrderListAdapter;

public class OrderActivity extends AppCompatActivity {

  OrderBinding oBinding;
  List<Order> orders = new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    oBinding = OrderBinding.inflate(getLayoutInflater());
    setContentView(oBinding.getRoot());
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    RecyclerView orderssRV = oBinding.ordersRV;
    OrderListAdapter ordersAdapter = new OrderListAdapter(this);
    ordersAdapter.set(orders);
    orderssRV.setAdapter(ordersAdapter);
    orderssRV.setLayoutManager(new LinearLayoutManager(this));
    database.getReference("orders").addValueEventListener(new ValueEventListener() {

      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        orders = new ArrayList<>();
        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
          orders.add(childSnapshot.getValue(Order.class));
        }
        ordersAdapter.set(orders);
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {
        // Failed to read value
        Log.w(TAG, "Failed to read value.", error.toException());
      }
    });
  }
}