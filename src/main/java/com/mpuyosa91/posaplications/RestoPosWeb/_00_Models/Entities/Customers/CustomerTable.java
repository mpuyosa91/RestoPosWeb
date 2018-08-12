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

    private Integer position_row;
    private Integer position_col;

    @OneToMany(mappedBy = "pk.masterCustomerTable", cascade = CascadeType.ALL)
    private Set<CustomerTable_Link> linkedCustomerTables;
    @ManyToOne
    @JoinColumn(name = "master_linker_id")
    private CustomerTable           masterLinker;
    private boolean                 linked;

    public CustomerTable(int position_row, int position_col) {
        super();
        this.position_row = position_row;
        this.position_col = position_col;
        linkedCustomerTables = new HashSet<>();
        this.linked = false;
        identifier = CustomerTypes.Table.getShowableName() + " " +
                String.valueOf(this.position_col) +
                String.valueOf(this.position_row);
    }

    public void link(boolean amIMaster, CustomerTable customerTable) {
        setLinked(true);
        if (amIMaster) {
            customerTable.link(false, this);
            setMasterLinker(null);
            CustomerTable_Link_Id customerTable_link_id = new CustomerTable_Link_Id();
            customerTable_link_id.setMasterCustomerTable(this);
            customerTable_link_id.setLinkedCustomerTable(customerTable);
            CustomerTable_Link customerTable_link = new CustomerTable_Link();
            customerTable_link.setPk(customerTable_link_id);
            getLinkedCustomerTables().add(customerTable_link);
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
            }
        }
        setMasterLinker(null);
        setLinkedCustomerTables(new HashSet<>());
    }

    public CustomerTable getMasterLinker() {
        if (isLinked())
            return masterLinker;
        else
            return null;
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

    public Integer getPosition_row() {
        return position_row;
    }

    public void setPosition_row(Integer position_row) {
        this.position_row = position_row;
    }

    public Integer getPosition_col() {
        return position_col;
    }

    public void setPosition_col(Integer position_col) {
        this.position_col = position_col;
    }

    public Set<CustomerTable_Link> getLinkedCustomerTables() {
        return linkedCustomerTables;
    }

    public void setLinkedCustomerTables(Set<CustomerTable_Link> linkedCustomerTables) {
        this.linkedCustomerTables = linkedCustomerTables;
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

    public final String getIdentifier() {
        return (CustomerTypes.Table.getShowableName() + " " + String.valueOf(this.position_col) + String.valueOf(this.position_row));
    }

    public boolean isOccupied() {
        return getPreConsumption() != 0.0 || isLinked();
    }

}
