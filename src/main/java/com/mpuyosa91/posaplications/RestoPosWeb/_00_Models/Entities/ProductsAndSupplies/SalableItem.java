package com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.ProductsAndSupplies;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Accounting.Bill;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Crew.User;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Customers.Customer;

import javax.persistence.*;
import java.util.Calendar;
import java.util.UUID;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class SalableItem {

    @Id
    @GeneratedValue(generator = "uuid")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "bill_id")
    private Bill bill;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String notes;

    @Enumerated(EnumType.STRING)
    private InventoryItem.Type type = InventoryItem.Type.RawFood;
    private String             name;
    private Integer            serial;
    private double             price;

    private boolean billed    = false;
    private boolean delivered = false;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Calendar orderTime;
    private Calendar readyTime;
    private Calendar deliveryTime;

    public SalableItem() {
    }

    public SalableItem(InventoryItem inventoryItem, String notes) {
        this.notes = notes;
        this.setType(inventoryItem.getType());
        this.setName(inventoryItem.getName());
        this.setSerial(inventoryItem.getSerial());
        this.setPrice(inventoryItem.getPrice());
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public InventoryItem.Type getType() {
        return type;
    }

    public void setType(InventoryItem.Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isBilled() {
        return billed;
    }

    public void setBilled(boolean billed) {
        this.billed = billed;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public Calendar getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Calendar orderTime) {
        this.orderTime = orderTime;
    }

    public Calendar getReadyTime() {
        return readyTime;
    }

    public void setReadyTime(Calendar readyTime) {
        this.readyTime = readyTime;
    }

    public Calendar getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Calendar deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    @Override
    public String toString() {
        return "SalableItem{" +
                "id=" + id +
                ", bill=" + bill +
                ", customer=" + customer +
                ", user=" + user +
                ", notes='" + notes + '\'' +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", serial=" + serial +
                ", price=" + price +
                ", billed=" + billed +
                ", delivered=" + delivered +
                ", orderTime=" + orderTime +
                ", readyTime=" + readyTime +
                ", deliveryTime=" + deliveryTime +
                '}';
    }
}
