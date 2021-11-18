package com.battleface.challengeDemo.controller;

import com.battleface.challengeDemo.model.request;
import com.battleface.challengeDemo.model.response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class quotationController {
    @PostMapping(path = "quotation",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> GetQuotation(@RequestBody request body) throws ParseException {
        Map<String, String> myerror = validateRequestParameter(body);
        if(myerror != null)
            return new ResponseEntity<Object>(myerror, HttpStatus.OK);;
        response myResponse = new response();
        myResponse.currency_id = body.currency_id;
        myResponse.quotation_id = "1";
        myResponse.total = calulateTotal(body);
        return new ResponseEntity<Object>(myResponse, HttpStatus.OK);
    }

    public Double getAgeLoad(int age) {
        if (age >= 18 && age <= 30)
            return 0.6;
        else if (age > 30 && age <= 40)
            return 0.7;
        else if (age > 40 && age <= 50)
            return 0.8;
        else if (age > 50 && age <= 60)
            return 0.9;
        else if (age > 60 && age <= 70)
            return 1.0;
        else
            return 0.0;
    }

    public Map<String, String>  validateRequestParameter(request myRequest) {
        String [] ages = myRequest.age.split(",");
        Map<String, String> errorResponse = new HashMap<>();
        int myAge = 0;
        for (String age: ages) {
            try {
                myAge = Integer.parseInt(age);
            }
            catch (Exception ex)
            {
                errorResponse.put("message", "Age list can only contains numbers");
                errorResponse.put("status", HttpStatus.BAD_REQUEST.toString());
                return errorResponse;
            }
            if (myAge == 0) {
                errorResponse.put("message", "0 is not a valid age");
                errorResponse.put("status", HttpStatus.BAD_REQUEST.toString());
                return errorResponse;
            }
        }
        myAge = Integer.parseInt(ages[0]);
        if (myAge < 18) {
            errorResponse.put("message", "Fisrt value in age list must be greater than 18.");
            errorResponse.put("status", HttpStatus.BAD_REQUEST.toString());
            return errorResponse;
        }

        Date startDate = null;
        Date endDate = null;
        try {
            startDate = new SimpleDateFormat("yyyy-MM-dd").parse(myRequest.start_date);
            // good format
        } catch (Exception e) {
            errorResponse.put("message", "start_date has to be in yyyy-mm-dd format");
            errorResponse.put("status", HttpStatus.BAD_REQUEST.toString());
            return errorResponse;
        }

        try {
            endDate = new SimpleDateFormat("yyyy-MM-dd").parse(myRequest.end_date);
            // good format
        } catch (Exception e) {
            errorResponse.put("message", "end_date has to be in yyyy-mm-dd format");
            errorResponse.put("status", HttpStatus.BAD_REQUEST.toString());
            return errorResponse;
        }
        return null;
    }

    public Double calulateTotal(request myRequest) throws ParseException {
        String [] ages = myRequest.age.split(",");
        int myAge = 0;
        Double fixedRate = 3.0;
        Double total = 0.0;
        Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(myRequest.start_date);
        Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(myRequest.end_date);
        int diffInDays = (int)( (endDate.getTime() - startDate.getTime())
                / (1000 * 60 * 60 * 24) ) + 1;
        for (String age: ages) {
            myAge = Integer.parseInt(age);
            Double myLoad = getAgeLoad(myAge);
            total += fixedRate * myLoad * diffInDays;
        }
        return Math.ceil(total);
    }

    @GetMapping("/")
    String home() {
        return "Hello, World!";
    }
}
