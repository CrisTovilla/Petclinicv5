/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.springframework.samples.petclinic.medicamento;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
/**
 *
 * @author dani_
 */
@RunWith(SpringRunner.class)
@WebMvcTest(MedicamentoController.class)
public class MedicamentoTest {
    
    private static final int TEST_MEDICAMENTO_ID = 100;
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicamentoRepository medicamentos;

    private Medicamento paracetamol;

    @Before
    public void setup() {
        paracetamol = new Medicamento();
        paracetamol.setId(TEST_MEDICAMENTO_ID);
        paracetamol.setNombre("paracetamol");
        paracetamol.setIngrediente_activo("paracetamol :v");
        paracetamol.setPresentacion("cajita");
        given(this.medicamentos.findById(TEST_MEDICAMENTO_ID)).willReturn(paracetamol);
    }
    
    
    @Test
    public void testInitCreationForm() throws Exception {
        mockMvc.perform(get("/medicamento/new"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("medicamento"))
            .andExpect(view().name("medicamentos/createOrUpdateMedicamentoForm"));
    }
    
    
    @Test
    public void testProcessCreationFormSuccess() throws Exception {
        mockMvc.perform(post("/medicamento/new")
            .param("nombre", "paracetamol")
            .param("ingrediente_activo", "paracetamol :v")
            .param("presentacion", "cajita")
        )
            .andExpect(status().is3xxRedirection());
    }
    
        @Test
    public void testInitFindForm() throws Exception {
        mockMvc.perform(get("/medicamento/find"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("medicamento"))
            .andExpect(view().name("medicamentos/findMedicamentos"));
    }
    
    
    
        @Test
    public void testProcessFindFormSuccess() throws Exception {
        given(this.medicamentos.findByNombre("")).willReturn(Lists.newArrayList(paracetamol, new Medicamento()));
        mockMvc.perform(get("/medicamento"))
            .andExpect(status().isOk())
            .andExpect(view().name("medicamentos/medicamentosList"));
    }
    
    
    @Test
    public void testProcessFindFormByNombre() throws Exception {
        given(this.medicamentos.findByNombre(paracetamol.getNombre())).willReturn(Lists.newArrayList(paracetamol));
        mockMvc.perform(get("/medicamento")
            .param("nombre", "paracetamol")
        )
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/medicamento/" + TEST_MEDICAMENTO_ID));
    }
    
    
        @Test
    public void testProcessFindFormNoMedicamentosFound() throws Exception {
        mockMvc.perform(get("/medicamento")
            .param("nombre","")
        )
            .andExpect(status().isOk())
            .andExpect(model().attributeHasFieldErrors("medicamento", "nombre"))
            .andExpect(model().attributeHasFieldErrorCode("medicamento", "nombre", "notFound"))
            .andExpect(view().name("medicamentos/findMedicamentos"));
    }
    
    
    
        @Test
    public void testInitUpdateMedicamentoForm() throws Exception {
        mockMvc.perform(get("/medicamento/{medicamentoId}/edit", TEST_MEDICAMENTO_ID))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("medicamento"))
            .andExpect(model().attribute("medicamento", hasProperty("nombre", is("paracetamol"))))
            .andExpect(model().attribute("medicamento", hasProperty("ingrediente_activo", is("paracetamol :v"))))
            .andExpect(model().attribute("medicamento", hasProperty("presentacion", is("cajita"))))
            .andExpect(view().name("medicamentos/createOrUpdateMedicamentoForm"));
    }
    
    
    
        @Test
    public void testProcessUpdateMedicamentoFormSuccess() throws Exception {
        mockMvc.perform(post("/medicamento/{medicamentoId}/edit", TEST_MEDICAMENTO_ID)
            .param("nombre", "paracetamol")
            .param("ingrediente_activo", "paracetamol :v")
            .param("presentacion", "cajita")
        )
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/medicamento/{medicamentoId}"));
    }
    
    
      @Test
    public void testProcessUpdateMedicamentoFormHasErrors() throws Exception {
        mockMvc.perform(post("/medicamento/{medicamentoId}/edit", TEST_MEDICAMENTO_ID)
            .param("nombre", "paracetamol 2.0")
            .param("ingrediente_activo", "paracetamol")
        )
            .andExpect(status().isOk())
            .andExpect(model().attributeHasErrors("medicamento"))
            .andExpect(model().attributeHasFieldErrors("medicamento", "presentacion"))
            .andExpect(view().name("medicamentos/createOrUpdateMedicamentoForm"));
    }
    
    
        @Test
    public void testShowOwner() throws Exception {
        mockMvc.perform(get("/medicamento/{medicamentoId}", TEST_MEDICAMENTO_ID))
            .andExpect(status().isOk())
            .andExpect(model().attribute("medicamento", hasProperty("nombre", is("paracetamol"))))
            .andExpect(model().attribute("medicamento", hasProperty("ingrediente_activo", is("paracetamol :v"))))
            .andExpect(model().attribute("medicamento", hasProperty("presentacion", is("cajita"))))
            .andExpect(view().name("medicamentos/medicamentoDetails"));
    }
    
    
    
}
