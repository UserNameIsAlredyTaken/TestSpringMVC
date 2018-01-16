package ru.battlefield.my.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.battlefield.my.domain.Camo;
import ru.battlefield.my.service.CamoService;

/**
 * Created by danil on 09.01.2018.
 */

@Controller
public class MainController {
    private CamoService camoService;
    @Autowired(required = true)
    public void setCamoService(CamoService camoService) {
        this.camoService = camoService;
    }

    @RequestMapping(value = "camos", method = RequestMethod.GET)
    public String listCamos(Model model){
        model.addAttribute("listCamos", this.camoService.findAll());
        model.addAttribute("camo", new Camo());
        return "camos";
    }



}
