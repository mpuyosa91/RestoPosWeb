package com.mpuyosa91.posaplications.RestoPosWeb._01_Controllers.Services;

import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.ProductsAndSupplies.InventoryItem;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Repositories.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping(path = "/inventory_item")
public class InventoryController {

    private final InventoryRepository inventoryRepository;

    @Autowired
    public InventoryController(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @GetMapping(path = "/add")
    public @ResponseBody
    String create(@RequestParam String name) {
        InventoryItem inventory = new InventoryItem();
        inventory.setName(name);

        inventoryRepository.save(inventory);

        return "saved";
    }

    @GetMapping(path = "/all")
    public @ResponseBody
    Iterable<InventoryItem> readAllEnabled() {
        return inventoryRepository.findAllEnabled();
    }

    @GetMapping(path = "/all-all")
    public @ResponseBody
    Iterable<InventoryItem> readAll() {
        return inventoryRepository.findAll();
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity delete(@PathVariable UUID id) {
        try {
            inventoryRepository.deleteById(id);
            return ResponseEntity.accepted().build();
        } catch (IllegalArgumentException ignored) {
            return ResponseEntity.badRequest().build();
        }

    }

    @PostMapping(path = "/enable")
    public ResponseEntity<Object> enable(@RequestBody Map<String, UUID> json) {

        boolean canEnable = json.get("inventoryitem") != null &&
                inventoryRepository.findById(json.get("inventoryitem")).isPresent();

        if (canEnable) {
            InventoryItem inventoryitem = inventoryRepository.findById(json.get("inventoryitem")).get();
            inventoryitem.setEnabled(true);
        }

        return (canEnable) ? ResponseEntity.accepted().build() : ResponseEntity.badRequest().build();
    }

    @PostMapping(path = "/disable")
    public ResponseEntity<Object> disable(@RequestBody Map<String, UUID> json) {

        boolean canDisable = json.get("inventoryitem") != null &&
                inventoryRepository.findById(json.get("inventoryitem")).isPresent();

        if (canDisable) {
            InventoryItem inventoryitem = inventoryRepository.findById(json.get("inventoryitem")).get();
            inventoryitem.setEnabled(false);
        }

        return (canDisable) ? ResponseEntity.accepted().build() : ResponseEntity.badRequest().build();
    }

}
