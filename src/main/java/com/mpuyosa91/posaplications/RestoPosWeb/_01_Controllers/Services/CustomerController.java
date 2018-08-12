package com.mpuyosa91.posaplications.RestoPosWeb._01_Controllers.Services;

import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Accounting.Bill;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Accounting.Bill_User;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Accounting.Bill_User_Id;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Crew.User;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Customers.Customer;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Customers.CustomerTable;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Customers.ExternalCustomer;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Customers.PointOfService;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.ProductsAndSupplies.InventoryItem;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.ProductsAndSupplies.SalableItem;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Repositories.*;
import com.mpuyosa91.posaplications.RestoPosWeb._01_Controllers.Model_Helpers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

import static com.mpuyosa91.posaplications.RestoPosWeb._01_Controllers.Model_Helpers.link_site_customer;
import static com.mpuyosa91.posaplications.RestoPosWeb._01_Controllers.Model_Helpers.unlink_site_customer;

@Controller
@RequestMapping(path = "/customer")
public class CustomerController {

    private final CustomerRepository    customerRepository;
    private final SiteRepository        siteRepository;
    private final InventoryRepository   inventoryRepository;
    private final SalableItemRepository salableItemRepository;
    private final BillRepository        billRepository;
    private final UserRepository        userRepository;

    @Autowired
    public CustomerController(CustomerRepository customerRepository,
                              SiteRepository siteRepository,
                              InventoryRepository inventoryRepository,
                              SalableItemRepository salableItemRepository,
                              BillRepository billRepository,
                              UserRepository userRepository) {
        this.customerRepository = customerRepository;
        this.siteRepository = siteRepository;
        this.inventoryRepository = inventoryRepository;
        this.salableItemRepository = salableItemRepository;
        this.billRepository = billRepository;
        this.userRepository = userRepository;
    }

    @PostMapping(path = "/PointOfService")
    public @ResponseBody
    PointOfService createPointOfService(@RequestBody PointOfService customer) {
        return customerRepository.save(customer);
    }

    @PostMapping(path = "/Table")
    public @ResponseBody
    CustomerTable createTable(@RequestBody CustomerTable customer) {
        return customerRepository.save(customer);
    }

    @PostMapping(path = "/ExternalCustomer")
    public @ResponseBody
    ExternalCustomer createExternalCustomer(@RequestBody ExternalCustomer customer) {
        return customerRepository.save(customer);
    }

    @GetMapping(path = "/{id}")
    public @ResponseBody
    Customer read(@PathVariable UUID id) {
        return (customerRepository.findById(id).isPresent()) ? customerRepository.findById(id).get() : null;
    }

    @GetMapping(path = "/all")
    public @ResponseBody
    Iterable<Customer> readAllEnabled() {
        return customerRepository.findAllEnabled();
    }

    @GetMapping(path = "/all-all")
    public @ResponseBody
    Iterable<Customer> readAll() {
        return customerRepository.findAll();
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity update(@PathVariable UUID id, @RequestBody Customer customer) {

        ResponseEntity responseEntity;

        Customer siteToUpdate = (customerRepository.findById(id).isPresent()) ? customerRepository.findById(id).get() : null;

        if (siteToUpdate != null) {
            //TODO: Para clonar
            customerRepository.save(siteToUpdate);
            responseEntity = ResponseEntity.accepted().build();
        } else {
            responseEntity = ResponseEntity.notFound().build();
        }

        return responseEntity;
    }

    @PutMapping(path = "/link")
    public ResponseEntity link(@RequestBody Map<String, UUID> json) {
        return Model_Helpers.link_customer_customer(json, customerRepository) ?
                ResponseEntity.accepted().build() :
                ResponseEntity.badRequest().build();
    }

    @PutMapping(path = "/unlink")
    public ResponseEntity unlink(@RequestBody Map<String, UUID> json) {
        return Model_Helpers.unlink_customer_customer(json, customerRepository) ?
                ResponseEntity.accepted().build() :
                ResponseEntity.badRequest().build();
    }

    @PutMapping(path = "/enable")
    public ResponseEntity<Object> enable(@RequestBody Map<String, UUID> json) {

        boolean canEnable = json.get("customer") != null &&
                customerRepository.findById(json.get("customer")).isPresent();

        if (canEnable) {
            Customer customer = customerRepository.findById(json.get("customer")).get();
            customer.setEnabled(true);
        }

        return (canEnable) ? ResponseEntity.accepted().build() : ResponseEntity.badRequest().build();
    }

    @PutMapping(path = "/disable")
    public ResponseEntity<Object> disable(@RequestBody Map<String, UUID> json) {

        boolean canDisable = json.get("customer") != null &&
                customerRepository.findById(json.get("customer")).isPresent();

        if (canDisable) {
            Customer customer = customerRepository.findById(json.get("customer")).get();
            customer.setEnabled(false);
        }

        return (canDisable) ? ResponseEntity.accepted().build() : ResponseEntity.badRequest().build();
    }

    @PutMapping(path = "/add_to_site")
    public ResponseEntity<Object> addUserToSite(@RequestBody Map<String, UUID> json) {
        return (link_site_customer(json, siteRepository, customerRepository)) ?
                ResponseEntity.accepted().build() :
                ResponseEntity.badRequest().build();
    }

    @PutMapping(path = "/remove_from_site")
    public ResponseEntity<Object> removeUserFromSite(@RequestBody Map<String, UUID> json) {
        return (unlink_site_customer(json, siteRepository, customerRepository)) ?
                ResponseEntity.accepted().build() :
                ResponseEntity.badRequest().build();
    }

    @PutMapping(path = "/open_or_add_user/{id}")
    public ResponseEntity<Object> openOrAddUser(@PathVariable UUID id, @RequestBody Map<String, UUID> json) {
        boolean canAdd = customerRepository.findById(id).isPresent() &&
                json.get("user") != null && userRepository.findById(json.get("user")).isPresent();

        if (canAdd) {
            Customer customer = customerRepository.findById(id).get();
            User     user     = userRepository.findById(json.get("user")).get();
            if (customer.getCurrentBill() == null) {
                Bill bill = new Bill();
                bill.setConsecutive(0); //TODO: Dar el consecutivo correcto
                bill.setSite(customer.getSite());
                bill.getCustomers().add(customer);
                customer.setCurrentBill(bill);
            }
            Bill_User_Id bill_user_id = new Bill_User_Id();
            bill_user_id.setUser(user);
            bill_user_id.setBill(customer.getCurrentBill());
            Bill_User bill_user = new Bill_User();
            bill_user.setPk(bill_user_id);
            bill_user.setSite(customer.getSite());
            customer.getCurrentBill().getBill_users().add(bill_user);

            billRepository.save(customer.getCurrentBill());
        }

        return (canAdd) ? ResponseEntity.accepted().build() : ResponseEntity.badRequest().build();
    }

    @PutMapping(path = "/add_item/{id}")
    public ResponseEntity<Object> addItem(@PathVariable UUID id, @RequestBody Map<String, String> json) {

        try {
            UUID inventoryItem_id = UUID.fromString(json.get("item"));
            boolean canAdd = customerRepository.findById(id).isPresent() &&
                    inventoryRepository.findById(inventoryItem_id).isPresent() &&
                    (json.get("notes") != null);
            if (canAdd) {
                Customer      customer      = customerRepository.findById(id).get();
                InventoryItem inventoryItem = inventoryRepository.findById(inventoryItem_id).get();
                SalableItem   salableItem   = customer.addItem(inventoryItem, json.get("notes"));
                salableItemRepository.save(salableItem);
                billRepository.save(customer.getCurrentBill());
                customerRepository.save(customer);
            }
            return (canAdd) ? ResponseEntity.accepted().build() : ResponseEntity.badRequest().build();
        } catch (IllegalArgumentException | NullPointerException e) {
            return ResponseEntity.badRequest().build();
        }

    }

    @PutMapping(path = "/remove_item/{id}")
    public ResponseEntity<Object> removeItem(@PathVariable UUID id, @RequestBody Map<String, String> json) {
        boolean canRemove = false; // TODO
        return (canRemove) ? ResponseEntity.accepted().build() : ResponseEntity.badRequest().build();
    }

    @PutMapping(path = "/close/{id}")
    public ResponseEntity<Object> close(@PathVariable UUID id, @RequestBody Map<String, UUID> json) {
        boolean canClose = false; // TODO

        return (canClose) ? ResponseEntity.accepted().build() : ResponseEntity.badRequest().build();
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity delete(@PathVariable UUID id) {
        try {
            customerRepository.deleteById(id);
            return ResponseEntity.accepted().build();
        } catch (IllegalArgumentException ignored) {
            return ResponseEntity.badRequest().build();
        }

    }

}
