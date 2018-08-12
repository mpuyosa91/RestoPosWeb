package com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Repositories;

import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Site;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SiteRepository extends CrudRepository<Site, UUID> {

    @Query("select site from Site site where site.enabled=true")
    List<Site> findAllEnabled();

    @Query("select user.sites from User user where user.id = :user_id")
    List<Site> getAllOfUser(@Param("user_id") UUID user_id);

}
