/*
 * Copyright 2016-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.vet;

import org.junit.Test;

import org.springframework.util.SerializationUtils;

import static org.assertj.core.api.Assertions.assertThat;
import org.assertj.core.util.Lists;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import org.junit.Before;
import org.junit.runner.RunWith;
import static org.mockito.BDDMockito.given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.samples.petclinic.medicamento.Medicamento;
import org.springframework.samples.petclinic.medicamento.MedicamentoController;
import org.springframework.samples.petclinic.medicamento.MedicamentoRepository;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * @author Dave Syer
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(VetController.class)
public class VetTest {

    private static final int TEST_VET_ID = 100;
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VetRepository Vets;

    private Vet mary;

    @Before
    public void setup() {
        mary = new Vet();
        mary.setId(TEST_VET_ID);
        mary.setFirstName("Mary");
        mary.setLastName("Pekerman");
        mary.setTelephone("987654321");
        mary.setSchedule("12:00 pm - 4:00 pm");
        mary.setSpecialty("surgery");
        
        given(this.Vets.findById(TEST_VET_ID)).willReturn(mary);
    }
    
    
    @Test
    public void testInitCreationForm() throws Exception {
        mockMvc.perform(get("/vets/new"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("vet"))
            .andExpect(view().name("vets/createOrUpdateVetForm"));
    }
    
    
    @Test
    public void testProcessCreationFormSuccess() throws Exception {
        mockMvc.perform(post("/vets/new")
            .param("firstName", "Mary")
            .param("lastName", "Pekerman")
            .param("telephone", "987654321")
            .param("schedule", "12:00 pm - 4:00 pm")
            .param("specialty", "surgery")
        )
            .andExpect(status().is3xxRedirection());
    }
    
        @Test
    public void testInitFindForm() throws Exception {
        mockMvc.perform(get("/vets/find"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("vet"))
            .andExpect(view().name("vets/findVets"));
    }
    
    
    
        @Test
    public void testProcessFindFormSuccess() throws Exception {
        given(this.Vets.findByLastName("")).willReturn(Lists.newArrayList(mary, new Vet()));
        mockMvc.perform(get("/vets"))
            .andExpect(status().isOk())
            .andExpect(view().name("vets/vetList"));
    }
    
    
    @Test
    public void testProcessFindFormByLastName() throws Exception {
        given(this.Vets.findByLastName(mary.getLastName())).willReturn(Lists.newArrayList(mary));
        mockMvc.perform(get("/vets")
            .param("lastName", "Pekerman")
        )
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/vets/" + TEST_VET_ID));
    }
    
    
        @Test
    public void testProcessFindFormNoVetFound() throws Exception {
        mockMvc.perform(get("/vets")
            .param("lastName","")
        )
            .andExpect(status().isOk())
            .andExpect(model().attributeHasFieldErrors("vet", "lastName"))
            .andExpect(model().attributeHasFieldErrorCode("vet", "lastName", "notFound"))
            .andExpect(view().name("vets/findVets"));
    }
    
    
    
        @Test
    public void testInitUpdateVetForm() throws Exception {
        mockMvc.perform(get("/vets/{vetsId}/edit", TEST_VET_ID))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("vet"))
            .andExpect(model().attribute("vet", hasProperty("firstName", is("Mary"))))
            .andExpect(model().attribute("vet", hasProperty("lastName", is("Pekerman"))))
            .andExpect(model().attribute("vet", hasProperty("telephone", is("987654321"))))
            .andExpect(model().attribute("vet", hasProperty("schedule", is("12:00 pm - 4:00 pm"))))
            .andExpect(model().attribute("vet", hasProperty("specialty", is("surgery"))))
            .andExpect(view().name("vets/createOrUpdateVetForm"));
    }
    
    
    
        @Test
    public void testProcessUpdateVetFormSuccess() throws Exception {
        mockMvc.perform(post("/vets/{vetId}/edit", TEST_VET_ID)
            .param("firstName", "Mary")
            .param("lastName", "Pekerman")
            .param("telephone", "987654321")
                .param("schedule", "2")
                .param("specialty", "surgery")
        )
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/vets/{vetId}"));
    }
    
    
      @Test
    public void testProcessUpdateVetFormHasErrors() throws Exception {
        mockMvc.perform(post("/vets/{vetId}/edit", TEST_VET_ID)
            .param("firstName", "paracetamol 2.0")
            .param("lastName", "paracetamol")
                .param("telephone", "paracetamol")
                .param("Schedule", "paracetamol")
        )
            .andExpect(status().isOk())
            .andExpect(model().attributeHasErrors("vet"))
            .andExpect(model().attributeHasFieldErrors("vet", "specialty"))
            .andExpect(view().name("vets/createOrUpdateVetForm"));
    }
    
    
        @Test
    public void testShowVet() throws Exception {
        mockMvc.perform(get("/vets/{vetId}", TEST_VET_ID))
            .andExpect(status().isOk())
            .andExpect(model().attribute("vet", hasProperty("firstName", is("Mary"))))
            .andExpect(model().attribute("vet", hasProperty("lastName", is("Pekerman"))))
            .andExpect(model().attribute("vet", hasProperty("telephone", is("987654321"))))
                .andExpect(model().attribute("vet", hasProperty("schedule", is("12:00 pm - 4:00 pm"))))
                .andExpect(model().attribute("vet", hasProperty("specialty", is("surgery"))))
            .andExpect(view().name("vets/vetDetails"));
    }

}
