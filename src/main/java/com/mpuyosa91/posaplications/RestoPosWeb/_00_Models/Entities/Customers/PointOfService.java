package com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Customers;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author MoisesE
 */
@Entity
@DiscriminatorValue("PointOfService")
public class PointOfService extends Customer {

    public PointOfService() {
        super();
        identifier = CustomerTypes.PointOfService.getShowableName();
    }

    @Override
    public String toString() {
        return "PointOfService{" +
                "itemListBilled=" + itemListBilled +
                ", itemListUnBilled=" + itemListUnBilled +
                ", identifier='" + identifier + '\'' +
                "} " + super.toString();
    }
}
