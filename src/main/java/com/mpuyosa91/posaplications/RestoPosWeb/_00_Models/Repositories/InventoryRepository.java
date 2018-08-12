package com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Repositories;

import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.ProductsAndSupplies.InventoryItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface InventoryRepository extends CrudRepository<InventoryItem, UUID> {

    @Query("select inventoryitem from InventoryItem inventoryitem where inventoryitem.enabled=true")
    List<InventoryItem> findAllEnabled();

    @Query("select inventoryitem from InventoryItem inventoryitem where inventoryitem.site.id = :site_id")
    List<InventoryItem> findAllOfSite(@Param("site_id") UUID site_id);

}
