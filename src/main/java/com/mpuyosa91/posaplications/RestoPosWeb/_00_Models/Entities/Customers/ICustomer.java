package com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Customers;

import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.ProductsAndSupplies.InventoryItem;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.ProductsAndSupplies.SalableItem;

import java.util.Calendar;
import java.util.Set;

/**
 * @author MoisesE
 */
public interface ICustomer {

    String getIdentifier();

    boolean isOccupied();

    SalableItem addItem(InventoryItem salableItem, String notes);

    void billAllItems();

    boolean removeItem(SalableItem item);

    Set<SalableItem> getItemListUnBilled();

    Set<SalableItem> getItemListBilled();

    void incrementOrderNum();

    int getOrderNum();

    double evacuate();

    Calendar getDateTimeFinal();

    Calendar getDateTimeStart();

    long getDurationInSeconds();

    double getConsumption();

    double getPreConsumption();

    enum CustomerTypes {
        GenericCustomer("Cliente Generico"),
        Table("Mesa "),
        PointOfService("Compra por Taquilla"),
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
