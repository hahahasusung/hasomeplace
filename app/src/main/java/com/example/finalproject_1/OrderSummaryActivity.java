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
    private int totalPrice; // totalPrice 변수를 범위 밖에서 정의

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

        TextView tvOrderSummary = findViewById(R.id.tvOrderSummary);
        Button btnCheckout = findViewById(R.id.btnCheckout);
        Button btnCancel = findViewById(R.id.btnCancel);
        String orderType = getIntent().getStringExtra("orderType");
        // Firebase Realtime Database의 레퍼런스 설정
        databaseRef = FirebaseDatabase.getInstance().getReference("orders");

        List<MenuItem> selectedItems = getIntent().getParcelableArrayListExtra("selectedItems");
        if (selectedItems != null) {
            StringBuilder orderSummary = new StringBuilder("주문 내역:\n");

            totalPrice = 0; // totalPrice 초기화
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
            // 주문 정보를 Firebase에 저장
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
        String orderType = getIntent().getStringExtra("orderType"); // orderType을 Intent에서 가져옴

        // 데이터베이스에서 현재 최대 orderId를 가져와서 새로운 orderId 생성
        databaseRef.orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int newOrderId = 1; // 기본값으로 1 설정
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
                            // 데이터 저장 성공 시 호출
                            Toast.makeText(OrderSummaryActivity.this, "Order saved successfully!", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            // 데이터 저장 실패 시 호출
                            Toast.makeText(OrderSummaryActivity.this, "Failed to save order: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 데이터베이스 오류 처리
                Toast.makeText(OrderSummaryActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String generateOrderNumber() {
        // 실제로는 더 복잡한 방법으로 주문 번호 생성 로직을 구현할 수 있습니다.
        return "ORD-" + System.currentTimeMillis();
    }
}