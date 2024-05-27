package com.kursovaya.receptorganaizer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;

public class RecipeDetailActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SELECT_IMAGE = 2;
    private ImageView imageView;
    private EditText titleEditText;
    private EditText descriptionEditText;
    private String imagePath;
    private int recipeIndex;
    private int recipeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        imageView = findViewById(R.id.recipeImage);
        titleEditText = findViewById(R.id.recipeTitle);
        descriptionEditText = findViewById(R.id.recipeDescription);
        Button saveButton = findViewById(R.id.saveButton);
        Button removeButton = findViewById(R.id.removeButton);

        Intent intent = getIntent();
        recipeIndex = intent.getIntExtra("recipeIndex", -1);
        recipeId = intent.getIntExtra("recipeId", -1);
        String title = intent.getStringExtra("recipeTitle");
        String description = intent.getStringExtra("recipeDescription");
        imagePath = intent.getStringExtra("recipeImagePath");

        titleEditText.setText(title);
        descriptionEditText.setText(description);
        if (imagePath != null) {
            Bitmap bitmap = BitmapUtils.loadBitmapFromFile(imagePath);
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
        }

        imageView.setOnClickListener(v -> {
            Intent selectImageIntent = new Intent(Intent.ACTION_PICK);
            selectImageIntent.setType("image/*");
            startActivityForResult(selectImageIntent, REQUEST_CODE_SELECT_IMAGE);
        });

        saveButton.setOnClickListener(v -> {
            String title1 = titleEditText.getText().toString();
            String description1 = descriptionEditText.getText().toString();
            Intent resultIntent = new Intent();
            resultIntent.putExtra("recipeIndex", recipeIndex);
            resultIntent.putExtra("recipeId", recipeId);
            resultIntent.putExtra("recipeTitle", title1);
            resultIntent.putExtra("recipeDescription", description1);
            resultIntent.putExtra("recipeImagePath", imagePath);
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        removeButton.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("recipeIndex", recipeIndex);
            resultIntent.putExtra("recipeId", recipeId);
            setResult(Activity.RESULT_FIRST_USER, resultIntent);
            finish();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK && data != null) {
            try {
                Bitmap bitmap = BitmapUtils.getBitmapFromIntent(this, data);
                imageView.setImageBitmap(bitmap);
                imagePath = BitmapUtils.saveBitmapToFile(this, bitmap, "recipe_image_" + System.currentTimeMillis() + ".png");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
