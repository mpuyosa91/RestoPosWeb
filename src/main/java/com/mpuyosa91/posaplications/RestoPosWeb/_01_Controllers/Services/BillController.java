package com.mpuyosa91.posaplications.RestoPosWeb._01_Controllers.Services;

import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Accounting.Bill;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.ProductsAndSupplies.SalableItem;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Repositories.BillRepository;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Repositories.SalableItemRepository;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Repositories.SiteRepository;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Repositories.UserRepository;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.SettingsAndProperties.LocalSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Controller
@RequestMapping(path = "/bill")
public class BillController {

    private final UserRepository        userRepository;
    private final SiteRepository        siteRepository;
    private final BillRepository        billRepository;
    private final SalableItemRepository salableItemRepository;

    @Autowired
    public BillController(UserRepository userRepository,
                          SiteRepository siteRepository,
                          BillRepository billRepository,
                          SalableItemRepository salableItemRepository) {
        this.userRepository = userRepository;
        this.siteRepository = siteRepository;
        this.billRepository = billRepository;
        this.salableItemRepository = salableItemRepository;
    }

    @GetMapping("/consecutive/{id}")
    public @ResponseBody
    Integer getBillConsecutiveFromSite(@PathVariable UUID id) {
        LocalSettings localSettings = new LocalSettings();

        int r = (
                localSettings.getProperty("bill_consecutive") == null && siteRepository.findById(id).isPresent()
        ) ? (
                billRepository.countBillsOfSite(id) + 1
        ) : Integer.parseInt(localSettings.getProperty("bill_consecutive")) + 1;

        localSettings.setProperty("bill_consecutive", Integer.toString(r));

        return r;
    }

    @GetMapping("/of/{site_id}/all")
    public @ResponseBody
    Iterable<Bill> getall(@PathVariable("site_id") UUID site_id) {
        boolean canSearch = siteRepository.findById(site_id).isPresent();

        if (canSearch) {
            return billRepository.getAllBy(site_id);
        } else {
            return null;
        }
    }

    @GetMapping("{bill_id}")
    public @ResponseBody
    Bill get(@PathVariable UUID bill_id) {
        return billRepository.findById(bill_id).get();
    }

    @GetMapping("/of/{site_id}/from/{start}/to/{end}")
    public @ResponseBody
    Iterable<Bill> getFromTo(@PathVariable("site_id") UUID site_id,
                             @PathVariable("start") int start,
                             @PathVariable("end") int end) {
        boolean canSearch = siteRepository.findById(site_id).isPresent() && start < end;

        if (canSearch) {
            Calendar start_calendar = Calendar.getInstance();
            start_calendar.setTime(new Date(((long) start) * 1000L));
            Calendar end_calendar = Calendar.getInstance();
            end_calendar.setTime(new Date(((long) end) * 1000L));
            return billRepository.findBillsOfSiteInRange(site_id, start_calendar, end_calendar);
        } else {
            return null;
        }
    }

    @DeleteMapping("/of/{site_id}/clear")
    public ResponseEntity clearBills(@PathVariable("site_id") UUID site_id) {
        billRepository.deleteAllBy(site_id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/salable_item/{salable_id}")
    @ResponseBody
    SalableItem getSalableItem(@PathVariable UUID salable_id) {
        return salableItemRepository.findById(salable_id).get();
    }

}
