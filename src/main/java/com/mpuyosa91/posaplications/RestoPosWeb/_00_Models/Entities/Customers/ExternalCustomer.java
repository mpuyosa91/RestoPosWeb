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
        type = CustomerTypes.ExternalCustomer;
        identifier = CustomerTypes.ExternalCustomer.getShowableName();
        position_col = 0;
        position_row = 1;
    }

    @Override
    public String toString() {
        return "ExternalCustomer{" +
                "position_row=" + position_row +
                ", position_col=" + position_col +
                ", itemListBilled=" + itemListBilled +
                ", itemListUnBilled=" + itemListUnBilled +
                ", identifier='" + identifier + '\'' +
                '}';
    }
}