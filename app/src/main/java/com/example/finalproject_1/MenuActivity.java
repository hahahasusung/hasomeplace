package com.example.finalproject_1;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MenuActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    private RecyclerView recyclerView;
    private MenuAdapter menuAdapter;
    private List<MenuItem> selectedItems;
    private LinearLayout selectedItemsContainer;
    private TextView tvTotalPrice;
    private Map<String, List<MenuItem>> categoryMenuMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        String orderType = getIntent().getStringExtra("orderType");
        selectedItems = new ArrayList<>();
        selectedItemsContainer = findViewById(R.id.selectedItemsContainer);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);

        initMenuItems();

        menuAdapter = new MenuAdapter(new ArrayList<>(), new MenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MenuItem item) {
                addItemToOrder(item);
            }
        });
        recyclerView.setAdapter(menuAdapter);

        Button btnCoffee = findViewById(R.id.btnCoffee);
        Button btnDrinks = findViewById(R.id.btnDrinks);
        Button btnCake = findViewById(R.id.btnCake);
        Button btnIceCream = findViewById(R.id.btnIceCream);
        Button btnCheckout = findViewById(R.id.btnCheckout);
        Button btnVoiceRecognition = findViewById(R.id.btnVoiceRecognition);

        btnCoffee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateMenuItems("커피");
            }
        });

        btnDrinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateMenuItems("음료");
            }
        });

        btnCake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateMenuItems("케이크");
            }
        });

        btnIceCream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateMenuItems("아이스크림");
            }
        });

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, OrderSummaryActivity.class);
                intent.putExtra("orderType", orderType);
                intent.putParcelableArrayListExtra("selectedItems", new ArrayList<>(selectedItems));
                startActivity(intent);
            }
        });
        btnVoiceRecognition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVoiceRecognition();
            }
        });

        // Set initial category
        updateMenuItems("커피");
    }
    private void startVoiceRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "메뉴를 말하세요");

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if (result != null && !result.isEmpty()) {
                    String spokenText = result.get(0);
                    processVoiceCommand(spokenText);
                }
            }
        }
    }

    private void processVoiceCommand(String command) {
        for (List<MenuItem> menuItems : categoryMenuMap.values()) {
            for (MenuItem item : menuItems) {
                if (command.contains(item.getName())) {
                    addItemToOrder(item);
                    return;
                }
            }
        }
    }

    private void initMenuItems() {
        categoryMenuMap = new HashMap<>();

        List<MenuItem> coffeeItems = new ArrayList<>();
        coffeeItems.add(new MenuItem("아메리카노", 3000,R.drawable.ameri));
        coffeeItems.add(new MenuItem("카페라떼", 3500,R.drawable.latte));
        coffeeItems.add(new MenuItem("카푸치노", 4000,R.drawable.capu));

        List<MenuItem> drinksItems = new ArrayList<>();
        drinksItems.add(new MenuItem("오렌지 주스", 4000,R.drawable.orangeju));
        drinksItems.add(new MenuItem("레몬 에이드", 4500,R.drawable.lemonade));
        drinksItems.add(new MenuItem("수박 주스", 3500,R.drawable.waterju));
        drinksItems.add(new MenuItem("망고 주스", 4000,R.drawable.mangoju));
        drinksItems.add(new MenuItem("바나나 주스", 4000,R.drawable.bananaju));

        List<MenuItem> cakeItems = new ArrayList<>();
        cakeItems.add(new MenuItem("치즈 케이크", 5000,R.drawable.cheezecake));
        cakeItems.add(new MenuItem("초콜릿 케이크", 5500,R.drawable.choco));
        cakeItems.add(new MenuItem("레드벨벳 케이크", 6000,R.drawable.redvelvet));
        cakeItems.add(new MenuItem("딸기 케이크", 6000,R.drawable.strawberrycake));

        List<MenuItem> iceCreamItems = new ArrayList<>();
        iceCreamItems.add(new MenuItem("바닐라 아이스크림", 3000,R.drawable.vanillaice));
        iceCreamItems.add(new MenuItem("초콜릿 아이스크림", 3500,R.drawable.chocoice));
        iceCreamItems.add(new MenuItem("딸기 아이스크림", 4000,R.drawable.strawberryice));
        iceCreamItems.add(new MenuItem("민트초코 아이스크림", 4000,R.drawable.mintice));

        categoryMenuMap.put("커피", coffeeItems);
        categoryMenuMap.put("음료", drinksItems);
        categoryMenuMap.put("케이크", cakeItems);
        categoryMenuMap.put("아이스크림", iceCreamItems);
    }

    private void updateMenuItems(@NonNull String category) {
        List<MenuItem> items = categoryMenuMap.get(category);
        if (items != null) {
            menuAdapter.updateMenuItems(items);
        }
    }

    private void addItemToOrder(MenuItem item) {
        boolean found = false;
        for (MenuItem selectedItem : selectedItems) {
            if (selectedItem.equals(item)) {
                selectedItem.setQuantity(selectedItem.getQuantity() + 1);
                found = true;
                break;
            }
        }

        if (!found) {
            item.setQuantity(1);
            selectedItems.add(item);
        }

        updateOrderSummary();
    }

    private void updateOrderSummary() {
        selectedItemsContainer.removeAllViews();
        int totalPrice = 0;

        for (MenuItem selectedItem : selectedItems) {
            totalPrice += selectedItem.getPrice() * selectedItem.getQuantity();
            addSelectedItemView(selectedItem);
        }

        tvTotalPrice.setText("총 합계: " + totalPrice + "원");
    }

    private void addSelectedItemView(final MenuItem item) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.selected_item, selectedItemsContainer, false);

        TextView tvSelectedItemName = view.findViewById(R.id.tvSelectedItemName);
        TextView tvSelectedItemPrice = view.findViewById(R.id.tvSelectedItemPrice);
        TextView tvSelectedItemQuantity = view.findViewById(R.id.tvSelectedItemQuantity);
        Button btnDecreaseQuantity = view.findViewById(R.id.btnDecreaseQuantity);
        Button btnIncreaseQuantity = view.findViewById(R.id.btnIncreaseQuantity);
        ImageButton btnDeleteItem = view.findViewById(R.id.btnDeleteItem);

        tvSelectedItemName.setText(item.getName());
        tvSelectedItemPrice.setText(item.getPrice() + "원");
        tvSelectedItemQuantity.setText(String.valueOf(item.getQuantity()));

        btnDecreaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreaseItemQuantity(item);
            }
        });

        btnIncreaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseItemQuantity(item);
            }
        });

        btnDeleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItemFromOrder(item);
            }
        });

        selectedItemsContainer.addView(view);
    }

    private void removeItemFromOrder(MenuItem item) {
        selectedItems.remove(item);
        updateOrderSummary();
    }

    private void increaseItemQuantity(MenuItem item) {
        item.setQuantity(item.getQuantity() + 1);
        updateOrderSummary();
    }

    private void decreaseItemQuantity(MenuItem item) {
        if (item.getQuantity() > 1) {
            item.setQuantity(item.getQuantity() - 1);
        } else {
            selectedItems.remove(item);
        }
        updateOrderSummary();
    }
}
