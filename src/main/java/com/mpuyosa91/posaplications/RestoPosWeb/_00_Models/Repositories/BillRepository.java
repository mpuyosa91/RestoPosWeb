package com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Repositories;

import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Accounting.Bill;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public interface BillRepository extends CrudRepository<Bill, UUID> {

    @Query("select bill from Bill bill where " +
            "bill.site.id = :site_id and " +
            "bill.datetimeBilling > :start_time and bill.datetimeBilling < :end_time")
    List<Bill> findBillsOfSite(
            @Param("site_id") UUID site_id,
            @Param("start_time") Calendar start_time,
            @Param("end_time") Calendar end_time
    );

    @Query("select bill_user.pk.bill from Bill_User bill_user where " +
            "bill_user.site.id = :site_id and bill_user.pk.user.id = :user_id")
    List<Bill> findBillsOfUserInSite(
            @Param("site_id") UUID site_id,
            @Param("user_id") UUID user_id
    );

}
