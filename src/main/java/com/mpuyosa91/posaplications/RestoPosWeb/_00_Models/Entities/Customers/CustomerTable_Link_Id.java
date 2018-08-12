package com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Customers;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CustomerTable_Link_Id implements Serializable {

    private CustomerTable masterCustomerTable;
    private CustomerTable linkedCustomerTable;

    @Override
    public int hashCode() {
        return Objects.hash(masterCustomerTable, linkedCustomerTable);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;

        if (object == null || getClass() != object.getClass())
            return false;

        CustomerTable_Link_Id that = (CustomerTable_Link_Id) object;
        return Objects.equals(masterCustomerTable, that.masterCustomerTable) &&
                Objects.equals(linkedCustomerTable, that.linkedCustomerTable);
    }

    @ManyToOne
    CustomerTable getMasterCustomerTable() {
        return masterCustomerTable;
    }

    void setMasterCustomerTable(CustomerTable masterCustomerTable) {
        this.masterCustomerTable = masterCustomerTable;
    }

    @ManyToOne
    CustomerTable getLinkedCustomerTable() {
        return linkedCustomerTable;
    }

    void setLinkedCustomerTable(CustomerTable linkedCustomerTable) {
        this.linkedCustomerTable = linkedCustomerTable;
    }
}