package com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Customers;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Accounting.Bill;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Crew.User;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.ProductsAndSupplies.InventoryItem;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.ProductsAndSupplies.SalableItem;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Site;
import org.hibernate.annotations.Where;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

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
    @Where(clause = "delivered=false and billed=true")
    Set<SalableItem> itemListBilled;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    @Where(clause = "delivered=false and billed=false")
    Set<SalableItem> itemListUnBilled;
    String identifier = ICustomer.CustomerTypes.GenericCustomer.getShowableName();
    @Value("${server.port}")
    private Integer server_port;
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
    private Bill   currentBill;
    private double consumption;
    private int    orderNum = 1;

    Customer() {
        consumption = calculateConsumption();
        orderNum = 1;
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
        return getPreConsumption() != 0.0 || currentBill != null;
    }

    public SalableItem addItem(User user, InventoryItem inventoryItem, String notes) {

        if (this.getCurrentBill() == null) {

            Integer consecutive = (new RestTemplate()).getForObject(
                    "http://localhost:" + server_port.toString() +
                            "/bill/consecutive/" + site.getId().toString(),
                    Integer.class
            );

            Bill bill = new Bill();
            if (consecutive != null && consecutive > 0) bill.setConsecutive(consecutive);
            else bill.setConsecutive(1);
            bill.setSite(getSite());
            bill.addCustomer(this);
            bill.setDateTimeStart(Calendar.getInstance());
            this.setCurrentBill(bill);

        }

        SalableItem salableItem = new SalableItem(inventoryItem, notes);
        salableItem.setUser(user);
        itemListUnBilled.add(salableItem);
        return salableItem;

    }

    public void billAllItems() {

        if (!itemListUnBilled.isEmpty()) {

            for (SalableItem item : itemListUnBilled) {

                item.setBill(this.getCurrentBill());
                item.setBilled(true);
                item.setOrderTime(Calendar.getInstance());

                getCurrentBill().addItem(item);
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
        if (itemListUnBilled == null)
            this.setItemListUnBilled(new HashSet<>());
        return itemListUnBilled;
    }

    public void setItemListUnBilled(Set<SalableItem> itemListUnBilled) {
        this.itemListUnBilled = itemListUnBilled;
    }

    public Set<SalableItem> getItemListBilled() {
        if (itemListBilled == null)
            this.setItemListBilled(new HashSet<>());
        return itemListBilled;
    }

    public void incrementOrderNum() {
        orderNum++;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public Bill evacuate() {
        if (itemListUnBilled.isEmpty()) {
            for (SalableItem item : itemListBilled) {
                if (!item.isDelivered())
                    item.setDelivered(true);
                if (item.getReadyTime() == null)
                    item.setReadyTime(Calendar.getInstance());
                if (item.getDeliveryTime() == null)
                    item.setDeliveryTime(Calendar.getInstance());
            }
            currentBill.setDateTimeFinal(Calendar.getInstance());
            return currentBill;
        } else {
            return null;
        }
    }

    public Calendar getDateTimeFinal() {
        return currentBill.getDateTimeFinal();
    }

    public Calendar getDateTimeStart() {
        return currentBill.getDateTimeStart();
    }

    public long getDurationInSeconds() {
        return currentBill.getDurationInSeconds();
    }

    public double getConsumption() {
        return consumption;
    }

    public double getPreConsumption() {
        double r = calculateConsumption();
        for (SalableItem item : getItemListUnBilled()) {
            r += item.getPrice();
        }
        return r;
    }

    public void setItemListBilled(Set<SalableItem> itemListBilled) {
        this.itemListBilled = itemListBilled;
    }

    public void clean() {
        orderNum = 1;
        currentBill = null;
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
        if (getCurrentBill() != null)
            r = getCurrentBill().calculateConsumption();
        return r;
    }
}
