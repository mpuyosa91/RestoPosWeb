package com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Accounting;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Customers.Customer;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.ProductsAndSupplies.SalableItem;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Site;

import javax.persistence.*;
import java.util.*;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(indexes = {@Index(name = "bill_site_id_consecutive_id_idx", columnList = "site_id,consecutive")})
public class Bill {

    @Id
    @GeneratedValue(generator = "uuid")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    private long consecutive;

    @ManyToOne
    @JoinColumn(name = "site_id")
    private Site site;

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL)
    private Set<SalableItem> salableItems;

    @OneToMany(mappedBy = "pk.bill", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Bill_User> bill_users;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "customer_bill",
            joinColumns = {@JoinColumn(name = "customer_id")},
            inverseJoinColumns = {@JoinColumn(name = "bill_id")})
    private Set<Customer> customers;

    private Double   consumption;
    private Calendar dateTimeStart;
    private Calendar dateTimeFinal;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public long getConsecutive() {
        return consecutive;
    }

    public void setConsecutive(long consecutive) {
        this.consecutive = consecutive;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public Set<SalableItem> getSalableItems() {
        return salableItems;
    }

    public void setSalableItems(Set<SalableItem> salableItems) {
        this.salableItems = salableItems;
    }

    public Map<UUID, Integer> getSalableQuantityMap() {
        Map<UUID, Integer> map = new HashMap<>();

        for (SalableItem salableItem : getSalableItems()) {

            UUID uuid     = salableItem.getId();
            int  quantity = 1;
            if (map.containsKey(uuid)) quantity = map.get(uuid) + 1;

            map.put(uuid, quantity);

        }

        return map;
    }

    public Map<UUID, SalableItem> getSalableItemMap() {
        Map<UUID, SalableItem> map = new HashMap<>();

        for (SalableItem salableItem : getSalableItems()) {

            UUID uuid = salableItem.getId();
            map.put(uuid, salableItem);

        }

        return map;
    }

    public void calculateConsumption() {
        double total_consumption = 0;

        for (Map.Entry<UUID, Integer> entry : this.getSalableQuantityMap().entrySet()) {

            SalableItem salableItem = this.getSalableItemMap().get(entry.getKey());

            total_consumption += salableItem.getPrice() * entry.getValue();
        }
        setConsumption(total_consumption);
    }

    public Set<Bill_User> getBill_users() {
        if (bill_users == null) {
            this.setBill_users(new HashSet<>());
        }
        return bill_users;
    }

    public void setBill_users(Set<Bill_User> bill_users) {
        this.bill_users = bill_users;
    }

    public Set<Customer> getCustomers() {
        if (customers == null) {
            this.setCustomers(new HashSet<>());
        }
        return customers;
    }

    public void setCustomers(Set<Customer> customers) {
        this.customers = customers;
    }

    public Double getConsumption() {
        return consumption;
    }

    public void setConsumption(Double consumption) {
        this.consumption = consumption;
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

    public Calendar getDateTimeStart() {
        return dateTimeStart;
    }

    public void setDateTimeStart(Calendar dateTimeStart) {
        this.dateTimeStart = dateTimeStart;
    }

    public Calendar getDateTimeFinal() {
        return dateTimeFinal;
    }

    public void setDateTimeFinal(Calendar dateTimeFinal) {
        this.dateTimeFinal = dateTimeFinal;
    }
}
