package com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.ProductsAndSupplies;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class InventoryItem_Ingredients_Id implements Serializable {

    private InventoryItem inventoryItem;
    private InventoryItem ingredient;

    @Override
    public int hashCode() {
        return Objects.hash(inventoryItem, ingredient);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;

        if (object == null || getClass() != object.getClass())
            return false;

        InventoryItem_Ingredients_Id that = (InventoryItem_Ingredients_Id) object;
        return Objects.equals(inventoryItem, that.inventoryItem) &&
                Objects.equals(ingredient, that.ingredient);
    }

    @ManyToOne
    InventoryItem getInventoryItem() {
        return inventoryItem;
    }

    void setInventoryItem(InventoryItem inventoryItem) {
        this.inventoryItem = inventoryItem;
    }

    @ManyToOne
    InventoryItem getIngredient() {
        return ingredient;
    }

    void setIngredient(InventoryItem ingredient) {
        this.ingredient = ingredient;
    }
}