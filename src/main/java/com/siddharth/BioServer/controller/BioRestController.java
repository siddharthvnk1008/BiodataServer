package com.siddharth.BioServer.controller;


import com.siddharth.BioServer.model.PayuRequestModel;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/hash")
public class BioRestController {

    @Autowired
    RestTemplate restTemplate;

    /**
     * Api is to send one to one message to respective channels.
     *
     * @param requestModel RCSMessageWithContactInfo DTO
     * @return RCSMessageWithContactInfoResponse DTO
     */
    @PostMapping(path = "/generateHash", consumes = "application/json")
    public ResponseEntity<String> generateHash(@RequestBody PayuRequestModel requestModel) {

        return new ResponseEntity("Hello Sir", HttpStatus.OK);
    }

    private final String key = "JUnC7f";      //put your merchant key value
    private final String salt = "t9tV889H";   //put your merchant salt value

    @GetMapping(path = "/createHash/{hashKey}")
    public String createHash(@PathVariable String hashKey) {
       return calculateHash(hashKey+salt);
    }


    /**
     * Hash Should be generated from your sever side only.
     * <p>
     * Do not use this, you may use this only for testing.
     * This should be done from server side..
     * Do not keep salt anywhere in app.
     */
    private String calculateHash(String hashString) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            messageDigest.update(hashString.getBytes());
            byte[] mdbytes = messageDigest.digest();
            return getHexString(mdbytes);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private String getHexString(byte[] array) {
        StringBuilder hash = new StringBuilder();
        for (byte hashByte : array) {
            hash.append(Integer.toString((hashByte & 0xff) + 0x100, 16).substring(1));
        }
        return hash.toString();
    }
}
