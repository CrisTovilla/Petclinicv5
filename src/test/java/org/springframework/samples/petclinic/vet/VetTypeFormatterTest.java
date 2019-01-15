/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.springframework.samples.petclinic.vet;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.samples.petclinic.owner.PetRepository;
import org.springframework.samples.petclinic.owner.PetType;
import org.springframework.samples.petclinic.owner.PetTypeFormatter;

/**
 *
 * @author WSGO
 */
@RunWith(MockitoJUnitRunner.class)
public class VetTypeFormatterTest {
     @Mock
    private VetRepository vets;

    private VetTypeFormatter vetTypeFormatter;

    @Before
    public void setup() {
        this.vetTypeFormatter = new VetTypeFormatter(vets);
    }

    @Test
    public void testPrint() {
        Specialty special = new Specialty();
        special.setName("surgery");
        String petTypeName = this.vetTypeFormatter.print(special, Locale.ENGLISH);
        assertEquals("surgery", petTypeName);
    }

    /**
     * Helper method to produce some sample pet types just for test purpose
     *
     * @return {@link Collection} of {@link PetType}
     */
    private List<Specialty> makePetTypes() {
        List<Specialty> vetTypes = new ArrayList<>();
        vetTypes.add(new Specialty() {
            {
                setName("none");
            }
        });
        vetTypes.add(new Specialty() {
            {
                setName("surgery");
            }
        });
        return vetTypes;
    }

}
