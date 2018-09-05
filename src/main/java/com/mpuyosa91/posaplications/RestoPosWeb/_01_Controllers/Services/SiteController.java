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

    @PostMapping(path = "/{site_id}/create_table")
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

    @PostMapping(path = "/{site_id}/create_user")
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

    @PostMapping(path = "/{site_id}/create-inventory-item")
    public @ResponseBody
    UUID createInventoryItem(@PathVariable UUID site_id, @RequestBody InventoryItem inventoryItem) {
        boolean canCreate = siteRepository.findById(site_id).isPresent() &&
                inventoryItem.getSerial() != null && inventoryItem.getName() != null;

        //TODO Buscar por site y por serial que no este creado para poder crear.

        if (canCreate) {
            Site site = siteRepository.findById(site_id).get();
            inventoryItem.setSite(site);
            inventoryRepository.save(inventoryItem);

            return inventoryItem.getId();
        }

        return null;
    }

    @GetMapping(path = "/{site_id}")
    public @ResponseBody
    Site read(@PathVariable UUID site_id) {
        return (siteRepository.findById(site_id).isPresent()) ? siteRepository.findById(site_id).get() : null;
    }

    @GetMapping(path = "/{site_id}/all-users")
    public @ResponseBody
    Iterable<User> readAllUsersOfSite(@PathVariable UUID site_id) {
        return userRepository.getAllOfSite(site_id);
    }

    @GetMapping(path = "/{site_id}/search-user-by-username/{username}")
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

    @GetMapping(path = "/{site_id}/all-customers")
    public @ResponseBody
    Iterable<Customer> readAllCustomersOfSite(@PathVariable UUID site_id) {
        return customerRepository.findAllOfSite(site_id);
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

    @PutMapping(path = "/{site_id}")
    public ResponseEntity update(@PathVariable UUID site_id, @RequestBody Site site) {

        ResponseEntity responseEntity;

        Site siteToUpdate = (siteRepository.findById(site_id).isPresent()) ? siteRepository.findById(site_id).get() : null;

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

    @PutMapping(path = "/{site_id}/update-inventory-item/{inventoryItem_id}")
    public @ResponseBody
    UUID updateInventoryItem(@PathVariable UUID site_id,
                             @PathVariable UUID inventoryItem_id, @RequestBody InventoryItem inventoryItem) {
        boolean canUpdate = siteRepository.findById(site_id).isPresent() &&
                            inventoryRepository.findById(inventoryItem_id).isPresent() &&
                            inventoryItem.getSerial() != null && inventoryItem.getName() != null;

        if (canUpdate) {
            Site          site               = siteRepository.findById(site_id).get();
            InventoryItem savedInventoryItem = inventoryRepository.findById(inventoryItem_id).get();
            savedInventoryItem.copyFrom(inventoryItem);
            savedInventoryItem.setSite(site);
            inventoryRepository.save(savedInventoryItem);

            return savedInventoryItem.getId();
        }

        return null;
    }

    @PutMapping(path = "/{site_id}/update_user/{user_id}")
    public @ResponseBody
    UUID updateUser(@PathVariable UUID site_id, @PathVariable UUID user_id, @RequestBody User user) {
        boolean canUpdate = siteRepository.findById(site_id).isPresent() &&
                            userRepository.findById(user_id).isPresent() &&
                            user.getFirstName() != null &&
                            user.getLastName() != null;

        if (canUpdate) {

            Site site      = siteRepository.findById(site_id).get();
            User savedUser = userRepository.findById(user_id).get();
            savedUser.updateFromUser(user);
            savedUser.getSites().add(site);
            userRepository.save(savedUser);

            return savedUser.getId();
        }

        return null;
    }

    @DeleteMapping(path = "/{site_id}")
    public ResponseEntity delete(@PathVariable UUID site_id) {
        try {
            siteRepository.deleteById(site_id);
            return ResponseEntity.accepted().build();
        } catch (IllegalArgumentException ignored) {
            return ResponseEntity.badRequest().build();
        }

    }

}
