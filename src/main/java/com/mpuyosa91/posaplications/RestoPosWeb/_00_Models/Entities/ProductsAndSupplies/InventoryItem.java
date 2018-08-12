package com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.ProductsAndSupplies;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Site;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class InventoryItem {

    @Id
    @GeneratedValue(generator = "uuid")
    @Column(columnDefinition = "BINARY(16)")
    private UUID    id;
    private Integer serial;

    @ManyToOne
    @JoinColumn(name = "site_id")
    private Site    site;
    private boolean enabled = true;

    @OneToMany(mappedBy = "pk.inventoryItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<InventoryItem_Ingredients> ingredients = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private Type    type = Type.RawFood;
    private boolean is_final;
    private String  name;
    private double  price;

    @Enumerated(EnumType.STRING)
    private MeasureType measureType = MeasureType.Units;
    private double      volume      = 0;
    private double      weight      = 0;
    private double      units       = 1;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<InventoryItem_Ingredients> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Set<InventoryItem_Ingredients> ingredients) {
        this.ingredients = ingredients;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean isIs_final() {
        return is_final;
    }

    public void setIs_final(boolean is_final) {
        this.is_final = is_final;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getUnits() {
        return units;
    }

    public void setUnits(double units) {
        this.units = units;
    }

    public MeasureType getMeasureType() {
        return measureType;
    }

    public void setMeasureType(MeasureType measureType) {
        this.measureType = measureType;
    }

    public Double getQuantity() {
        switch (measureType) {
            case Weight:
                return weight;
            case Volume:
                return volume;
            case Units:
                return units;
            default:
                return 0.0;
        }
    }

    enum MeasureType {Weight, Volume, Units}


    enum Type {RawFood, Mixture, Product, MenuPlate}

}
