package com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Repositories;

import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.ProductsAndSupplies.SalableItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface SalableItemRepository extends CrudRepository<SalableItem, UUID> {

//    @Query("select inventoryitem from InventoryItem inventoryitem where inventoryitem.enabled=true")
//    List<InventoryItem> findAllEnabled();
//
//    @Query("select inventoryitem from InventoryItem inventoryitem where inventoryitem.site.id = :site_id")
//    List<InventoryItem> findAllOfSite(@Param("site_id") UUID site_id);

    @Query("select salableItem from SalableItem salableItem where customer = null or user = null")
    Iterable<SalableItem> findAllOrphan();

}
