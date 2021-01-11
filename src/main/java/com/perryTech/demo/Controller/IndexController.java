package com.perryTech.demo.Controller;

import com.perryTech.demo.Models.CoronaStats;
import com.perryTech.demo.Services.CoronaDataServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
public class IndexController {

    @Autowired
    CoronaDataServices coronaDataServices;

    @GetMapping("/")
    public String index(Model model){
        List<CoronaStats> allStats = coronaDataServices.getAllStats();
        int totalReportedCases = allStats.stream().mapToInt(CoronaStats::getNewTotalCases).sum();
        int totalNewCases = allStats.stream().mapToInt(CoronaStats::getDifferenceFromPreviousDay).sum();
        model.addAttribute("coronaStats", coronaDataServices.getAllStats());
        model.addAttribute("totalNewCases", totalNewCases);
        model.addAttribute("totalReportedCases", totalReportedCases);
        return "index";
    }
}
