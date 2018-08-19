package com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Crew.User;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Customers.Customer;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Site {

    @Id
    @GeneratedValue(generator = "uuid")
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID    id;
    private String  tradeName;
    private String  billing_name;
    private String  nit;
    private String  legalAddress;
    private boolean enabled = true;

    @ManyToMany(
            mappedBy = "sites",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<User> users;

    @OneToMany(
            mappedBy = "site",
            cascade = CascadeType.ALL)
    private Set<Customer> customers     = new HashSet<>();
    private int           commandNumber = 1;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public String getBilling_name() {
        return billing_name;
    }

    public void setBilling_name(String billing_name) {
        this.billing_name = billing_name;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getLegalAddress() {
        return legalAddress;
    }

    public void setLegalAddress(String legalAddress) {
        this.legalAddress = legalAddress;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(Set<Customer> customers) {
        this.customers = customers;
    }

    public int getCommandNumber() {
        return commandNumber;
    }

    public void setCommandNumber(int commandNumber) {
        this.commandNumber = commandNumber;
    }

    @Override
    public String toString() {
        return "Site{" +
                "id=" + id +
                ", tradeName='" + tradeName + '\'' +
                ", billing_name='" + billing_name + '\'' +
                ", nit='" + nit + '\'' +
                ", enabled=" + enabled +
                ", users=" + users +
                ", customers=" + customers +
                ", commandNumber=" + commandNumber +
                '}';
    }
}
