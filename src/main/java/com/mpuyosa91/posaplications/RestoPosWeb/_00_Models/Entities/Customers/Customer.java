package com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Customers;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Accounting.Bill;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.ProductsAndSupplies.InventoryItem;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.ProductsAndSupplies.SalableItem;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Site;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "customer_type")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Customer implements ICustomer {

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    @Where(clause = "closed=false and billed=true")
    Set<SalableItem> itemListBilled   = new HashSet<>();
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    @Where(clause = "closed=false and billed=false")
    Set<SalableItem> itemListUnBilled = new HashSet<>();
    String identifier = ICustomer.CustomerTypes.GenericCustomer.getShowableName();

    @Id
    @GeneratedValue(generator = "uuid")
    @Column(columnDefinition = "BINARY(16)")
    private UUID    id;
    @ManyToOne
    @JoinColumn(name = "site_id")
    private Site    site;
    private boolean enabled = true;

    @OneToOne
    @JoinColumn(name = "current_bill_id")
    private Bill     currentBill;
    private double   consumption;
    private int      orderNum;
    private Calendar dateTimeStart;
    private Calendar dateTimeFinal;

    Customer() {
        consumption = calculateConsumption();
        orderNum = 1;
        dateTimeFinal = null;
        dateTimeStart = null;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public boolean isOccupied() {
        return getPreConsumption() != 0.0;
    }

    public SalableItem addItem(InventoryItem inventoryItem, String notes) {
        if (dateTimeStart == null) {
            dateTimeStart = Calendar.getInstance();
        }
        if (currentBill == null) {
            currentBill = new Bill();  // TODO: Crear un customer como se debe
        }
        SalableItem salableItem = new SalableItem();
        salableItem.setBill(this.getCurrentBill());
        salableItem.setCustomer(this);
        salableItem.setInventoryItem(inventoryItem);
        salableItem.setNotes(notes);
        itemListUnBilled.add(salableItem);
        return salableItem;
    }

    public void billAllItems() {
        if (!itemListUnBilled.isEmpty()) {
            for (SalableItem item : itemListUnBilled) {
                item.setBilled(true);
                item.setOrderTime(Calendar.getInstance());
            }
            itemListBilled.addAll(itemListUnBilled);
            itemListUnBilled = new HashSet<>();
        }
        consumption = calculateConsumption();
    }

    public boolean removeItem(SalableItem item) {
        try {
            return itemListUnBilled.remove(item);
        } catch (IndexOutOfBoundsException ignored) {
            return false;
        }
    }

    public Set<SalableItem> getItemListUnBilled() {
        return itemListUnBilled;
    }

    public void setItemListUnBilled(Set<SalableItem> itemListUnBilled) {
        this.itemListUnBilled = itemListUnBilled;
    }

    public Set<SalableItem> getItemListBilled() {
        return itemListBilled;
    }

    public void incrementOrderNum() {
        orderNum++;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public double evacuate() {
        billAllItems();
        for (SalableItem item : itemListBilled) {
            if (!item.isDelivered())
                item.setDelivered(true);
            if (item.getReadyTime() == null)
                item.setReadyTime(Calendar.getInstance());
            if (item.getDeliveryTime() == null)
                item.setDeliveryTime(Calendar.getInstance());
        }
        orderNum = 1;
        dateTimeFinal = Calendar.getInstance();  // TODO: Los bills son los que llevan el tiempo, no el customer
        currentBill = null;
        return getConsumption();
    }

    public Calendar getDateTimeFinal() {
        return dateTimeFinal;
    }

    public void setDateTimeFinal(Calendar dateTimeFinal) {
        this.dateTimeFinal = dateTimeFinal;
    }

    public Calendar getDateTimeStart() {
        return dateTimeStart;
    }

    public void setDateTimeStart(Calendar dateTimeStart) {
        this.dateTimeStart = dateTimeStart;
    }

    public long getDurationInSeconds() {
        long r = 0;
        if (dateTimeStart != null) {
            if (dateTimeFinal != null) {
                r = (dateTimeFinal.getTimeInMillis() - dateTimeStart.getTimeInMillis()) / 1000;
            } else {
                r = (Calendar.getInstance().getTimeInMillis() - dateTimeStart.getTimeInMillis()) / 1000;
            }
        }
        return r;
    }

    public double getConsumption() {
        return consumption;
    }

    public void setConsumption(double consumption) {
        this.consumption = consumption;
    }

    public double getPreConsumption() {
        double r = calculateConsumption();
        for (SalableItem item :
                itemListUnBilled) {
            r += item.getPrice();
        }
        return r;
    }

    public void setItemListBilled(Set<SalableItem> itemListBilled) {
        this.itemListBilled = itemListBilled;
    }

    public Bill getCurrentBill() {
        return currentBill;
    }

    public void setCurrentBill(Bill currentBill) {
        this.currentBill = currentBill;
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

    private double calculateConsumption() {
        double r = 0;
        for (SalableItem item :
                itemListBilled) {
            r += item.getPrice();
        }
        return r;
    }
}
