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

    @Query("select inventoryitem from InventoryItem inventoryitem where " +
            "inventoryitem.site.id = :site_id and " +
            "inventoryitem.serial = :item_serial")
    InventoryItem findNode(@Param("site_id") UUID site_id, @Param("item_serial") int item_serial);

    @Query("select inventoryitem from InventoryItem inventoryitem where " +
            "inventoryitem.site.id = :site_id and " +
            "inventoryitem.serial = :item_serial/10")
    InventoryItem findFirstFather(@Param("site_id") UUID site_id, @Param("item_serial") int item_serial);

    @Query("select inventoryitem from InventoryItem inventoryitem where " +
            "inventoryitem.site.id = :site_id and " +
            "inventoryitem.serial = :item_serial/100")
    InventoryItem findFather(@Param("site_id") UUID site_id, @Param("item_serial") int item_serial);

    @Query("select inventoryitem from InventoryItem inventoryitem where " +
            "inventoryitem.site.id = :site_id and " +
            "inventoryitem.serial > (:item_serial*10) and inventoryitem.serial < ((:item_serial+1)*10)")
    List<InventoryItem> findFirstChilds(@Param("site_id") UUID site_id, @Param("item_serial") int item_serial);

    @Query("select inventoryitem from InventoryItem inventoryitem where " +
            "inventoryitem.site.id = :site_id and " +
            "inventoryitem.serial > (:item_serial*100) and inventoryitem.serial < ((:item_serial+1)*100)")
    List<InventoryItem> findChilds(@Param("site_id") UUID site_id, @Param("item_serial") int item_serial);

}