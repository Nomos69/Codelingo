package com.example.codelingo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupMenuItems();
    }

    private void setupMenuItems() {
        // Find the layout container
        LinearLayout menuContainer = findViewById(R.id.menu_container);

        // Create menu items
        String[] topics = {"Print Statement", "Variables", "Operations", "Loops", "Array"};
        String[] descriptions = {"EASY", "EASY", "MEDIUM", "HARD", "HARD"};
        int[] backgroundColors = {
                R.color.blue,
                R.color.purple,
                R.color.yellow,
                R.color.red,
                R.color.orange
        };
        int[] icons = {
                R.drawable.trophy_icon,
                R.drawable.happy_face_icon,
                R.drawable.flag_icon,
                R.drawable.lemon_icon,
                R.drawable.folder_icon
        };

        // Add each menu item to the container
        for (int i = 0; i < topics.length; i++) {
            addMenuItem(menuContainer, topics[i], descriptions[i], backgroundColors[i], icons[i], i);
        }
    }

    private void addMenuItem(LinearLayout container, String title, String description,
                             int backgroundColor, int iconResId, final int position) {
        // Inflate menu item layout
        View itemView = getLayoutInflater().inflate(R.layout.menu_item, container, false);

        // Set background color
        CardView cardView = itemView.findViewById(R.id.card_view);
        cardView.setCardBackgroundColor(ContextCompat.getColor(this, backgroundColor));

        // Set text
        TextView titleTextView = itemView.findViewById(R.id.title_text);
        TextView descriptionTextView = itemView.findViewById(R.id.description_text);
        titleTextView.setText(title);
        descriptionTextView.setText(description);

        // Set icon
        ImageView iconImageView = itemView.findViewById(R.id.icon_image);
        iconImageView.setImageResource(iconResId);

        // Set click listener
        Button enterButton = itemView.findViewById(R.id.enter_button);
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the road map activity
                Intent intent = new Intent(MainActivity.this, RoadMapActivity.class);
                intent.putExtra("TOPIC_TITLE", title);
                intent.putExtra("TOPIC_POSITION", position);
                startActivity(intent);
            }
        });

        // Add to container
        container.addView(itemView);
    }
}