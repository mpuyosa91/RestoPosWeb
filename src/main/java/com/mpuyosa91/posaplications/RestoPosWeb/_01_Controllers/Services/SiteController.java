package com.mpuyosa91.posaplications.RestoPosWeb._01_Controllers.Services;

import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Crew.User;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Customers.Customer;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Customers.CustomerTable;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Customers.ExternalCustomer;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Customers.PointOfService;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.ProductsAndSupplies.InventoryItem;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Site;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Repositories.CustomerRepository;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Repositories.InventoryRepository;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Repositories.SiteRepository;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Repositories.UserRepository;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.SettingsAndProperties.LocalSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping(path = "/site")
public class SiteController {

    private final Environment         environment;
    private final SiteRepository      siteRepository;
    private final UserRepository      userRepository;
    private final CustomerRepository  customerRepository;
    private final InventoryRepository inventoryRepository;

    @Autowired
    public SiteController(SiteRepository siteRepository,
                          UserRepository userRepository,
                          CustomerRepository customerRepository,
                          Environment environment,
                          InventoryRepository inventoryRepository) {
        this.siteRepository = siteRepository;
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.environment = environment;
        this.inventoryRepository = inventoryRepository;
    }

    @PostMapping(path = "/")
    public @ResponseBody
    UUID create(@RequestBody Site site) {

        String server_port = environment.getProperty("local.server.port");

        Site savedSite = siteRepository.save(site);

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Creating Point Of Service

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

        // Creating External Customer

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

        // Creating First Raw Food

        InventoryItem rawFood = restTemplate.exchange(
                "http://localhost:" + server_port + "/inventory_item/",
                HttpMethod.POST,
                new HttpEntity<>(new InventoryItem(), headers),
                InventoryItem.class
        ).getBody();
        assert rawFood != null;
        rawFood.setName("Ingredientes");
        rawFood.setSerial(1);
        rawFood.setFinal_item(false);
        inventoryRepository.save(rawFood);

        Map<String, String> addRawFoodJson = new HashMap<>();
        addRawFoodJson.put("site", savedSite.getId().toString());
        addRawFoodJson.put("item", rawFood.getId().toString());
        restTemplate.put(
                "http://localhost:" + server_port + "/inventory_item/add_to_site",
                addRawFoodJson,
                InventoryItem.class);

        // Creating First Mixture
        InventoryItem mixture = restTemplate.postForObject(
                "http://localhost:" + server_port + "/inventory_item/",
                new InventoryItem(),
                InventoryItem.class);
        assert mixture != null;
        mixture.setName("SubProductos");
        mixture.setSerial(2);
        mixture.setFinal_item(false);
        inventoryRepository.save(mixture);

        Map<String, String> addMixtureJson = new HashMap<>();
        addMixtureJson.put("site", savedSite.getId().toString());
        addMixtureJson.put("item", mixture.getId().toString());
        restTemplate.put(
                "http://localhost:" + server_port + "/inventory_item/add_to_site",
                addMixtureJson,
                InventoryItem.class);

        // Creating First Product
        InventoryItem product = restTemplate.postForObject(
                "http://localhost:" + server_port + "/inventory_item/",
                new InventoryItem(),
                InventoryItem.class);

        assert product != null;
        product.setName("Productos");
        product.setSerial(3);
        product.setFinal_item(false);
        inventoryRepository.save(product);

        Map<String, String> addProductJson = new HashMap<>();
        addProductJson.put("site", savedSite.getId().toString());
        addProductJson.put("item", product.getId().toString());

        restTemplate.put(
                "http://localhost:" + server_port + "/inventory_item/add_to_site",
                addProductJson,
                InventoryItem.class);

        // Creating First Menu Plate
        InventoryItem menuPlate = restTemplate.postForObject(
                "http://localhost:" + server_port + "/inventory_item/",
                new InventoryItem(),
                InventoryItem.class);
        assert menuPlate != null;
        menuPlate.setName("De la carta");
        menuPlate.setSerial(4);
        menuPlate.setFinal_item(false);
        inventoryRepository.save(menuPlate);

        Map<String, String> addMenuPlateJson = new HashMap<>();
        addMenuPlateJson.put("site", savedSite.getId().toString());
        addMenuPlateJson.put("item", menuPlate.getId().toString());

        restTemplate.put(
                "http://localhost:" + server_port + "/inventory_item/add_to_site",
                addMenuPlateJson,
                InventoryItem.class);

        LocalSettings localSettings = new LocalSettings();
        localSettings.setProperty("site_id", savedSite.getId().toString());
        localSettings.setProperty("site_tradeName", savedSite.getTradeName());

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

    @PostMapping(path = "/createUser/{site_id}")
    public ResponseEntity createUser(@PathVariable UUID site_id, @RequestBody User bodyUser) {

        String server_port = environment.getProperty("local.server.port");
        boolean canCreate = siteRepository.findById(site_id).isPresent() &&
                bodyUser.getFirstName() != null &&
                bodyUser.getLastName() != null;

        if (canCreate) {

            Site site = siteRepository.findById(site_id).get();

            RestTemplate restTemplate = new RestTemplate();

            if (bodyUser.getUsername() == null) {
                String username = bodyUser.getFirstName().substring(0, 1).toLowerCase();
                username = username.concat(bodyUser.getLastName().toLowerCase());
                bodyUser.setUsername(username);
            }

            UUID user_id = restTemplate.postForObject(
                    "http://localhost:" + server_port + "/user/",
                    bodyUser,
                    UUID.class);

            Map<String, String> addCustomerTableJson = new HashMap<>();
            addCustomerTableJson.put("site", site.getId().toString());
            addCustomerTableJson.put("user", user_id.toString());

            restTemplate.put(
                    "http://localhost:" + server_port + "/user/add_to_site",
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

    @GetMapping(path = "/{site_id}/search-by-username/{username}")
    public @ResponseBody
    User searchUserByUsername(@PathVariable UUID site_id, @PathVariable String username) {

        boolean canSearch = username != null;
        User    user      = null;

        if (canSearch) {

            List<User> userList = userRepository.getAllOfSite(site_id);

            for (User user_item : userList) {
                if (user_item.getUsername().equals(username)) {
                    user = user_item;
                    break;
                }
            }

        }

        return user;
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
