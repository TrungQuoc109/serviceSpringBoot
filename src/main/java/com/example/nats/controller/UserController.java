package com.example.nats.controller;

import com.example.nats.Repository.AccountRepository;
import com.example.nats.model.Account;
import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Nats;
import io.nats.client.Options;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

//@CrossOrigin("${applicationPath}")
@RestController
@RequestMapping("/user")
public class UserController {
    private final AccountRepository accountRepository;
    @Autowired
    public UserController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

@PostMapping("/login")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> login(@RequestBody Account account) throws IOException, InterruptedException {
   try{
       Map<String, Object> user = new HashMap<>();
       Account acc = accountRepository.findByUsername(account.getUsername());
       if( acc==null || !acc.getPassword().equals(account.getPassword())) {
           user.put("message","UNAUTHORIZED");
           return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(user);
       }

       Options options = new Options.Builder().server("nats://localhost:4222").build();
       Connection nc = Nats.connect(options);
       String subject = "greetings";
       String message = "Hello from Spring Boot!";
       nc.publish(subject, message.getBytes());
       nc.close();
       user.put("userId",acc.getId());
       user.put("message",message);
       return ResponseEntity.status(HttpStatus.OK).body(user);
   }
   catch(Exception e){
        System.out.println(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new HashMap<>());
    }
    }
}

