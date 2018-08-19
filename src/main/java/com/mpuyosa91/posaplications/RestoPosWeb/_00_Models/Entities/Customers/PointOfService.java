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
        type = CustomerTypes.PointOfService;
        identifier = CustomerTypes.PointOfService.getShowableName();
        position_col = 0;
        position_row = 0;
    }

    @Override
    public String toString() {
        return "PointOfService{" +
                "position_row=" + position_row +
                ", position_col=" + position_col +
                ", itemListBilled=" + itemListBilled +
                ", itemListUnBilled=" + itemListUnBilled +
                ", identifier='" + identifier + '\'' +
                '}';
    }
}
