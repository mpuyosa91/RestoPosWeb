package com.mpuyosa91.posaplications.RestoPosWeb._01_Controllers.Place;

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
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class GeneralController {

    public static  boolean       springReady   = false;
    private static RestTemplate  restTemplate  = new RestTemplate();
    private static LocalSettings localSettings = new LocalSettings();
    private static HttpHeaders   headers       = new HttpHeaders();
    private static Site          site;

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

    public static String getConfigurationValue(String configurationValue) {
        return "";
    }

    public static InventoryItem getInventoryItem(int serial) {
        String host = localSettings.getProperty("host");
        String port = localSettings.getProperty("port");
        InventoryItem aux = restTemplate.postForObject(
                "http://" + host + ":" + port + "/inventory_item/",
                new InventoryItem(),
                InventoryItem.class);
        return aux;
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

    public static SalableItem getSalable(int serial) {
        return null;
    }

    public static InventoryItem getMainProduct() {
        return null;
    }

    public static InventoryItem getMainMenuPlate() {
        return null;
    }

    public static Set<InventoryItem> getChilds(InventoryItem inventoryItem) {
        return new HashSet<>();
    }

    public static Bill saveBill(ICustomer customer) {
        return null;
    }

    public static Bill printBill(ICustomer customer) {
        return null;
    }

    public static boolean sendToKitchen(ICustomer cliente) {
        return true;
    }

    public static Set<Customer> getCustomerList() {
        return GeneralController.getSite(site.getId()).getCustomers();
    }

    public static User getUser(String user, String pass) {
        return null;
    }

    public static User getUser(String user) {
        return null;
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

}
