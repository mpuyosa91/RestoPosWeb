package com.mpuyosa91.posaplications.RestoPosWeb._01_Controllers.Services;

import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Crew.User;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Customers.Customer;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Customers.CustomerTable;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Customers.ExternalCustomer;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Customers.PointOfService;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Site;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Repositories.CustomerRepository;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Repositories.SiteRepository;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.mpuyosa91.posaplications.RestoPosWeb._01_Controllers.Model_Helpers.*;

@Controller
@RequestMapping(path = "/site")
public class SiteController {

    private final Environment        environment;
    private final SiteRepository     siteRepository;
    private final UserRepository     userRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public SiteController(SiteRepository siteRepository,
                          UserRepository userRepository,
                          CustomerRepository customerRepository, Environment environment) {
        this.siteRepository = siteRepository;
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.environment = environment;
    }

    @PostMapping(path = "/")
    public @ResponseBody
    UUID create(@RequestBody Site site) {

        String server_port = environment.getProperty("local.server.port");

        Site savedSite = siteRepository.save(site);

        RestTemplate restTemplate = new RestTemplate();

        PointOfService pointOfService = restTemplate.postForObject(
                "http://localhost:" + server_port + "/customer/PointOfService",
                new PointOfService(),
                PointOfService.class);

        Map<String, String> addPointOfServiceJson = new HashMap<>();
        addPointOfServiceJson.put("site", savedSite.getId().toString());
        addPointOfServiceJson.put("customer", pointOfService != null ? pointOfService.getId().toString() : null);

        restTemplate.put(
                "http://localhost:" + server_port + "/customer/add_to_site",
                addPointOfServiceJson,
                PointOfService.class);

        ExternalCustomer externalCustomer = restTemplate.postForObject(
                "http://localhost:" + server_port + "/customer/ExternalCustomer",
                new ExternalCustomer(),
                ExternalCustomer.class);

        Map<String, String> addExternalCustomerJson = new HashMap<>();
        addExternalCustomerJson.put("site", savedSite.getId().toString());
        addExternalCustomerJson.put("customer", externalCustomer != null ? externalCustomer.getId().toString() : null);

        restTemplate.put(
                "http://localhost:" + server_port + "/customer/add_to_site",
                addExternalCustomerJson,
                ExternalCustomer.class);

        return siteRepository.save(savedSite).getId();
    }

    @PostMapping(path = "/createTable/{site_id}")
    public ResponseEntity createTable(@PathVariable UUID site_id, @RequestBody CustomerTable customer) {
        String server_port = environment.getProperty("local.server.port");

        if (siteRepository.findById(site_id).isPresent()) {
            Site site = siteRepository.findById(site_id).get();

            RestTemplate restTemplate = new RestTemplate();

            CustomerTable customerTable = restTemplate
                    .postForObject(
                            "http://localhost:" + server_port + "/customer/Table",
                            customer,
                            CustomerTable.class);

            Map<String, String> addCustomerTableJson = new HashMap<>();
            addCustomerTableJson.put("site", site.getId().toString());
            addCustomerTableJson.put("customer", customerTable != null ? customerTable.getId().toString() : null);

            // TODO: Agregar validacion que no hayan dos mesas en una misma posicion, por ejemplo, ingresando solo
            // columna y agregando automaticamente la fila.

            restTemplate.put(
                    "http://localhost:" + server_port + "/customer/add_to_site",
                    addCustomerTableJson,
                    ResponseEntity.class);

            return ResponseEntity.accepted().build();
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping(path = "/{id}")
    public @ResponseBody
    Site read(@PathVariable UUID id) {
        return (siteRepository.findById(id).isPresent()) ? siteRepository.findById(id).get() : null;
    }

    @GetMapping(path = "/allUsers/{id}")
    public @ResponseBody
    Iterable<User> readAllUsersOfSite(@PathVariable UUID id) {
        return userRepository.getAllOfSite(id);
    }

    @GetMapping(path = "/allCustomers/{id}")
    public @ResponseBody
    Iterable<Customer> readAllCustomersOfSite(@PathVariable UUID id) {
        return customerRepository.findAllOfSite(id);
    }

    @GetMapping(path = "/all")
    public @ResponseBody
    Iterable<Site> readAllEnabled() {
        return siteRepository.findAllEnabled();
    }

    @GetMapping(path = "/all-all")
    public @ResponseBody
    Iterable<Site> readAll() {
        return siteRepository.findAll();
    }

    @PutMapping(path = "/enable")
    public ResponseEntity<Object> enable(@RequestBody Map<String, UUID> json) {

        boolean canEnable = json.get("site") != null && siteRepository.findById(json.get("site")).isPresent();

        if (canEnable) {
            Site site = siteRepository.findById(json.get("site")).get();
            site.setEnabled(true);
            siteRepository.save(site);
        }

        return (canEnable) ? ResponseEntity.accepted().build() : ResponseEntity.badRequest().build();
    }

    @PutMapping(path = "/disable")
    public ResponseEntity<Object> disable(@RequestBody Map<String, UUID> json) {

        boolean canDisable = json.get("site") != null && siteRepository.findById(json.get("site")).isPresent();

        if (canDisable) {
            Site site = siteRepository.findById(json.get("site")).get();
            site.setEnabled(false);
            siteRepository.save(site);
        }

        return (canDisable) ? ResponseEntity.accepted().build() : ResponseEntity.badRequest().build();
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity update(@PathVariable UUID id, @RequestBody Site site) {

        ResponseEntity responseEntity;

        Site siteToUpdate = (siteRepository.findById(id).isPresent()) ? siteRepository.findById(id).get() : null;

        if (siteToUpdate != null) {
            // TODO: Agregar otros updates
            if (site.getUsers() != null) siteToUpdate.getUsers().addAll(site.getUsers());
            if (site.getCustomers() != null) siteToUpdate.getCustomers().addAll(site.getCustomers());
            siteRepository.save(siteToUpdate);
            responseEntity = ResponseEntity.accepted().build();
        } else {
            responseEntity = ResponseEntity.notFound().build();
        }

        return responseEntity;
    }

    @PutMapping(path = "/add_user")
    public ResponseEntity<Object> addUserToSite(@RequestBody Map<String, UUID> json) {
        return (link_site_user(json, userRepository, siteRepository)) ?
                ResponseEntity.accepted().build() :
                ResponseEntity.badRequest().build();
    }

    @PutMapping(path = "/remove_user")
    public ResponseEntity<Object> removeUserFromSite(@RequestBody Map<String, UUID> json) {
        return (unlink_site_user(json, userRepository, siteRepository)) ?
                ResponseEntity.accepted().build() :
                ResponseEntity.badRequest().build();
    }

    @PutMapping(path = "/add_customer")
    public ResponseEntity<Object> addCustomerToSite(@RequestBody Map<String, UUID> json) {
        return (link_site_customer(json, siteRepository, customerRepository)) ?
                ResponseEntity.accepted().build() :
                ResponseEntity.badRequest().build();
    }

    @PutMapping(path = "/remove_customer")
    public ResponseEntity<Object> removeCustomerFromSite(@RequestBody Map<String, UUID> json) {
        return (unlink_site_customer(json, siteRepository, customerRepository)) ?
                ResponseEntity.accepted().build() :
                ResponseEntity.badRequest().build();
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity delete(@PathVariable UUID id) {
        try {
            siteRepository.deleteById(id);
            return ResponseEntity.accepted().build();
        } catch (IllegalArgumentException ignored) {
            return ResponseEntity.badRequest().build();
        }

    }

}
