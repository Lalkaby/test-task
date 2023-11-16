package by.temniakov.testtask.api.controllers;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api")
public class OrderController {
    public static final String GET_ADDRESS = "/address/{id_address}";
    public static final String GET_ADDRESSES = "/addresses";
    public static final String CREATE_ADDRESS = "/addresses";
    public static final String DELETE_ADDRESS = "/addresses/{id_address}";
}
