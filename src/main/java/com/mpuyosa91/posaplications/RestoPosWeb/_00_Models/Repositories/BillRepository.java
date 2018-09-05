package com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Repositories;

import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Accounting.Bill;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public interface BillRepository extends CrudRepository<Bill, UUID> {

    @Query("select count(bill) from Bill bill where bill.site.id = :site_id")
    int countBillsOfSite(@Param("site_id") UUID site_id);

    @Query("select bill from Bill bill where bill.site.id = :site_id")
    List<Bill> getAllBy(@Param("site_id") UUID site_id);

    @Modifying
    @Transactional
    @Query("delete from Bill bill where bill.site.id = :site_id")
    void deleteAllBy(@Param("site_id") UUID site_id);

    @Query("select bill from Bill bill where " +
           "bill.site.id = :site_id and " +
           "bill.dateTimeStart > :start_time and bill.dateTimeFinal < :end_time")
    List<Bill> findBillsOfSiteInRange(@Param("site_id") UUID site_id,
                                      @Param("start_time") Calendar start_time,
                                      @Param("end_time") Calendar end_time);

    @Query("select salable_item.bill from SalableItem salable_item where " +
           "salable_item.bill.site.id = :site_id and salable_item.user.id = :user_id")
    List<UUID> findBillsOfUserInSite(@Param("site_id") UUID site_id, @Param("user_id") UUID user_id);

}
