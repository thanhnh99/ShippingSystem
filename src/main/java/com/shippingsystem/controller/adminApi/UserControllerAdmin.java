package com.shippingsystem.controller.adminApi;

import com.shippingsystem.models.response.ResponseBaseModel;
import com.shippingsystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("admin/user")
public class UserControllerAdmin {

    @Autowired
    UserService userService;
    @PostMapping("shipper")
    public ResponseEntity addShipper(@RequestBody String userId)
    {
        ResponseBaseModel response = new ResponseBaseModel();
        response = userService.addNewShipper(userId);

        if(response.getStatusCode().equals("200")) return ResponseEntity.ok(response);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
