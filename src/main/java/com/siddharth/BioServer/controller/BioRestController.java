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

        ResponseEntity responseEntity = restTemplate.postForEntity("https://pk_live_nm9hWU5CreNo:sk_live_7e468kdXd0sjKK9t9Ts5fj3hYG3HD7GVbevmu5mH@api.ippopay.com/v1/order/create",
                requestModel, ResponseEntity.class);
        return new ResponseEntity(responseEntity.getBody(), HttpStatus.OK);
    }

    private final String key = "JUnC7f";      //put your merchant key value
    private final String salt = "t9tV889H";   //put your merchant salt value

    private final String PAYMENT_HASH = "payment_hash";
    private final String GET_MERCHANT_IBIBO_CODES_HASH = "get_merchant_ibibo_codes_hash";
    private final String VAS_FOR_MOBILE_SDK_HASH = "vas_for_mobile_sdk_hash";
    private final String PAYMENT_RELATED_DETAILS_FOR_MOBILE_SDK_HASH = "payment_related_details_for_mobile_sdk_hash";
    private final String DELETE_USER_CARD_HASH = "delete_user_card_hash";
    private final String GET_USER_CARDS_HASH = "get_user_cards_hash";
    private final String EDIT_USER_CARD_HASH = "edit_user_card_hash";
    private final String SAVE_USER_CARD_HASH = "save_user_card_hash";
    private final String CHECK_OFFER_STATUS_HASH = "check_offer_status_hash";
    private final String CHECK_ISDOMESTIC_HASH = "check_isDomestic_hash";
    private final String VERIFY_PAYMENT_HASH = "verify_payment_hash";

    /**
     * This function generates a JSON String of required mandatory hashes.
     */
    public String getHashes(String txnid, String amount, String productInfo, String firstname, String email,
                            String user_credentials, String udf1, String udf2, String udf3, String udf4, String udf5
    ) {
        JSONObject response = new JSONObject();

        String ph = checkNull(key) + "|" + checkNull(txnid) + "|" + checkNull(amount) + "|" + checkNull(productInfo)
                + "|" + checkNull(firstname) + "|" + checkNull(email) + "|" + checkNull(udf1) + "|" + checkNull(udf2)
                + "|" + checkNull(udf3) + "|" + checkNull(udf4) + "|" + checkNull(udf5) + "||||||" + salt;
        String paymentHash = getSHA(ph);
        System.out.println("Payment Hash " + paymentHash);
        response.put(PAYMENT_HASH, paymentHash);
        response.put(VAS_FOR_MOBILE_SDK_HASH, generateHashString("vas_for_mobile_sdk", "default"));

        //Use var1 as user_credentials if user_credential is not empty
        if (!checkNull(user_credentials).isEmpty()) {

            response.put(PAYMENT_RELATED_DETAILS_FOR_MOBILE_SDK_HASH,
                    generateHashString("payment_related_details_for_mobile_sdk", user_credentials));

            response.put(DELETE_USER_CARD_HASH, generateHashString("delete_user_card", user_credentials));
            response.put(GET_USER_CARDS_HASH, generateHashString("get_user_cards", user_credentials));
            response.put(EDIT_USER_CARD_HASH, generateHashString("edit_user_card", user_credentials));
            response.put(SAVE_USER_CARD_HASH, generateHashString("save_user_card", user_credentials));
        } else {
            response.put(PAYMENT_RELATED_DETAILS_FOR_MOBILE_SDK_HASH,
                    generateHashString("payment_related_details_for_mobile_sdk", "default"));

        }
        System.out.println("Vas_for _mobile_sdk  " + generateHashString("vas_for_mobile_sdk", "default"));
        System.out.println("payment_related_details_sdk_hash  " + generateHashString("payment_related_details_for_mobile_sdk", user_credentials));

        System.out.println("delete_user_card Hash" + generateHashString("delete_user_card", user_credentials));


        return response.toJSONString();

    }

// This method generates hash string

    private String generateHashString(String command, String var1) {
        return getSHA(key + "|" + command + "|" + var1 + "|" + salt);
    }

    private String checkNull(String value) {
        if (value == null) {
            return "";
        } else {
            return value;
        }
    }

    private String getSHA(String str) {

        MessageDigest md;
        String out = "";
        try {
            md = MessageDigest.getInstance("SHA-512");
            md.update(str.getBytes());
            byte[] mb = md.digest();

            for (int i = 0; i < mb.length; i++) {
                byte temp = mb[i];
                String s = Integer.toHexString(new Byte(temp));
                while (s.length() < 2) {
                    s = "0" + s;
                }
                s = s.substring(s.length() - 2);
                out += s;
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return out;

    }
}
