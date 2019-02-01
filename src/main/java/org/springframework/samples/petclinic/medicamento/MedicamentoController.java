/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.springframework.samples.petclinic.medicamento;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author COFIES
 */
@Controller
public class MedicamentoController {
    private static final String VIEW_MEDICAMENTOS = "medicamentos/createOrUpdateMedicamentoForm";
    public static String uploadDirectory = System.getProperty("user.dir")+"/src/main/resources/static/resources/images/Medicinas";
    private final MedicamentoRepository medicamento;
    
        
    public MedicamentoController(MedicamentoRepository medicinas) {
        this.medicamento = medicinas;
    }
    
    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @GetMapping("/medicamento/new")
    public String initCreationForm(Map<String, Object> model) {
               
        
        
        Medicamento medicamento = new Medicamento();
        model.put("medicamento", medicamento);
        return VIEW_MEDICAMENTOS;
    }

    @PostMapping("/medicamento/new")
    public String processCreationForm(@Valid Medicamento medicamento, BindingResult result, Model model2, @RequestParam("files") MultipartFile[] files) {
        
        StringBuilder fileNames = new StringBuilder();
        String path = null; 
        for(MultipartFile file: files){
            Path fileNameAndPath = Paths.get(uploadDirectory,file.getOriginalFilename());
            path=file.getOriginalFilename();
            medicamento.setFotografia(path);
            fileNames.append(file.getOriginalFilename());
            try{
               Files.write(fileNameAndPath, file.getBytes());
            }catch(IOException e){
                System.out.println(e);
            }     
            
        }
        
        if (result.hasErrors()) {
            return VIEW_MEDICAMENTOS;
        } else {            
            this.medicamento.save(medicamento);
            return "redirect:/medicamento/find";
        }
    }

    @GetMapping("/medicamento/find")
    public String initFindForm(Map<String, Object> model) {
        model.put("medicamento", new Medicamento());
        return "medicamentos/findMedicamentos";
    }

    @GetMapping("/medicamento")
    public String processFindForm(Medicamento medicamento, BindingResult result, Map<String, Object> model) {

        // allow parameterless GET request for /owners to return all records
        if (medicamento.getNombre()== null) {
            medicamento.setNombre(""); // empty string signifies broadest possible search
        }

        // find owners by last name
        Collection<Medicamento> results = this.medicamento.findByNombre(medicamento.getNombre());
        if (results.isEmpty()) {
            // no owners found
            result.rejectValue("nombre", "notFound", "not found");
            return "medicamentos/findMedicamentos";
        } else if (results.size() == 1) {
            // 1 owner found
            medicamento = results.iterator().next();
            return "redirect:/medicamento/" + medicamento.getId();
        } else {
            // multiple owners found
            model.put("selections", results);
            return "medicamentos/medicamentosList";
        }
    }
    
        @GetMapping("/medicamento2")
    public String processFindForm2(Medicamento medicamento, BindingResult result, Map<String, Object> model) {

        // allow parameterless GET request for /owners to return all records
        if (medicamento.getNombre()== null) {
            medicamento.setNombre(""); // empty string signifies broadest possible search
        }

        // find owners by last name
        Collection<Medicamento> results = this.medicamento.findByNombre(medicamento.getNombre());
        if (results.isEmpty()) {
            // no owners found
            result.rejectValue("nombre", "notFound", "not found");
            return "medicamentos/findMedicamentos";
        } else if (results.size() == 1) {
            // 1 owner found
            medicamento = results.iterator().next();
            return "redirect:/medicamento/" + medicamento.getId();
        } else {
            // multiple owners found
            model.put("selections", results);
            return "medicamentos/medicamentosList2";
        }
    }
    

    @GetMapping("/medicamento/{medicamentoId}/edit")
    public String initUpdateMedicamentoForm(@PathVariable("medicamentoId") int medicamentoId, Model model) {
        Medicamento medicamento = this.medicamento.findById(medicamentoId);
        model.addAttribute(medicamento);
        return VIEW_MEDICAMENTOS;
    }

    @PostMapping("/medicamento/{medicamentoId}/edit")
    public String processUpdateMedicamentoForm(@Valid Medicamento medicamento, BindingResult result, @PathVariable("medicamentoId") int medicamentoId, Model model2, @RequestParam("files") MultipartFile[] files) {
        
         StringBuilder fileNames = new StringBuilder();
        String path = null; 
        for(MultipartFile file: files){
            Path fileNameAndPath = Paths.get(uploadDirectory,file.getOriginalFilename());
            path=file.getOriginalFilename();
            medicamento.setFotografia(path);
            fileNames.append(file.getOriginalFilename());
            try{
               Files.write(fileNameAndPath, file.getBytes());
            }catch(IOException e){
                System.out.println(e);
            }     
            
        }
        
        if (result.hasErrors()) {
            return VIEW_MEDICAMENTOS;
        } else {
            medicamento.setId(medicamentoId);
            this.medicamento.save(medicamento);
            return "redirect:/medicamento/{medicamentoId}";
        }
    }


    @GetMapping("/medicamento/{medicamentoId}")
    public ModelAndView showMedicamento(@PathVariable("medicamentoId") int medicamentoId) {
        ModelAndView mav = new ModelAndView("medicamentos/medicamentoDetails");
        mav.addObject(this.medicamento.findById(medicamentoId));
        return mav;
    }
    
    @GetMapping("/medicamento/{medicamentoId}/delete")
    public String deleteMedicamento(Medicamento medicamento, BindingResult result,@PathVariable("medicamentoId") int medicamentoId){
        medicamento = this.medicamento.findById(medicamentoId);
        this.medicamento.delete(medicamento);
                
        return "redirect:/medicamento?nombre=";
    }
    
    @GetMapping("/medicamentosReport")
    public String showMedicamentos(Medicamento medicamento, BindingResult result, Map<String, Object> model) {
        Collection<Medicamento> results = this.medicamento.findAll();
        model.put("selections", results);
        return "medicamentos/medicamentosList";
    }
        
}
