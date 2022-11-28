package com.example.lunchmunch;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Main activity for all Ingredients functionality
 */
public class IngredientsActivity extends AppCompatActivity implements IngredientItemFragment.OnFragmentInteractionListener {
    LinearLayout RecipesNav, MealPlanNav, ShoppingListNav;
    FirebaseFirestore db;
    CollectionReference IngrCollec;

    ListView ingredientsListView;
    FoodItemAdapter ingredientAdapter;
    /**
     * Array list of Ingredient instances for use in array adapter
     */
    static ArrayList<Ingredient> ingredientsList;
    /**
     * Map of Ingredient instances for uniqueness
     */
    //Map<String, Ingredient> foodMap;

    Integer itemPosition;
    /**
     * Please see the {@link com.example.lunchmunch.IngredientItemFragment} class for true identity
     */
    IngredientItemFragment fragment;
    IngredientItemFragment fragment2;
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
        ingredientsList = new ArrayList<Ingredient>(); // used for displaying list
        //foodMap = new HashMap<String, Ingredient>(); // used for storing unique ingredients

        // ingredient lists
        ingredientsListView = (ListView) findViewById(R.id.ingredient_list);
        ingredientAdapter = new FoodItemAdapter(this, R.layout.content_ingredients, ingredientsList);
        ingredientsListView.setAdapter(ingredientAdapter);

        // adding or editing a new item button
        final FloatingActionButton addFoodButton = findViewById(R.id.add_ingredient_button);
        fragment = new IngredientItemFragment();
        fragment2 = new IngredientItemFragment();
        addFoodButton.setOnClickListener(view -> fragment2.show(getSupportFragmentManager(), "ADD_INGREDIENT"));

        // Sorting Spinner
        sortSpinner = (Spinner) findViewById(R.id.SortOptions);
        sortAdapter = new ArrayAdapter<String>(IngredientsActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.sortOptionsI));
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
                args.putParcelable("currentIngredient", ingredientsList.get(itemPosition));
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
                Sort.ingredientSort(ingredientsList, choice);
                ingredientAdapter.notifyDataSetChanged();
                ingredientsListView.refreshDrawableState();

                if(!choice.equals("Sort By")) {
                    Toast toast = Toast.makeText(IngredientsActivity.this, "Now sorting by " + choice, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
    /**
    * Inserts an Ingredient into database or edits existing ingredient
    *   @param ingredient
     *      ingredient to edit or add
     * @param position
     *      position of existing ingredient
    */
    @Override
    public void onOkPressed(Ingredient ingredient, int position) {

        sortSpinner.setSelection(0);
        // dont need this as db call below already overwrites
        /*if (position != -1) {
            //Our ingredient is not new, so we need to update it
            ingredientsList.set(position, ingredient);
            //foodMap.put(ingredient.getName(), ingredient);
            ingredientAdapter.notifyDataSetChanged();

            //Update the ingredient in the database
            IngrCollec.document(ingredient.getName()).set(ingredient);
            return;
        }

         */

        // add the new food to our current ingr list if new

            /*if (!foodMap.containsKey(ingredient.getName())) {
                ingredientsList.add(ingredient);

                ingredientAdapter.notifyDataSetChanged();

            }
            foodMap.put(ingredient.getName(), ingredient);

             */
            // update ingr list in db by overwriting it with the current ingredientsList
            // restructured db to have list of collections instead of one collection
            IngrCollec.document(ingredient.getName()).set(ingredient) // .add equiv to .collec().set(..)
                    .addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            System.out.println("Success");
                            // incase already exists and we are just editing //removing if doesnt exist wont cause any errors
                            if (position == -1) {
                                ingredientsList.add(ingredient);
                                ingredientAdapter.notifyDataSetChanged();
                            }
                            else {
                                ingredientsList.set(position, ingredient);
                                ingredientAdapter.notifyDataSetChanged();
                            }
//                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            System.out.println("Fail");
                            Toast.makeText(getApplicationContext(),"Failed to upload "+ingredient.getName()+ " to database, try again",Toast.LENGTH_LONG).show();
//                        Log.w(TAG, "Error adding document", e);
                        }
                    });

            // Delete Food obj (delete from ingredientsList then run add code above (this will overwrite the list in the db)

            // Edit Food obj (edit from ingriendsList then same as above ^^)
        }

     /**
     * Deletes an Ingredient from database
     */
    @Override
    public void deleteIngredient() {
        if (itemPosition == null) {
            return;
        }
        Ingredient ingredient = ingredientsList.get(itemPosition);


        Log.d("ITEM POSITION", "Position is: " + String.valueOf(itemPosition));
        /*
        if (foodMap.containsKey(name)) {
            foodMap.remove(name);
            ingredientsList.clear();
            ingredientsList.addAll(foodMap.values());
            ingredientAdapter.notifyDataSetChanged();

        }
        else { return; }*/

        //boolean ingrExists = IngredientsActivity.ingredientsList.stream().map(Ingredient::getName).anyMatch(ingrName::equals);



        IngrCollec.document(ingredient.getName()).delete()// .add equiv to .collec().set(..)
                .addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        System.out.println("Success");
                        // this line should be ran in initDB when db updates from noticing change but add here incase
                        ingredientsList.remove(ingredient);
                        ingredientAdapter.notifyDataSetChanged();
                        //Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Fail");
                        Toast.makeText(getApplicationContext(),"Failed to delete ingredient from database, please try again.",Toast.LENGTH_LONG).show();
                    }
                });

    }


    private void initDBListener(CollectionReference ingrCollec) {

        ingrCollec.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    // clear current list and re-add all the current ingredients
                    ingredientsList.clear();
                    // each document in the Ingredients collection is an Ingredient class object
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        Timestamp timestamp = (Timestamp) document.getData().get("bestBefore");
                        Date bestBefore;
                        if (timestamp == null) {
                            bestBefore = null;
                        }else{
                            bestBefore = timestamp.toDate();
                        }

                        Float cost = new Float(0);
                        Float count = new Float(0);
                        if (document.getData().get("cost") instanceof Double) {
                            cost = ((Double) document.getData().get("cost")).floatValue();

                        } else {
                            cost = ((Long) document.getData().get("cost")).floatValue();

                        }

                        if (document.getData().get("count") instanceof Double) {
                            count = ((Double) document.getData().get("count")).floatValue();

                        } else {
                            count = ((Long) document.getData().get("count")).floatValue();

                        }

                        Ingredient ingredient = new Ingredient(
                                (String) document.getData().get("name"),
                                (String) document.getData().get("description"),
                                bestBefore,
                                Location.valueOf(document.getData().get("location").toString().toUpperCase()),
                                count,
                                cost,
                                IngredientCategory.valueOf(document.getData().get("category").toString().toUpperCase())
                        );


                        //Ingredient ingredient = document.toObject(Ingredient.class);



                        //foodMap.put(ingredient.getName(), ingredient);
                        ingredientsList.add(ingredient);

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

