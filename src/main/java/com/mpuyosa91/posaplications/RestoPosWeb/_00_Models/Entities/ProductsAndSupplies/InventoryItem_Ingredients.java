package com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.ProductsAndSupplies;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;

@Entity
@AssociationOverrides({
        @AssociationOverride(name = "pk.inventoryItem",
                joinColumns = @JoinColumn(name = "inventory_item_id")),
        @AssociationOverride(name = "pk.ingredient",
                joinColumns = @JoinColumn(name = "ingredient_id"))})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "pk")
public class InventoryItem_Ingredients {

    private InventoryItem_Ingredients_Id pk;
    private InventoryItem.MeasureType    type;
    private Double                       quantity;

    public InventoryItem_Ingredients(InventoryItem inventoryItem, InventoryItem ingredient) {
        pk = new InventoryItem_Ingredients_Id();
        pk.setIngredient(ingredient);
        pk.setInventoryItem(inventoryItem);
    }

    @Transient
    public InventoryItem getInventoryItem() {
        return getPk().getInventoryItem();
    }

    public void setInventoryItem(InventoryItem inventoryItem) {
        getPk().setInventoryItem(inventoryItem);
    }

    @Transient
    public InventoryItem getComponent() {
        return getPk().getIngredient();
    }

    public void setComponent(InventoryItem component) {
        getPk().setIngredient(component);
    }

    public InventoryItem.MeasureType getType() {
        return type;
    }

    public void setType(InventoryItem.MeasureType type) {
        this.type = type;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    @Override
    public int hashCode() {
        return (getPk() != null ? getPk().hashCode() : 0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        InventoryItem_Ingredients that = (InventoryItem_Ingredients) o;
        return getPk() != null ? getPk().equals(that.getPk()) : that.getPk() == null;
    }

    @EmbeddedId
    private InventoryItem_Ingredients_Id getPk() {
        return pk;
    }

    public void setPk(InventoryItem_Ingredients_Id pk) {
        this.pk = pk;
    }
}
