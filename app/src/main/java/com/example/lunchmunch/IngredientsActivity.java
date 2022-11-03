package com.example.lunchmunch;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Date;

public class IngredientsActivity extends AppCompatActivity implements IngredientItemFragment.OnFragmentInteractionListener {
    Button RecipesNav, MealPlanNav, ShoppingListNav;
    FirebaseFirestore db;
    CollectionReference IngrCollec;


    ListView ingredientsListView;
    FoodItemAdapter ingredientAdapter;
    ArrayList<Ingredient> dataList;
    Map<String, Ingredient> foodMap;
    IngredientItemFragment fragment;
    Integer itemPosition;
    Spinner sortSpinner;

    ArrayAdapter<String> sortAdapter;

    // Declare the variables so that you will be able to reference it later.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingredients_activity);



        // init firebase reference
        db = FirebaseFirestore.getInstance();
        IngrCollec = db.collection("Ingredients");

        initDBListener(IngrCollec);
        initViews();

        // ingredient item storage
        dataList = new ArrayList<Ingredient>(); // used for displaying list
        foodMap = new HashMap<String, Ingredient>(); // used for storing unique ingredients

        // ingredient lists
        ingredientsListView = (ListView) findViewById(R.id.ingredient_list);
        ingredientAdapter = new FoodItemAdapter(this, R.layout.content_ingredients, dataList);
        ingredientsListView.setAdapter(ingredientAdapter);

        // adding or editing a new item button
        final FloatingActionButton addFoodButton = findViewById(R.id.add_ingredient_button);
        fragment = new IngredientItemFragment();
        addFoodButton.setOnClickListener(view -> fragment.show(getSupportFragmentManager(), "ADD_INGREDIENT"));

        // Sorting Spinner
        sortSpinner = (Spinner) findViewById(R.id.SortOptions);
        sortAdapter = new ArrayAdapter<String>(IngredientsActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.sortOptions));
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(sortAdapter);


        // navbar listeners
        RecipesNav.setOnClickListener(view -> {
            startActivity(new Intent(IngredientsActivity.this, RecipeActivity.class));
        });

        MealPlanNav.setOnClickListener(view -> {
            startActivity(new Intent(IngredientsActivity.this, MealPlanActivity.class));
        });

        ShoppingListNav.setOnClickListener(view -> {
            startActivity(new Intent(IngredientsActivity.this, ShoppingListActivity.class));
        });

        //Open fragment when ingredient_list item is clicked
        ingredientsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemPosition = i;
                Bundle args = new Bundle();
                args.putParcelable("currentIngredient", dataList.get(itemPosition));
                args.putInt("currentIngredientPosition", itemPosition);
                fragment.setArguments(args);
                fragment.show(getSupportFragmentManager(), "ADD_INGREDIENT");
                view.refreshDrawableState();
            }
        });


        // Spinner Sorting Functionality
        // sets spinner to default
        sortSpinner.setSelection(0);

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String choice = sortSpinner.getSelectedItem().toString();
                if (choice.equals("Description")){
                    Toast.makeText(IngredientsActivity.this, "Selected " + choice, Toast.LENGTH_SHORT).show();
                    Collections.sort(dataList, new Comparator<Ingredient>() {
                        @Override
                        public int compare(Ingredient ingredient, Ingredient ingredient2) {
                            return ingredient.getDescription().compareTo(ingredient2.getDescription());
                        }
                    });

                } else if (choice.equals("Best Before")){
                    Toast.makeText(IngredientsActivity.this, "Selected " + choice, Toast.LENGTH_SHORT).show();
                    Collections.sort(dataList, new Comparator<Ingredient>() {
                        @Override
                        public int compare(Ingredient ingredient, Ingredient ingredient2) {
                            return ingredient.getBestBefore().compareTo(ingredient2.getBestBefore());
                        }
                    });

                } else if (choice.equals("Location")){
                    Toast.makeText(IngredientsActivity.this, "Selected " + choice, Toast.LENGTH_SHORT).show();
                    Collections.sort(dataList, new Comparator<Ingredient>() {
                        @Override
                        public int compare(Ingredient ingredient, Ingredient ingredient2) {
                            return ingredient.getLocation().compareTo(ingredient2.getLocation());
                        }
                    });
                    Collections.reverse(dataList);
                    ingredientAdapter.notifyDataSetChanged();


                } else if (choice.equals("Category")){
                    Toast.makeText(IngredientsActivity.this, "Selected " + choice, Toast.LENGTH_SHORT).show();
                    Collections.sort(dataList, new Comparator<Ingredient>() {
                        @Override
                        public int compare(Ingredient ingredient, Ingredient ingredient2) {
                            return ingredient.getCategory().compareTo(ingredient2.getCategory());
                        }
                    });
                }
                ingredientAdapter.notifyDataSetChanged();
                ingredientsListView.refreshDrawableState();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    public void onOkPressed(String name, String description, Date bestBefore, Location location, Integer count, Integer cost, IngredientCategory category) {
        System.out.println(bestBefore);

        Ingredient newIngredient = new Ingredient(name, description, bestBefore, location, count, cost, category);

        // add the new food to our current ingr list if new
        if (!foodMap.containsKey(name)) {
            dataList.add(newIngredient);
            sortSpinner.setSelection(0);
            ingredientAdapter.notifyDataSetChanged();

        }
        foodMap.put(newIngredient.getName(), newIngredient);
        // update ingr list in db by overwriting it with the current ingredientsList
        // restructured db to have list of collections instead of one collection
        IngrCollec.document(name).set(newIngredient) // .add equiv to .collec().set(..)
                .addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        System.out.println("Success");
//                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Fail");
//                        Log.w(TAG, "Error adding document", e);
                    }
                });

        // Delete Food obj (delete from ingredientsList then run add code above (this will overwrite the list in the db)

        // Edit Food obj (edit from ingriendsList then same as above ^^)
    }

    @Override
    public void deleteIngredient() {
        if (itemPosition == null) {
            return;
        }
        String name = dataList.get(itemPosition).getName();

        Log.d("ITEM POSITION", "Position is: " + String.valueOf(itemPosition));
        if (foodMap.containsKey(name)) {
            foodMap.remove(name);
            dataList.clear();
            dataList.addAll(foodMap.values());
            ingredientAdapter.notifyDataSetChanged();

        }
        else { return; }
        IngrCollec.document(name).delete()// .add equiv to .collec().set(..)
                .addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        System.out.println("Success");
                        //Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Fail");
                        //Log.w(TAG, "Error adding document", e);
                    }
                });

    }


    private void initDBListener(CollectionReference ingrCollec) {

        ingrCollec.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    // clear current list and re-add all the current ingredients
                    dataList.clear();
                    // each document in the Ingredients collection is an Ingredient class object
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        Timestamp timestamp = (Timestamp) document.getData().get("bestBefore");
                        Date bestBefore;
                        if (timestamp == null) {
                            bestBefore = null;
                        }else{
                            bestBefore = timestamp.toDate();
                        }

                        Ingredient ingredient = new Ingredient(
                                (String) document.getData().get("name"),
                                (String) document.getData().get("description"),
                                bestBefore,
                                Location.valueOf(document.getData().get("location").toString().toUpperCase()),
                                ((Long) document.getData().get("count")).intValue(),
                                ((Long) document.getData().get("cost")).intValue(),
                                IngredientCategory.valueOf(document.getData().get("category").toString().toUpperCase())
                        );


                        //Ingredient ingredient = document.toObject(Ingredient.class);


                        //TODO: (Maxym) Add ingredients to sharedPreferences
                        foodMap.put(ingredient.getName(), ingredient);
                        dataList.add(ingredient);

                    }
                    ingredientAdapter.notifyDataSetChanged();
                }
            }
        });
    }


    private void initViews() {
        RecipesNav = findViewById(R.id.recipesNav);
        MealPlanNav = findViewById(R.id.mealPlanNav);
        ShoppingListNav = findViewById(R.id.shoppingListNav);
    }


}

