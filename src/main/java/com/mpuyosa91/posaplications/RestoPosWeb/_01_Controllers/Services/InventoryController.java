package com.mpuyosa91.posaplications.RestoPosWeb._01_Controllers.Services;

import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.ProductsAndSupplies.InventoryItem;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Site;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Repositories.InventoryRepository;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Repositories.SiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

import static com.mpuyosa91.posaplications.RestoPosWeb._01_Controllers.Model_Helpers.link_site_inventoryitem;
import static com.mpuyosa91.posaplications.RestoPosWeb._01_Controllers.Model_Helpers.unlink_site_inventoryitem;

@Controller
@RequestMapping(path = "/inventory_item")
public class InventoryController {

    private final InventoryRepository inventoryRepository;
    private final SiteRepository      siteRepository;

    @Autowired
    public InventoryController(InventoryRepository inventoryRepository, SiteRepository siteRepository) {
        this.inventoryRepository = inventoryRepository;
        this.siteRepository = siteRepository;
    }

    @PostMapping(path = "/")
    public @ResponseBody
    InventoryItem create(@RequestBody InventoryItem inventoryItem) {
        System.out.println(inventoryItem.toString());
        return inventoryRepository.save(inventoryItem);
    }

    @PostMapping(path = "/{site_id}")
    public @ResponseBody
    InventoryItem create(@RequestBody InventoryItem inventoryItem, @PathVariable UUID site_id) {

        boolean canCreate = siteRepository.findById(site_id).isPresent() &&
                inventoryItem.getSerial() != null && inventoryItem.getName() != null;

        //TODO Buscar por site y por serial que no este creado para poder crear.

        if (canCreate) {
            Site site = siteRepository.findById(site_id).get();
            inventoryItem.setSite(site);
            return inventoryRepository.save(inventoryItem);
        } else {
            return null;
        }

    }

    @GetMapping(path = "/{id}")
    public @ResponseBody
    InventoryItem read(@PathVariable("id") UUID id) {
        return inventoryRepository.findById(id).get();
    }

    @GetMapping(path = "/{site_id}/{serial}")
    public @ResponseBody
    InventoryItem readBySerial(@PathVariable("site_id") UUID site_id, @PathVariable("serial") int serial) {
        return inventoryRepository.findNode(site_id, serial);
    }

    @GetMapping(path = "/{site_id}/{serial}/father")
    public @ResponseBody
    InventoryItem readFather(@PathVariable("site_id") UUID site_id, @PathVariable("serial") int serial) {
        if (serial >= 10 && serial < 100)
            return inventoryRepository.findFirstFather(site_id, serial);
        else
            return inventoryRepository.findFather(site_id, serial);
    }

    @GetMapping(path = "/{site_id}/{serial}/childs")
    public @ResponseBody
    Iterable<InventoryItem> readChilds(@PathVariable("site_id") UUID site_id, @PathVariable("serial") int serial) {
        InventoryItem inventoryItem = inventoryRepository.findNode(site_id, serial);
        if (!inventoryItem.isFinal_item()) {
            if (serial >= 0 && serial < 10)
                return inventoryRepository.findFirstChilds(site_id, serial);
            else
                return inventoryRepository.findChilds(site_id, serial);
        } else return null;
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

    @PutMapping(path = "/")
    public @ResponseBody
    InventoryItem update(@RequestBody InventoryItem inventoryItem) {
        return inventoryRepository.save(inventoryItem);
    }

    @PutMapping(path = "/add_to_site")
    public ResponseEntity<Object> addInventoryItemToSite(@RequestBody Map<String, UUID> json) {
        return (link_site_inventoryitem(json, siteRepository, inventoryRepository)) ?
                ResponseEntity.accepted().build() :
                ResponseEntity.badRequest().build();
    }

    @PutMapping(path = "/remove_from_site")
    public ResponseEntity<Object> removeInventoryItemFromSite(@RequestBody Map<String, UUID> json) {
        return (unlink_site_inventoryitem(json, siteRepository, inventoryRepository)) ?
                ResponseEntity.accepted().build() :
                ResponseEntity.badRequest().build();
    }

    @PutMapping(path = "/enable")
    public ResponseEntity<Object> enable(@RequestBody Map<String, UUID> json) {

        boolean canEnable = json.get("inventoryitem") != null &&
                inventoryRepository.findById(json.get("inventoryitem")).isPresent();

        if (canEnable) {
            InventoryItem inventoryitem = inventoryRepository.findById(json.get("inventoryitem")).get();
            inventoryitem.setEnabled(true);
            inventoryRepository.save(inventoryitem);
        }

        return (canEnable) ? ResponseEntity.accepted().build() : ResponseEntity.badRequest().build();
    }

    @PutMapping(path = "/disable")
    public ResponseEntity<Object> disable(@RequestBody Map<String, UUID> json) {

        boolean canDisable = json.get("inventoryitem") != null &&
                inventoryRepository.findById(json.get("inventoryitem")).isPresent();

        if (canDisable) {
            InventoryItem inventoryitem = inventoryRepository.findById(json.get("inventoryitem")).get();
            inventoryitem.setEnabled(false);
            inventoryRepository.save(inventoryitem);
        }

        return (canDisable) ? ResponseEntity.accepted().build() : ResponseEntity.badRequest().build();
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

}
