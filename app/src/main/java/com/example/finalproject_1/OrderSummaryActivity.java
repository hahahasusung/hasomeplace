package com.example.finalproject_1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class OrderSummaryActivity extends AppCompatActivity {

    private DatabaseReference databaseRef;
    private int totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

        TextView tvOrderSummary = findViewById(R.id.tvOrderSummary);
        Button btnCheckout = findViewById(R.id.btnCheckout);
        Button btnCancel = findViewById(R.id.btnCancel);
        String orderType = getIntent().getStringExtra("orderType");

        databaseRef = FirebaseDatabase.getInstance().getReference("orders");

        List<MenuItem> selectedItems = getIntent().getParcelableArrayListExtra("selectedItems");
        if (selectedItems != null) {
            StringBuilder orderSummary = new StringBuilder("주문 내역:\n");

            totalPrice = 0;
            for (MenuItem item : selectedItems) {
                int itemTotalPrice = item.getPrice() * item.getQuantity();
                orderSummary.append(item.getName())
                        .append(" - ")
                        .append(item.getPrice())
                        .append("원 x ")
                        .append(item.getQuantity())
                        .append(" = ")
                        .append(itemTotalPrice)
                        .append("원\n");
                totalPrice += itemTotalPrice;
            }

            orderSummary.append("\n총 합계: ").append(totalPrice).append("원");

            tvOrderSummary.setText(orderSummary.toString());
        }

        btnCheckout.setOnClickListener(v -> {

            saveOrderToFirebase(selectedItems);
            Intent intent = new Intent(OrderSummaryActivity.this, PaymentActivity.class);
            intent.putExtra("orderNumber", generateOrderNumber());
            startActivity(intent);
        });

        btnCancel.setOnClickListener(v -> {
            Intent intent = new Intent(OrderSummaryActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void saveOrderToFirebase(List<MenuItem> items) {
        String orderType = getIntent().getStringExtra("orderType");


        databaseRef.orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int newOrderId = 1;
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String lastOrderId = snapshot.getKey();
                        if (lastOrderId != null) {
                            newOrderId = Integer.parseInt(lastOrderId) + 1;
                        }
                    }
                }

                Order order = new Order(String.valueOf(newOrderId), orderType, items, totalPrice);
                databaseRef.child(String.valueOf(newOrderId)).setValue(order)
                        .addOnSuccessListener(aVoid -> {

                            Toast.makeText(OrderSummaryActivity.this, "Order saved successfully!", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {

                            Toast.makeText(OrderSummaryActivity.this, "Failed to save order: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(OrderSummaryActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String generateOrderNumber() {

        return "ORD-" + System.currentTimeMillis();
    }
}