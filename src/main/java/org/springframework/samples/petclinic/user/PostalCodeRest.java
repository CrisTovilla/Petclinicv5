/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.springframework.samples.petclinic.user;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Iterator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author CristopherT
 */
public class PostalCodeRest {
    boolean consult(String postalCode,String city) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl = "http://api.geonames.org/postalCodeLookupJSON?postalcode=" + postalCode + "&country=MX&username=petclinicv5";
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(fooResourceUrl, String.class);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
<<<<<<< HEAD
            System.out.println(root.path("postalcodes"));
            System.out.println("city: " + city);
=======
>>>>>>> f25e0c21b0e218c6707ba10ee5f02d8d18064d56
            String city_json;
            
            for (Iterator<JsonNode> i = root.path("postalcodes").iterator(); i.hasNext();) {
                JsonNode item = i.next();            
                System.out.println(item.findValue("adminName3"));
                city_json=item.findValue("adminName3").textValue();
                 System.out.println("city: " + city_json);
                if(city_json.equalsIgnoreCase(city)){
                     System.out.println("True");
                     return true;
                }               
            }        
            return false;
        } 
        catch (Exception e) {
            return false;
        }
    }
}
