package com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Customers;

import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.ProductsAndSupplies.SalableItem;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author MoisesE
 */
@Entity
@DiscriminatorValue("ExternalCustomer")
public final class ExternalCustomer extends PointOfService {

    public ExternalCustomer() {
        super();
        identifier = CustomerTypes.ExternalCustomer.getShowableName();
    }

    @Override
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
            r.append(", $").append(item.getPrice());
        }
        return r.toString();
    }
}