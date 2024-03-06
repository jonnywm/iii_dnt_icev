package br.com.icev.dnt.iii.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author jonny
 */
@RestController
@RequestMapping("/status")
public class StatusController {

    @GetMapping
    public String okGuy(){
        return "It's ok guys";
    }
}
