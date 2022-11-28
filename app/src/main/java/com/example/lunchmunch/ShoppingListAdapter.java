package com.example.lunchmunch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Custom Adapter for the RecyclerView used in the ShoppingList activity
 * Displays each ingredient and allows them to interact with each one by checking the purchased checkbox
 */
public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder> {

    private ArrayList<Ingredient> dataList;
    Context context;

    private ingrPurchasedListener mIngrPurchasedListener;

    /**
     * set click listener
     * this allows us to initialize the interface click handler that sends the ingredient info from the specific view that got clicked to the ShoppingListActivity
     * sets our global var mIngrPurchasedListener to the value passed in {iPL}
     * @param iPL
     */
    public void setIngrPurchasedListener(ingrPurchasedListener iPL) {
        mIngrPurchasedListener = iPL;
    }

    /**
     * The interface click handler for each item in our recyclerview
     * see {setIngrPurchasedListener()} for extra information
     */
    public interface ingrPurchasedListener {
        /**
         * The needed method for the interface
         * allows the value of the ingredient instance displayed in the view clicked and its index
         * @param ingredient
         * @param ingrIdx
         */
        void ingrPurchasedBtnClicked(Ingredient ingredient, Integer ingrIdx);
    }


    /**
     * Contructor for the RecyclerView Adapter
     * Take in the context of the activity {context}
     * Take in an ArrayList of the ingredients we would like to display
     * @param context
     * @param objects
     */
    public ShoppingListAdapter(@NonNull Context context, @NonNull ArrayList<Ingredient> objects) {
        this.dataList = objects;
        this.context = context;
    }

    /**
     * Initializes the view for each content bar/item/ingredient in the recyclerview
     * returns the inflated view that has the xml for each view/bar (shopping_list_content)
     * @param parent
     * @param viewType
     * @return returns the viewholder that has the needed xml inputs to define and interact with
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_list_content, null);
        RecyclerView.ViewHolder holder = new ViewHolder(v);
        return (ViewHolder) holder;
    }


    /**
     * On each view being populated take the info for each bar and display its values into the xml frontend components
     * set the display and add the functionality for the purchashed checkbox @see {ingrPurchasedListener} javadoc for more info
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ingredient ingredient = dataList.get(position);

        holder.tvName.setText(ingredient.getName());
        holder.tvCost.setText(String.valueOf(ingredient.getCost()));
        holder.tvCount.setText(String.valueOf(ingredient.getCount()));
        holder.tvCategory.setText(String.valueOf(ingredient.getCategory()));
        holder.tvDescription.setText(ingredient.getDescription());

        // init to always be unchecked (for the case when user clicks on listview to only edit quant)
        holder.ingrPurchasedBtn.setChecked(false);

        holder.ingrPurchasedBtn.setOnClickListener(view -> {
            mIngrPurchasedListener.ingrPurchasedBtnClicked(ingredient, position);
        });
    }

    /**
     * Standard nececcary item counter for the recycler view
     * returns the total count of the amount of views needed/ingredients in the shoppinglist to be displayed
     * To avoid null pointer error return zero if we have not added any elements and the dataList is still null (not yet initialized)
     * @return returns the amount of ingredients in the dataList
     */
    @Override
    public int getItemCount() {
        if (dataList != null) {
            return dataList.size();
        } else return 0;
    }

    /**
     * ViewHolder class that extends the RecyclerView.ViewHolder class
     * This allows us to define the needed frontend xml variables we will be using in each view
     * Defining the ViewHolder returned above @see {onCreateViewHolder}
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvCount;
        TextView tvCost;
        TextView tvCategory;
        TextView tvDescription;
        CheckBox ingrPurchasedBtn;

        /**
         * The initialized ViewHolder of each bar in the recyclerview
         * Assigns our defined needed frontend variables to the actual xml inputs so we can interact with the user input
         * said xml input is inputted as itemView
         * @param itemView
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.name_label);
            tvCount = itemView.findViewById(R.id.count_label);
            tvCost = itemView.findViewById(R.id.cost_label);
            tvCategory = itemView.findViewById(R.id.category_label);
            ingrPurchasedBtn = itemView.findViewById(R.id.ingrPurchasedBtn);
            tvDescription = itemView.findViewById(R.id.description_label);
        }
    }
}

