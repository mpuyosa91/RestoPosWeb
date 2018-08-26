package com.mpuyosa91.posaplications.RestoPosWeb._01_Controllers.Place;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Accounting.Bill;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Crew.User;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Customers.Customer;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Customers.ICustomer;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.ProductsAndSupplies.InventoryItem;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.ProductsAndSupplies.SalableItem;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Site;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.SettingsAndProperties.LocalSettings;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

public class GeneralController {

    public static  boolean       springReady   = false;
    private static RestTemplate  restTemplate  = new RestTemplate();
    private static LocalSettings localSettings = new LocalSettings();
    private static HttpHeaders   headers       = new HttpHeaders();
    private static Site          site;

    //--------------------------------------------------------------------------------------------------
    //                                             Sites
    //--------------------------------------------------------------------------------------------------

    public static void createSite(Site site) {
        String host = localSettings.getProperty("host");
        String port = localSettings.getProperty("port");
        UUID site_id = restTemplate.exchange(
                "http://" + host + ":" + port + "/site/",
                HttpMethod.POST,
                new HttpEntity<>(site, headers),
                UUID.class
        ).getBody();
        assert site_id != null;
        localSettings.setProperty("site_id", site_id.toString());
    }

    public static Site getSite(UUID site_id) {
        String host = localSettings.getProperty("host");
        String port = localSettings.getProperty("port");
        try {
            site = restTemplate.getForObject(
                    "http://" + host + ":" + port + "/site/" + site_id.toString(),
                    Site.class
            );
        } catch (RestClientException ignored) {
            site = null;
        }
        return site;
    }

    //--------------------------------------------------------------------------------------------------
    //                                         Inventory Item
    //--------------------------------------------------------------------------------------------------

    public static void createInventoryItem(InventoryItem inventoryItem) {
        String host = localSettings.getProperty("host");
        String port = localSettings.getProperty("port");
        restTemplate.postForObject(
                "http://" + host + ":" + port + "/site/" + site.getId() + "/create-inventory-item",
                inventoryItem,
                UUID.class);
    }

    public static void updateInventoryItem(InventoryItem inventoryItem) {
        String host = localSettings.getProperty("host");
        String port = localSettings.getProperty("port");
        restTemplate.put(
                "http://" + host + ":" + port + "/site/" + site.getId() + "/update-inventory-item/" + inventoryItem.getId(),
                inventoryItem,
                InventoryItem.class);
    }

    public static InventoryItem getRootInventoryItem(InventoryItem.Type clase) {
        InventoryItem r = null;
        switch (clase) {
            case RawFood:
                r = getInventoryItem(1);
                break;
            case Mixture:
                r = getInventoryItem(2);
                break;
            case Product:
                r = getInventoryItem(3);
                break;
            case MenuPlate:
                r = getInventoryItem(4);
                break;
        }
        return r;
    }

    public static InventoryItem getInventoryItem(int serial) {
        String host = localSettings.getProperty("host");
        String port = localSettings.getProperty("port");
        InventoryItem aux = restTemplate.getForObject(
                "http://" + host + ":" + port + "/inventory_item/" + site.getId() + "/" + serial,
                InventoryItem.class);
        return aux;
    }

    public static int getAviableID(int father_serial) {
        int     min_serial, limit_serial, multiplier;
        boolean exist;

        if (father_serial > 0 && father_serial < 10) multiplier = 10;
        else multiplier = 100;

        min_serial = father_serial * multiplier;
        limit_serial = (father_serial + 1) * multiplier;

        ArrayList<InventoryItem> childList = getChildes(getInventoryItem(father_serial));

        do {
            min_serial += 1;
            exist = false;
            for (InventoryItem child : childList) {
                if (child.getSerial() == min_serial) {
                    exist = true;
                    break;
                }
            }
        } while (exist);

        if (min_serial > limit_serial) min_serial = -1;

        return min_serial;
    }

    public static ArrayList<InventoryItem> getChildes(InventoryItem inventoryItem) {
        String host = localSettings.getProperty("host");
        String port = localSettings.getProperty("port");

        JsonNode                 jsonNode;
        ArrayList<InventoryItem> inventoryItems = new ArrayList<>();

        try {
            jsonNode = restTemplate.getForObject(
                    "http://" + host + ":" + port + "/inventory_item/"
                            + site.getId() + "/" + inventoryItem.getSerial() + "/childes",
                    JsonNode.class);
        } catch (NullPointerException | HttpServerErrorException ignored) {
            return inventoryItems;
        }

        if (jsonNode != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                inventoryItems = objectMapper.readValue(
                        objectMapper.treeAsTokens(jsonNode),
                        new TypeReference<ArrayList<InventoryItem>>() {
                        });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        inventoryItems.sort(Comparator.comparingInt(InventoryItem::getSerial));

        return inventoryItems;
    }

    public static InventoryItem getFather(InventoryItem inventoryItem) {
        String host = localSettings.getProperty("host");
        String port = localSettings.getProperty("port");

        InventoryItem father = restTemplate.getForObject(
                "http://" + host + ":" + port + "/inventory_item/" + site.getId() +
                        "/" + inventoryItem.getSerial() + "/father",
                InventoryItem.class);

        return father;
    }

    public static String getProductUsages(InventoryItem inventoryItem) {
        return "";
    }

    public static String treeToString(InventoryItem tree) {
        return treeToString(tree, "", 0);
    }

    public static boolean addItemToCustomer(ICustomer cliente, User user, InventoryItem item, String notes) {

        Customer customer = (Customer) cliente;
        String   host     = localSettings.getProperty("host");
        String   port     = localSettings.getProperty("port");

        Map<String, String> addItemToCustomer = new HashMap<>();
        addItemToCustomer.put("item", item.getId().toString());
        addItemToCustomer.put("notes", notes);

        restTemplate.put(
                "http://" + host + ":" + port + "/customer/" + customer.getId() + "/user_add_item/" + user.getId(),
                addItemToCustomer,
                ResponseEntity.class);

        return true;
    }

    public static boolean addItemToCustomer(ICustomer cliente, User user, SalableItem salableItem) {

        Customer customer = (Customer) cliente;
        String   host     = localSettings.getProperty("host");
        String   port     = localSettings.getProperty("port");

        restTemplate.put(
                "http://" + host + ":" + port + "/customer/" + customer.getId() + "/user_add_salable/" + user.getId(),
                salableItem,
                ResponseEntity.class);

        return true;
    }

    public static void removeItemFromCustomer(Customer customer, SalableItem salableItem) {
        String host = localSettings.getProperty("host");
        String port = localSettings.getProperty("port");

        restTemplate.put(
                "http://" + host + ":" + port + "/customer/" + customer.getId() + "/remove_salable/" + salableItem.getId(),
                ResponseEntity.class);

    }

    private static String treeToString(InventoryItem tree, String r, int lvl) {
        r = r + "\n" + spaces(lvl) + getDescription(tree);
        for (InventoryItem child : GeneralController.getChildes(tree)) {
            r = treeToString(child, r, String.valueOf(tree.getSerial()).length() + 4 + lvl);
        }
        return r;
    }

    //--------------------------------------------------------------------------------------------------
    //                                               User
    //--------------------------------------------------------------------------------------------------

    public static User getUser(String username) {
        String host = localSettings.getProperty("host");
        String port = localSettings.getProperty("port");

        return restTemplate.getForObject(
                "http://" + host + ":" + port + "/site/" + site.getId().toString() +
                        "/search-user-by-username/" + username,
                User.class
        );
    }

    //--------------------------------------------------------------------------------------------------
    //                                            Customers
    //--------------------------------------------------------------------------------------------------

    public static Customer getCustomer(UUID customer_id) {
        String host = localSettings.getProperty("host");
        String port = localSettings.getProperty("port");

        Customer customer = restTemplate.getForObject(
                "http://" + host + ":" + port + "/customer/" + customer_id,
                Customer.class);

        Site customer_site = restTemplate.getForObject(
                "http://" + host + ":" + port + "/customer/" + customer_id + "/get_site_of",
                Site.class);

        customer.setSite(customer_site);

        return customer;
    }

    public static Set<Customer> getCustomerList() {
        Set<Customer> customerSet = GeneralController.getSite(site.getId()).getCustomers();
        for (Customer customer : customerSet) {
            customer.setSite(site);
        }
        return customerSet;
    }

    //--------------------------------------------------------------------------------------------------
    //                                              Bills
    //--------------------------------------------------------------------------------------------------

    public static Bill saveBill(ICustomer customer) {
        return null;
    }

    public static void delete(InventoryItem inventoryItem) {

    }

    public static Bill printBill(ICustomer customer) {
        return null;
    }

    public static boolean sendToKitchen(ICustomer cliente) {
        return true;
    }

    //--------------------------------------------------------------------------------------------------
    //                                          Configuration
    //--------------------------------------------------------------------------------------------------

    public static String getConfigurationValue(String configurationValue) {
        return "";
    }

    private static String spaces(int spaces) {
        StringBuilder r = new StringBuilder();
        for (int i = 0; i < spaces; i++)
            r.append(" ");
        return r.toString();
    }

    private static String getDescription(InventoryItem inventoryItem) {
        String r;
        if (inventoryItem.isFinal_item())
            r = "<" + inventoryItem.getSerial() + "> " + inventoryItem.getName() + " $" + String.valueOf((int) inventoryItem.getPrice());
        else
            r = "<" + inventoryItem.getSerial() + "> " + inventoryItem.getName() + ":";
        return r;
    }

    public enum Label {
        NroMesas(10),
        ConsecutivoFacturas(1),
        ConsecutivoExterno(6),
        FechaUltimaFactura(20170101),
        ConsecutivoComandas(1),
        ClienteExternoAutent(4733),
        TurnoActual(1);

        private final int value;

        Label(int value) {
            this.value = value;
        }

        int getValue() {
            return value;
        }
    }


    private static class InventoryItemList {

        private ArrayList<InventoryItem> inventoryItems;

        public InventoryItemList() {
            inventoryItems = new ArrayList<>();
        }

        public List<InventoryItem> getInventoryItems() {
            return inventoryItems;
        }

        public void setInventoryItems(ArrayList<InventoryItem> inventoryItems) {
            this.inventoryItems = inventoryItems;
        }

        @Override
        public String toString() {
            return "InventoryItemList{" +
                    "inventoryItems=" + inventoryItems +
                    '}';
        }
    }
}
