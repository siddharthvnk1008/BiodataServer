package com.siddharth.BioServer.service;

import com.siddharth.BioServer.model.OrderModel;
import com.siddharth.BioServer.model.OrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Service
public class BiodataService {

    @Autowired
    RestTemplate restTemplate;

    /*
     * Create a new payment order
     */
    public String createOrder(OrderModel orderModel) {

        final String baseUrl = "https://api.cashfree.com/api/v2/cftoken/order";
        URI uri = null;
        try {
            uri = new URI(baseUrl);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-client-id", "117660947b80659dba410bd1b5066711");
        headers.set("x-client-secret", "b15d5acf7e73b50f501e6d5b2396c448c0bf800c");

        HttpEntity<OrderModel> request = new HttpEntity<>(orderModel, headers);

        ResponseEntity<OrderResponse> result = restTemplate.postForEntity(uri, request, OrderResponse.class);

        return result.getBody().getCftoken();
    }
}
