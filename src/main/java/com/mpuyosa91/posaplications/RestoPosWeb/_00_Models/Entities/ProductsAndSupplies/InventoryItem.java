package com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.ProductsAndSupplies;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Site;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(
        indexes = {@Index(name = "inventory_item_serial_site_idx", columnList = "serial,site_id")},
        uniqueConstraints = {@UniqueConstraint(columnNames = {"serial", "site_id"})}
)
public class InventoryItem {

    @Id
    @GeneratedValue(generator = "uuid")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @JoinColumn(name = "serial")
    private Integer serial;

    @ManyToOne
    @JoinColumn(name = "site_id")
    @JsonBackReference
    private Site    site;
    private boolean enabled = true;

    @OneToMany(mappedBy = "pk.inventoryItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<InventoryItem_Ingredients> composition = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private Type    type       = Type.RawFood;
    private boolean final_item = true;
    private String  name;
    private double  price;

    @Enumerated(EnumType.STRING)
    private MeasureType measureType = MeasureType.Units;
    private double      volume      = 0;
    private double      weight      = 0;
    private double      units       = 1;

    public void copyFrom(InventoryItem inventoryItem) {
        setSite(inventoryItem.site);
        setEnabled(inventoryItem.enabled);
        setComposition(inventoryItem.composition);
        setType(inventoryItem.type);
        setFinal_item(inventoryItem.final_item);
        setName(inventoryItem.name);
        setPrice(inventoryItem.price);
        setMeasureType(inventoryItem.measureType);
        setVolume(inventoryItem.volume);
        setWeight(inventoryItem.weight);
        setUnits(inventoryItem.units);
    }

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

    public Set<InventoryItem_Ingredients> getComposition() {
        return composition;
    }

    public void setComposition(Set<InventoryItem_Ingredients> composition) {
        this.composition.addAll(composition);
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {

        if (serial != null) {
            int firstDigit = Integer.parseInt(Integer.toString(serial).substring(0, 1));
            switch (firstDigit) {
                case 1:
                    if (getType() != Type.RawFood)
                        setType(Type.RawFood);
                    break;
                case 2:
                    if (getType() != Type.Mixture)
                        setType(Type.Mixture);
                    break;
                case 3:
                    if (getType() != Type.Product)
                        setType(Type.Product);
                    break;
                case 4:
                    if (getType() != Type.MenuPlate)
                        setType(Type.MenuPlate);
                    break;
                default:
                    if (getType() != Type.RawFood)
                        setType(Type.RawFood);
            }
        }
        this.serial = serial;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        if (type != null && serial != null) {
            String fistDigit = "9";
            switch (type) {
                case RawFood:
                    fistDigit = "1";
                    break;
                case Mixture:
                    fistDigit = "2";
                    break;
                case Product:
                    fistDigit = "3";
                    break;
                case MenuPlate:
                    fistDigit = "4";
                    break;
            }
            String serial_string = Integer.toString(serial);
            serial_string = fistDigit.concat(serial_string.substring(1));
            if (getSerial() != Integer.parseInt(serial_string))
                setSerial(Integer.parseInt(serial_string));
        }
        this.type = type;
    }

    public boolean isFinal_item() {
        return final_item;
    }

    public void setFinal_item(boolean final_item) {
        this.final_item = final_item;
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

    public void setQuantity(Double quantity) {
        if (measureType == null)
            setMeasureType(MeasureType.Units);
        switch (measureType) {
            case Weight:
                weight = quantity;
            case Volume:
                volume = quantity;
            case Units:
                units = quantity;
        }
    }

    @Override
    public String toString() {
        return "InventoryItem{" +
                "id=" + id +
                ", serial=" + serial +
                ", site=" + site +
                ", enabled=" + enabled +
                ", composition=" + composition +
                ", type=" + type +
                ", final_item=" + final_item +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", measureType=" + measureType +
                ", volume=" + volume +
                ", weight=" + weight +
                ", units=" + units +
                '}';
    }

    enum MeasureType {Weight, Volume, Units}


    public enum Type {RawFood, Mixture, Product, MenuPlate}
}
