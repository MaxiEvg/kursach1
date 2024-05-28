package com.kursovaya.receptorganaizer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class RecipeDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "recipes.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_RECIPES = "recipes";
    private static final String COLUMN_ID = "_id";  // Renamed to _id
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_IMAGE_PATH = "image_path";

    public RecipeDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_RECIPES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_IMAGE_PATH + " TEXT" + ")";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        onCreate(db);
    }

    public void addRecipe(Recipe recipe) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, recipe.getTitle());
        values.put(COLUMN_DESCRIPTION, recipe.getDescription());
        values.put(COLUMN_IMAGE_PATH, recipe.getImagePath());

        db.insert(TABLE_RECIPES, null, values);
        db.close();
    }

    public void updateRecipe(Recipe recipe) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, recipe.getTitle());
        values.put(COLUMN_DESCRIPTION, recipe.getDescription());
        values.put(COLUMN_IMAGE_PATH, recipe.getImagePath());

        db.update(TABLE_RECIPES, values, COLUMN_ID + " = ?", new String[]{String.valueOf(recipe.getId())});
        db.close();
    }

    public void deleteRecipe(int recipeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RECIPES, COLUMN_ID + " = ?", new String[]{String.valueOf(recipeId)});
        db.close();
    }

    public ArrayList<Recipe> getAllRecipes() {
        ArrayList<Recipe> recipes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_RECIPES, null);

        if (cursor.moveToFirst()) {
            do {
                Recipe recipe = new Recipe();
                recipe.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                recipe.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)));
                recipe.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
                recipe.setImagePath(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_PATH)));
                recipes.add(recipe);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return recipes;
    }
}
