package com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Customers;

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
        return "ExternalCustomer{" +
                "itemListBilled=" + itemListBilled +
                ", itemListUnBilled=" + itemListUnBilled +
                ", identifier='" + identifier + '\'' +
                '}';
    }
}