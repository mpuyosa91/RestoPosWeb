package com.mpuyosa91.posaplications.RestoPosWeb._01_Controllers.Services;

import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Accounting.Bill;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Customers.Customer;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Customers.CustomerTable;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Customers.ExternalCustomer;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Customers.PointOfService;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.ProductsAndSupplies.InventoryItem;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.ProductsAndSupplies.SalableItem;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Printers.PrinterModel;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Repositories.*;
import com.mpuyosa91.posaplications.RestoPosWeb._01_Controllers.Model_Helpers;
import com.mpuyosa91.posaplications.RestoPosWeb._01_Controllers.Place.PrinterController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Map;
import java.util.UUID;

import static com.mpuyosa91.posaplications.RestoPosWeb._01_Controllers.Model_Helpers.link_site_customer;
import static com.mpuyosa91.posaplications.RestoPosWeb._01_Controllers.Model_Helpers.unlink_site_customer;
import static java.lang.Math.round;

@Controller
@RequestMapping(path = "/customer")
public class CustomerController {

    private static final PrinterModel PRINTER = new PrinterModel();

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
            customerRepository.save(customer);
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
            customerRepository.save(customer);
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

    @PutMapping(path = "/add_item/{id}")
    public ResponseEntity<Object> addItem(@PathVariable UUID id, @RequestBody Map<String, String> json) {

        try {

            UUID inventoryItem_id = UUID.fromString(json.get("item"));
            UUID user_id          = UUID.fromString(json.get("user"));
            boolean canAdd = customerRepository.findById(id).isPresent() &&
                    userRepository.findById(user_id).isPresent() &&
                    inventoryRepository.findById(inventoryItem_id).isPresent() &&
                    (json.get("notes") != null);

            // TODO: Verificar que el usuario y el item si sean de la sede.

            if (canAdd) {

                Customer customer = customerRepository.findById(id).get();
                SalableItem salableItem = new SalableItem(
                        inventoryRepository.findById(inventoryItem_id).get(),
                        json.get("notes"));
                customer.addItem(
                        userRepository.findById(user_id).get(),
                        salableItem);

                customerRepository.save(customer);
                salableItemRepository.save(salableItem);

            }
            return (canAdd) ? ResponseEntity.accepted().build() : ResponseEntity.badRequest().build();

        } catch (IllegalArgumentException | NullPointerException e) {
            return ResponseEntity.badRequest().build();
        }

    }

    @PutMapping(path = "/remove_item/{id}")
    public ResponseEntity<Object> removeItem(@PathVariable UUID id, @RequestBody Map<String, String> json) {
        try {

            UUID salableItem_Id = UUID.fromString(json.get("item"));
            boolean canRemove = customerRepository.findById(id).isPresent() &&
                    salableItemRepository.findById(salableItem_Id).isPresent() &&
                    (json.get("notes") != null);

            if (canRemove) {

                Customer    customer    = customerRepository.findById(id).get();
                SalableItem salableItem = salableItemRepository.findById(salableItem_Id).get();

                boolean wasRemoved = customer.removeItem(salableItem);

                if (wasRemoved) salableItemRepository.deleteById(salableItem_Id);
                customerRepository.save(customer);

                return (wasRemoved) ? ResponseEntity.accepted().build() : ResponseEntity.badRequest().build();

            }

            return ResponseEntity.badRequest().build();

        } catch (IllegalArgumentException | NullPointerException e) {

            return ResponseEntity.badRequest().build();

        }
    }

    @PutMapping(path = "/sent_to_kitchen/{id}")
    public ResponseEntity<Object> sendToKitchen(@PathVariable UUID id, @RequestBody Map<String, UUID> json) {
        try {

            boolean canSend = customerRepository.findById(id).isPresent() &&
                    json.get("kitchen") != null;

            if (canSend) {

                Customer customer = customerRepository.findById(id).get();

                printCommand(customer, "CognitiveTPG Receipt");

                // UUID kitchen_id = UUID.fromString(json.get("kitchen"));
                // Kitchen kitchen = kitchenRepository.findById(kitchen_id).get();
                // kitchen.sendToKitchen(customer,kitchen);
                // kitchenRepository.save(kitchen);

                customerRepository.save(customer);
            }

            return (canSend) ? ResponseEntity.accepted().build() : ResponseEntity.badRequest().build();

        } catch (IllegalArgumentException | NullPointerException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(path = "/close/{id}")
    public ResponseEntity<Object> close(@PathVariable UUID id, @RequestBody Map<String, UUID> json) {
        boolean canClose = customerRepository.findById(id).isPresent() &&
                json.get("pos") != null;

        if (canClose) {
            Customer customer = customerRepository.findById(id).get();
            if (customer.isOccupied()) {
                Bill bill = customer.evacuate();

                // Enviar todos a la cocina (y por consiguiente facturarlos)
                // UUID kitchen_id = UUID.fromString(json.get("kitchen"));
                // Kitchen kitchen = kitchenRepository.findById(kitchen_id).get();
                // kitchen.sendToKitchen(customer,kitchen);
                // kitchenRepository.save(kitchen);

                // Imprimir la factura
                // UUID pos_id = UUID.fromString(json.get("pos"));
                // POS pos = posRepository.findById(pos_id).get();
                // pos.printBill(customer,pos);
                // posRepository.save(pos);

                customer.clean();

                customerRepository.save(customer);
                billRepository.save(bill);
            }
        }

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

    private boolean printCommand(Customer customer, String printerName) {
        boolean valid = false, r;
        PRINTER.resetAll();
        PRINTER.initialize();
        PRINTER.feedBack((byte) 2);
        PRINTER.setFont(5, false);
        PRINTER.setTextCenter("COMANDA");
        PRINTER.newLine();
        PRINTER.setFont(2, true);
        PRINTER.setTextCenter("The Panera");
        PRINTER.newLine();
        PRINTER.setTextCenter("Bakery and Food");
        PRINTER.newLine();
        PRINTER.setFont(4, false);
        PRINTER.addLineSeperator();
        PRINTER.newLine();
        PRINTER.setTextLeft(PrinterController.dateTimeHeader(Calendar.getInstance()));
        PRINTER.setTextLeft(customer.getIdentifier());
        PRINTER.newLine();
        int commandNumber = siteRepository.getCommandNumber(customer.getSite().getId());
        PRINTER.setTextLeft("Comanda:" + " " + commandNumber);
        PRINTER.newLine();
        PRINTER.setTextLeft("Tiempo Clientes: " +
                                    String.valueOf(round((float) customer.getDurationInSeconds() / 60)) + " Min.");
        PRINTER.newLine();
        PRINTER.addLineSeperator();
        PRINTER.newLine();
        PRINTER.setTextCenter(" - Ordenes - ");
        PRINTER.newLine();
        PRINTER.addLineSeperator();
        PRINTER.newLine();

        int lineLimit = 39;
        for (SalableItem salableItem : customer.getItemListUnBilled()) {
            if (salableItem.getType() == InventoryItem.Type.MenuPlate) {
                valid = true;
                PRINTER.setTextLeft(" (*)" + salableItem.getName());
                PRINTER.newLine();
                String notes = salableItem.getNotes();
                while (notes.length() > 0) {
                    if (notes.length() > lineLimit) {
                        PRINTER.setTextLeft("    " + notes.substring(0, lineLimit - 1));
                        notes = notes.substring(lineLimit);
                    } else {
                        PRINTER.setTextLeft("    " + notes);
                        notes = "";
                    }
                    PRINTER.newLine();
                }
                PRINTER.newLine();
            }
        }

        PRINTER.addLineSeperator();
        PRINTER.newLine();
        PRINTER.setTextCenter("Fin Comanda");
        PRINTER.feed((byte) 3);
        PRINTER.finit();

        if (valid)
            //"Kitchen Receipt";
            r = PrinterModel.feedPrinter(PRINTER.finalCommandSet().getBytes(), printerName);
        else r = false;

        if (r) {
            customer.billAllItems();
            if (commandNumber >= 99) commandNumber = 0;
            siteRepository.setCommandNumber(customer.getSite().getId(), commandNumber + 1);
        }

        return r;
    }

    private boolean openTrack(String printerName) {
        PRINTER.resetAll();
        PRINTER.initialize();
        PRINTER.finitOnlyDrawer();
        return PrinterModel.feedPrinter(PRINTER.finalCommandSet().getBytes(), printerName);
        //"CognitiveTPG Receipt"
    }

    private boolean printBill(Bill bill, String printerName) {
        String line44;
        int idLen = 5,
                priceLen = 6,
                quantLen = 4,
                subTLen = 7,
                itemLen = PRINTER.getLineSize() - (idLen + priceLen + quantLen + subTLen);
        PRINTER.resetAll();
        PRINTER.initialize();
        PRINTER.feedBack((byte) 2);
        PRINTER.setFont(4, true);
        PRINTER.setTextLeft(PrinterController.billHeader());
        PRINTER.setFont(2, false);
        PRINTER.addLineSeperator();
        PRINTER.newLine();
        PRINTER.setTextRight(String.valueOf(bill.getConsecutive()));
        PRINTER.newLine();
        PRINTER.setTextLeft(PrinterController.dateTimeHeader(Calendar.getInstance()));
        Customer customer = bill.getFirstCustomer();
        PRINTER.setTextLeft(customer.getIdentifier());
        PRINTER.newLine();
        PRINTER.addLineSeperator();
        PRINTER.newLine();
        PRINTER.setTextCenter(" - Detalles de Compra - ");
        PRINTER.newLine();
        PRINTER.addLineSeperator();
        PRINTER.newLine();
        line44 = PrinterController
                .stringToLeftAndFill("Nro", idLen - 1, "fill")
                .concat(" ");
        line44 += PrinterController
                .stringToLeftAndFill("Item", itemLen - 1, "truncate")
                .concat(" ");
        line44 += PrinterController
                .stringToRightAndFill("Prec", priceLen - 1, "fill")
                .concat(" ");
        line44 += PrinterController
                .stringToRightAndFill("#", quantLen - 1, "fill")
                .concat(" ");
        line44 += PrinterController
                .stringToRightAndFill("SubT.", subTLen, "fill");
        PRINTER.setTextLeft(line44);
        PRINTER.newLine();
        PRINTER.addLineSeperator();
        PRINTER.newLine();

        for (Map.Entry<UUID, Integer> entry : bill.getSalableQuantityMap().entrySet()) {

            SalableItem salableItem = bill.getSalableItemMap().get(entry.getKey());

            String id = String.valueOf(salableItem.getSerial());
            line44 = PrinterController
                    .stringToLeftAndFill(id, idLen - 1, "fill")
                    .concat(" ");
            String name = salableItem.getName();
            line44 += PrinterController
                    .stringToLeftAndFill(name, itemLen - 1, "truncate")
                    .concat(" ");
            String price = String.valueOf((int) salableItem.getPrice());
            line44 += PrinterController
                    .stringToRightAndFill(price, priceLen - 1, "fill")
                    .concat(" ");

            String quantity = String.valueOf((int) entry.getValue());
            line44 += PrinterController
                    .stringToRightAndFill(quantity, quantLen - 1, "fill")
                    .concat(" ");

            String subT = String.valueOf((int) (salableItem.getPrice() * entry.getValue()));
            line44 += PrinterController
                    .stringToRightAndFill(subT, subTLen, "fill");

            PRINTER.setTextLeft(line44);
            PRINTER.newLine();
        }

        PRINTER.addLineSeperator();
        PRINTER.newLine();
        PRINTER.setTextRight("Total: " + bill.getConsumption().intValue());
        PRINTER.newLine();
        PRINTER.addLineSeperator();
        PRINTER.newLine();
        PRINTER.setTextCenter("Gracias por su Compra");
        PRINTER.newLine();
        PRINTER.setTextCenter("Nuestro Placer es Servirle");
        PRINTER.feed((byte) 3);
        PRINTER.finitWithDrawer();
        return PrinterModel.feedPrinter(PRINTER.finalCommandSet().getBytes(), printerName);
    }

}
