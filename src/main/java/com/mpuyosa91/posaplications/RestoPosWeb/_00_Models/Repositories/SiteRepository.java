package com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Repositories;

import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Site;
import org.springframework.data.jpa.repository.Modifying;
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

    @Query("select site.commandNumber from Site site where site.id = :site_id")
    int getCommandNumber(@Param("site_id") UUID site_id);

    @Modifying(clearAutomatically = true)
    @Query("update Site site set site.commandNumber = :commandNumber where site.id = :site_id")
    int setCommandNumber(@Param("site_id") UUID site_id, @Param("commandNumber") int commandNumber);

}
