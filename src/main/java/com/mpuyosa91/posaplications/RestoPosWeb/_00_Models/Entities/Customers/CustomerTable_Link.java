package com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Customers;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;

@Entity
@AssociationOverrides({
        @AssociationOverride(name = "pk.masterCustomerTable",
                joinColumns = @JoinColumn(name = "master_customer_table_id")),
        @AssociationOverride(name = "pk.linkedCustomerTable",
                joinColumns = @JoinColumn(name = "linked_customer_table_id"))})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "pk")
public class CustomerTable_Link {

    private CustomerTable_Link_Id pk;

    @EmbeddedId
    public CustomerTable_Link_Id getPk() {
        return pk;
    }

    public void setPk(CustomerTable_Link_Id pk) {
        this.pk = pk;
    }

    @Transient
    public CustomerTable getLinkedCustomerTable() {
        return getPk().getLinkedCustomerTable();
    }

    public void setLinkedCustomerTable(CustomerTable linkedCustomerTable) {
        pk.setLinkedCustomerTable(linkedCustomerTable);
    }

    @Transient
    public CustomerTable getMasterCustomerTable() {
        return getPk().getMasterCustomerTable();
    }

    public void setMasterCustomerTable(CustomerTable masterCustomerTable) {
        pk.setMasterCustomerTable(masterCustomerTable);
    }

    @Override
    public int hashCode() {
        return (getPk() != null ? getPk().hashCode() : 0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        CustomerTable_Link that = (CustomerTable_Link) o;
        return getPk() != null ? getPk().equals(that.getPk()) : that.getPk() == null;
    }

}
