package com.example.finalproject_1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class PaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        TextView tvPaymentStatus = findViewById(R.id.tvPaymentStatus);
        Button btnGoBack = findViewById(R.id.btnGoBack);



        btnGoBack.setOnClickListener(v -> {
            Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}