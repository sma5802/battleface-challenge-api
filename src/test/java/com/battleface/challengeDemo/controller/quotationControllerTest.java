package com.battleface.challengeDemo.controller;

import com.battleface.challengeDemo.model.request;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class quotationControllerTest {
    @Test
    void validateRequestParameterTestNoError() {
        request myRequest = new request();
        myRequest.age = "19,30";
        myRequest.currency_id = "EUR";
        myRequest.start_date = "2018-10-01";
        myRequest.end_date = "2018-10-30";
        quotationController myController =  new quotationController();
        Map<String, String> result = myController.validateRequestParameter(myRequest);
        assertNull(result);
    }

    @Test
    void validateRequestParameterTestGreaterThan18() {
        request myRequest = new request();
        myRequest.age = "17,30";
        myRequest.currency_id = "EUR";
        myRequest.start_date = "2018-10-01";
        myRequest.end_date = "2018-10-30";
        quotationController myController =  new quotationController();
        Map<String, String> result = myController.validateRequestParameter(myRequest);
        assertNotNull(result);
        assertTrue(result.get("message").equals("Fisrt value in age list must be greater than 18."));
    }
    @Test
    void validateRequestParameterTest0NotValid() {
        request myRequest = new request();
        myRequest.age = "19,0";
        myRequest.currency_id = "EUR";
        myRequest.start_date = "2018-10-01";
        myRequest.end_date = "2018-10-30";
        quotationController myController =  new quotationController();
        Map<String, String> result = myController.validateRequestParameter(myRequest);
        assertNotNull(result);
        assertTrue(result.get("message").equals("0 is not a valid age"));
    }
}
