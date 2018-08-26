package com.mpuyosa91.posaplications.RestoPosWeb._01_Controllers.Services;

import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Repositories.BillRepository;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Repositories.SiteRepository;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Repositories.UserRepository;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.SettingsAndProperties.LocalSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

@Controller
@RequestMapping(path = "/bill")
public class BillController {

    private final UserRepository userRepository;
    private final SiteRepository siteRepository;
    private final BillRepository billRepository;

    @Autowired
    public BillController(UserRepository userRepository,
                          SiteRepository siteRepository,
                          BillRepository billRepository) {
        this.userRepository = userRepository;
        this.siteRepository = siteRepository;
        this.billRepository = billRepository;
    }

    @GetMapping("/consecutive/{id}")
    public @ResponseBody
    Integer getBillConsecutiveFromSite(@PathVariable UUID id) {
        LocalSettings localSettings = new LocalSettings();

        int r = (localSettings.getProperty("bill_consecutive") == null &&
                siteRepository.findById(id).isPresent()) ?
                (billRepository.countBillsOfSite(id) + 1) :
                Integer.parseInt(localSettings.getProperty("bill_consecutive")) + 1;

        localSettings.setProperty("bill_consecutive", Integer.toString(r));

        return r;
    }

}
