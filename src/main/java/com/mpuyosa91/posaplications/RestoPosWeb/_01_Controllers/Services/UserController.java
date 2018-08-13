package com.mpuyosa91.posaplications.RestoPosWeb._01_Controllers.Services;

import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Crew.User;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Site;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Repositories.SiteRepository;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

import static com.mpuyosa91.posaplications.RestoPosWeb._01_Controllers.Model_Helpers.link_site_user;
import static com.mpuyosa91.posaplications.RestoPosWeb._01_Controllers.Model_Helpers.unlink_site_user;

@Controller
@RequestMapping(path = "/user")
public class UserController {

    private final UserRepository userRepository;
    private final SiteRepository siteRepository;

    @Autowired
    public UserController(UserRepository userRepository, SiteRepository siteRepository) {
        this.userRepository = userRepository;
        this.siteRepository = siteRepository;
    }

    @PostMapping(path = "/")
    public @ResponseBody
    UUID create(@RequestBody User user) {
        User savedUser = userRepository.save(user);
        return savedUser.getId();
    }

    @GetMapping(path = "/{id}")
    public @ResponseBody
    User read(@PathVariable UUID id) {
        return (userRepository.findById(id).isPresent()) ? userRepository.findById(id).get() : null;
    }

    @GetMapping(path = "/all")
    public @ResponseBody
    Iterable<User> readAllEnabled() {
        return userRepository.findAllEnabled();
    }

    @GetMapping(path = "/all-all")
    public @ResponseBody
    Iterable<User> readAll() {
        return userRepository.findAll();
    }

    @GetMapping(path = "/allSites/{id}")
    public @ResponseBody
    Iterable<Site> getAllSitesOfUser(@PathVariable UUID id) {
        return siteRepository.getAllOfUser(id);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity update(@PathVariable UUID id, @RequestBody User user) {

        ResponseEntity responseEntity;

        User userToUpdate = (userRepository.findById(id).isPresent()) ? userRepository.findById(id).get() : null;

        if (userToUpdate != null) {
            userToUpdate.updateFromUser(user);
            userRepository.save(userToUpdate);
            responseEntity = ResponseEntity.accepted().build();
        } else {
            responseEntity = ResponseEntity.notFound().build();
        }

        return responseEntity;
    }

    @PutMapping(path = "/enable")
    public ResponseEntity<Object> enable(@RequestBody Map<String, UUID> json) {

        boolean canEnable = json.get("user") != null && userRepository.findById(json.get("user")).isPresent();

        if (canEnable) {
            User user = userRepository.findById(json.get("user")).get();
            user.setEnabled(true);
            userRepository.save(user);
        }

        return (canEnable) ? ResponseEntity.accepted().build() : ResponseEntity.badRequest().build();
    }

    @PutMapping(path = "/disable")
    public ResponseEntity<Object> disable(@RequestBody Map<String, UUID> json) {

        boolean canDisable = json.get("user") != null && userRepository.findById(json.get("user")).isPresent();

        if (canDisable) {
            User user = userRepository.findById(json.get("user")).get();
            user.setEnabled(false);
            userRepository.save(user);
        }

        return (canDisable) ? ResponseEntity.accepted().build() : ResponseEntity.badRequest().build();
    }

    @PutMapping(path = "/add_to_site")
    public ResponseEntity<Object> addUserToSite(@RequestBody Map<String, UUID> json) {
        return (link_site_user(json, userRepository, siteRepository)) ?
                ResponseEntity.accepted().build() :
                ResponseEntity.notFound().build();
    }

    @PutMapping(path = "/remove_from_site")
    public ResponseEntity<Object> removeUserFromSite(@RequestBody Map<String, UUID> json) {
        return (unlink_site_user(json, userRepository, siteRepository)) ?
                ResponseEntity.accepted().build() :
                ResponseEntity.notFound().build();
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity delete(@PathVariable UUID id) {
        try {
            userRepository.deleteById(id);
            return ResponseEntity.accepted().build();
        } catch (IllegalArgumentException ignored) {
            return ResponseEntity.badRequest().build();
        }

    }

}
