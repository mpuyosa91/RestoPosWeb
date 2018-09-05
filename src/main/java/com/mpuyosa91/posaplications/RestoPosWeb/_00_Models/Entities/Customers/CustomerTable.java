package com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Customers;

import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.ProductsAndSupplies.SalableItem;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author MoisesE
 */
@Entity
@DiscriminatorValue("Table")
public class CustomerTable extends Customer {

    @OneToMany(mappedBy = "pk.masterCustomerTable", cascade = CascadeType.ALL)
    private Set<CustomerTable_Link> linkedCustomerTables;
    @ManyToOne
    @JoinColumn(name = "master_linker_id")
    private CustomerTable           masterLinker;
    private boolean                 linked;

    public CustomerTable() {
        this.type = CustomerTypes.Table;
    }

    public CustomerTable(int position_row, int position_col) {
        super();
        this.position_row = position_row;
        this.position_col = position_col;
        this.linkedCustomerTables = new HashSet<>();
        this.linked = false;
        this.type = CustomerTypes.Table;
        identifier = CustomerTypes.Table.getShowableName() +
                     " " +
                     String.valueOf(this.position_col) +
                     String.valueOf(this.position_row);
    }

    public void link(boolean amIMaster, CustomerTable customerTable) {
        setLinked(true);
        if (amIMaster) {
            customerTable.link(false, this);
            setMasterLinker(null);
            getLinkedCustomerTables().add(new CustomerTable_Link(this, customerTable));
            getCurrentBill().addCustomer(customerTable);
        } else {
            setMasterLinker(customerTable);
            setLinkedCustomerTables(new HashSet<>());
        }
    }

    public void unlink() {
        setLinked(false);
        if (!getLinkedCustomerTables().isEmpty()) {
            for (CustomerTable_Link customerTable_link : getLinkedCustomerTables()) {
                customerTable_link.getPk().getLinkedCustomerTable().unlink();
                getCurrentBill().removeCustomer(customerTable_link.getLinkedCustomerTable());
            }
        }
        setMasterLinker(null);
        setLinkedCustomerTables(new HashSet<>());
    }

    public CustomerTable getMasterLinker() {
        if (isLinked()) return masterLinker;
        else return null;
    }

    public void setMasterLinker(CustomerTable masterLinker) {
        this.masterLinker = masterLinker;
    }

    public boolean isLinked() {
        return linked;
    }

    public void setLinked(boolean linked) {
        this.linked = linked;
    }

    public Set<CustomerTable_Link> getLinkedCustomerTables() {
        return linkedCustomerTables;
    }

    public void setLinkedCustomerTables(Set<CustomerTable_Link> linkedCustomerTables) {
        this.linkedCustomerTables = linkedCustomerTables;
    }

    public String getIdentifier() {
        return CustomerTypes.Table.getShowableName() +
               " " +
               String.valueOf(this.position_col) +
               String.valueOf(this.position_row);
    }

    public boolean isOccupied() {
        return getPreConsumption() != 0.0 || isLinked();
    }

    public String toString() {
        StringBuilder r;
        r = new StringBuilder("\n" + getIdentifier() + ":");
        for (SalableItem item : itemListBilled) {
            r.append("\n    ").append(item.getName());
            int quantity = 0;
            for (SalableItem item2 : itemListBilled) {
                quantity += (item2.getSerial().equals(item.getSerial())) ? 1 : 0;
            }
            r.append(" x").append(quantity);
            r.append(", $").append(item.getPrice() * quantity);
        }
        for (SalableItem item : itemListUnBilled) {
            r.append("\n  ->").append(item.getName());
            int quantity = 0;
            for (SalableItem item2 : itemListUnBilled) {
                quantity += (item2.getSerial().equals(item.getSerial())) ? 1 : 0;
            }
            r.append(" x").append(quantity);
            r.append(", $").append(item.getPrice() * quantity);
        }
        return r.toString();
    }

}
