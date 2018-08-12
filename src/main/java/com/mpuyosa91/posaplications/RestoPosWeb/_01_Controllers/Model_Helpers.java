package com.mpuyosa91.posaplications.RestoPosWeb._01_Controllers;

import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Crew.User;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Customers.Customer;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Customers.CustomerTable;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Site;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Repositories.CustomerRepository;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Repositories.InventoryRepository;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Repositories.SiteRepository;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Repositories.UserRepository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;
import java.util.UUID;

public class Model_Helpers {

    public static boolean link_customer_customer(@RequestBody Map<String, UUID> json,
                                                 CustomerRepository customerRepository) {
        boolean canLink = json.get("master") != null && json.get("slaved") != null &&
                customerRepository.findById(json.get("master")).isPresent() &&
                customerRepository.findById(json.get("slaved")).isPresent();

        if (canLink) {
            CustomerTable masterCustomerTable = (CustomerTable) customerRepository.findById(json.get("master")).get();
            CustomerTable slavedCustomerTable = (CustomerTable) customerRepository.findById(json.get("slaved")).get();

            masterCustomerTable.link(true, slavedCustomerTable);

            customerRepository.save(masterCustomerTable);
            customerRepository.save(slavedCustomerTable);
        }
        return canLink;
    }

    public static boolean link_site_customer(@RequestBody Map<String, UUID> json,
                                             SiteRepository siteRepository,
                                             CustomerRepository customerRepository) {
        boolean canAdd = json.get("site") != null && json.get("customer") != null &&
                siteRepository.findById(json.get("site")).isPresent() &&
                customerRepository.findById(json.get("customer")).isPresent();
        if (canAdd) {
            Site     site     = siteRepository.findById(json.get("site")).get();
            Customer customer = customerRepository.findById(json.get("customer")).get();

            site.getCustomers().add(customer);
            customer.setSite(site);

            siteRepository.save(site);
            customerRepository.save(customer);
        }
        return canAdd;
    }

    public static boolean link_site_user(@RequestBody Map<String, UUID> json,
                                         UserRepository userRepository,
                                         SiteRepository siteRepository) {
        boolean canAdd = json.get("user") != null && json.get("site") != null &&
                userRepository.findById(json.get("user")).isPresent() &&
                siteRepository.findById(json.get("site")).isPresent();
        if (canAdd) {
            User user = userRepository.findById(json.get("user")).get();
            Site site = siteRepository.findById(json.get("site")).get();
            user.getSites().add(site);
            site.getUsers().add(user);
            userRepository.save(user);
            siteRepository.save(site);
        }
        return canAdd;
    }

    public static boolean unlink_customer_customer(@RequestBody Map<String, UUID> json,
                                                   CustomerRepository customerRepository) {
        boolean canUnlink = json.get("customer") != null &&
                customerRepository.findById(json.get("customer")).isPresent();

        if (canUnlink) {
            CustomerTable customerTable = (CustomerTable) customerRepository.findById(json.get("customer")).get();

            customerTable.unlink();

            customerRepository.save(customerTable);
        }
        return canUnlink;
    }

    public static boolean unlink_site_customer(@RequestBody Map<String, UUID> json,
                                               SiteRepository siteRepository,
                                               CustomerRepository customerRepository) {
        boolean canRemove = json.get("site") != null && json.get("customer") != null &&
                siteRepository.findById(json.get("site")).isPresent() &&
                customerRepository.findById(json.get("customer")).isPresent();
        if (canRemove) {
            Site     site     = siteRepository.findById(json.get("site")).get();
            Customer customer = customerRepository.findById(json.get("customer")).get();
            site.getCustomers().remove(customer);
            customer.setSite(null);
            siteRepository.save(site);
            customerRepository.save(customer);
        }
        return canRemove;
    }

    public static boolean unlink_site_user(@RequestBody Map<String, UUID> json,
                                           UserRepository userRepository,
                                           SiteRepository siteRepository) {
        boolean canRemove = json.get("user") != null && json.get("site") != null &&
                userRepository.findById(json.get("user")).isPresent() &&
                siteRepository.findById(json.get("site")).isPresent();
        if (canRemove) {
            User user = userRepository.findById(json.get("user")).get();
            Site site = siteRepository.findById(json.get("site")).get();
            user.getSites().remove(site);
            site.getUsers().remove(user);
            userRepository.save(user);
            siteRepository.save(site);
        }
        return canRemove;
    }

    public static boolean add_inventoryitem_to_customer(@RequestBody Map<String, UUID> json,
                                                        CustomerRepository customerRepository,
                                                        InventoryRepository inventoryRepository) {
        return true;
    }

    public static boolean remove_inventoryitem_from_customer(@RequestBody Map<String, UUID> json,
                                                             CustomerRepository customerRepository,
                                                             InventoryRepository inventoryRepository) {
        return true;
    }
}
