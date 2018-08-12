package com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Accounting;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Customers.Customer;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.ProductsAndSupplies.SalableItem;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Site;

import javax.persistence.*;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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
    private Calendar datetimeBilling;

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

    public Calendar getDatetimeBilling() {
        return datetimeBilling;
    }

    public void setDatetimeBilling(Calendar datetimeBilling) {
        this.datetimeBilling = datetimeBilling;
    }
}
