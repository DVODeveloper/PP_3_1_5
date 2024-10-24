package org.example;

import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Consumer {
    public static void main(String[] args) {

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        StringBuilder allResponses = new StringBuilder();

        String URL = "http://94.198.50.185:7081/api/users";

        Map<String, Object> jsonToSendPOST = new HashMap<>();
        jsonToSendPOST.put("id", 3L);
        jsonToSendPOST.put("name", "James");
        jsonToSendPOST.put("lastName", "Brown");
        jsonToSendPOST.put("age", 35);

        HttpEntity<Map<String, Object>> requestPOST = new HttpEntity<>(jsonToSendPOST);
        ResponseEntity<String> responsePost = restTemplate.exchange(URL, HttpMethod.POST, requestPOST, String.class);

        List<String> cookies = responsePost.getHeaders().get(HttpHeaders.SET_COOKIE);
        String sessionId = null;
        if (cookies != null && !cookies.isEmpty()) {
            sessionId = cookies.get(0);
        }

        allResponses.append(responsePost.getBody());


        if (sessionId != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.COOKIE, sessionId);

            Map<String, Object> jsonToSendPUT = new HashMap<>();
            jsonToSendPUT.put("id", 3L);
            jsonToSendPUT.put("name", "Thomas");
            jsonToSendPUT.put("lastName", "Shelby");
            jsonToSendPUT.put("age", 35);

            HttpEntity<Map<String, Object>> requestPUT = new HttpEntity<>(jsonToSendPUT, headers);

            ResponseEntity<String> responsePut = restTemplate.exchange(URL, HttpMethod.PUT, requestPUT, String.class);
            allResponses.append(responsePut.getBody());
        }

        if (sessionId != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.COOKIE, sessionId);

            HttpEntity<String> requestEntity = new HttpEntity<>(headers);
            ResponseEntity<String> responseDelete = restTemplate.exchange(URL + "/3", HttpMethod.DELETE, requestEntity, String.class);

            allResponses.append(responseDelete.getBody());
        }

        System.out.println(allResponses);
    }
}
