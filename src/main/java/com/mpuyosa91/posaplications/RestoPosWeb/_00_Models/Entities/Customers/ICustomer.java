package com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Customers;

import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Accounting.Bill;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Crew.User;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.ProductsAndSupplies.SalableItem;

import java.util.Calendar;
import java.util.Set;

/**
 * @author MoisesE
 */
public interface ICustomer {

    String getIdentifier();

    boolean isOccupied();

    SalableItem addItem(User user, SalableItem salableItem);

    void billAllItems();

    boolean removeItem(SalableItem item);

    Set<SalableItem> getItemListUnBilled();

    Set<SalableItem> getItemListBilled();

    void incrementOrderNum();

    int getOrderNum();

    Bill evacuate();

    Calendar getDateTimeFinal();

    Calendar getDateTimeStart();

    long getDurationInSeconds();

    double getConsumption();

    double getPreConsumption();

    CustomerTypes getType();

    enum CustomerTypes {
        GenericCustomer("Cliente Generico"),
        Table("Mesa "),
        PointOfService("Compra En Caja"),
        ExternalCustomer("Cliente Externo");

        private final String showableName;

        CustomerTypes(String showable) {
            showableName = showable;
        }

        public String getShowableName() {
            return showableName;
        }
    }

}
