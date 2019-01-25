/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.springframework.samples.petclinic.user;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author CristopherT
 */
public class PostalCodeService {

    boolean existPostalCode(String postalCode) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl = "http://api.geonames.org/postalCodeLookupJSON?postalcode=" + postalCode + "&country=MX&username=petclinicv5";
        try {
            ResponseEntity<String> response
                    = restTemplate.getForEntity(fooResourceUrl, String.class);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            if (response.getStatusCode() == HttpStatus.OK && root.path("postalcodes").get(0) != null) {
                return true;
            }
            System.out.println("Es falso Noe xiste");
            return false;
        } catch (Exception e) {
            return false;
        }

    }

}
