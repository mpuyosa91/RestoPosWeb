package com.mpuyosa91.posaplications.RestoPosWeb._01_Controllers.Services;

import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Repositories.BillRepository;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Repositories.SiteRepository;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping(path = "/bill")
public class BillController {

    final UserRepository userRepository;
    final SiteRepository siteRepository;
    final BillRepository billRepository;

    @Autowired
    public BillController(UserRepository userRepository,
                          SiteRepository siteRepository,
                          BillRepository billRepository) {
        this.userRepository = userRepository;
        this.siteRepository = siteRepository;
        this.billRepository = billRepository;
    }

    @GetMapping("/consecutive/{id}")
    public Integer getBillConsecutiveFromSite(@PathVariable UUID id) {
        boolean canGet = siteRepository.findById(id).isPresent();

        if (canGet) {
            int consecutive = billRepository.countBillsOfSite(id);
            return consecutive + 1;
        } else {
            return -1;
        }

    }

}
