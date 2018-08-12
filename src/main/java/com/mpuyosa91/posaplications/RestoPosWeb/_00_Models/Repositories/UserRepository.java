package com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Repositories;

import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Crew.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {

    @Query("select user from User user where user.enabled=true")
    List<User> findAllEnabled();

    @Query("select site.users from Site site where site.id = :site_id")
    List<User> getAllOfSite(@Param("site_id") UUID site_id);

}
