package com.careem.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/ping")
public class InfoController {
    @RequestMapping(value = "/ping", method = GET)
    @ResponseStatus(HttpStatus.OK)
    public void ping() {
    }

}
