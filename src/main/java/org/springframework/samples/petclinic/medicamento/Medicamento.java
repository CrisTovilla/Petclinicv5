/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.springframework.samples.petclinic.medicamento;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.core.style.ToStringCreator;
import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.model.Person;

/**
 *
 * @author COFIES
 */
@Entity
@Table(name = "medicamento")
public class Medicamento extends BaseEntity{
    @Column(name = "nombre")
    @NotEmpty
    public String nombre;

    @Column(name = "ingrediente_activo")
    @NotEmpty
    public String ingrediente_activo;

    @Column(name = "presentacion")
    @NotEmpty
    public String presentacion;
    
    @Column(name = "descripcion")
    @NotEmpty
    public String descripcion;
    
    @Column(name = "precio")
    @NotEmpty
    public String precio;
    
    @Column(name = "existencia")
    @NotEmpty
    public String existencia;   
    
    @Column(name = "fotografia")
    public String fotografia;
    

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIngrediente_activo() {
        return ingrediente_activo;
    }

    public String getFotografia() {
        return fotografia;
    }
    
    public String getPrecio() {
        return precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getExistencia() {
        return existencia;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setExistencia(String existencia) {
        this.existencia = existencia;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }
    
    public void setIngrediente_activo(String ingrediente_activo) {
        this.ingrediente_activo = ingrediente_activo;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public void setFotografia(String fotografia) {
        this.fotografia = fotografia;
    }
    
    
    

}
