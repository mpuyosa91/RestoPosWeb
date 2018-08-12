package com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Repositories;

import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Customers.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface CustomerRepository extends CrudRepository<Customer, UUID> {

    @Query("select customer from Customer customer where customer.enabled=true")
    List<Customer> findAllEnabled();

    @Query("select site.customers from Site site where site.id = :site_id")
    List<Customer> findAllOfSite(@Param("site_id") UUID site_id);

}

