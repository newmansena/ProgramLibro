package com.aluracursos.literalura.models;

import jakarta.persistence.*;

@Entity
@Table (name = "autores")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String autor;
    private Integer nacimiento;
    private Integer defuncion;

    public Author(){}

    public Author(DatosAutor datosAutors) {
        this.autor = datosAutors.nombreAutor();
        this.nacimiento = datosAutors.nacimiento();
        this.defuncion = datosAutors.defuncion();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public Integer getNacimiento() {
        return nacimiento;
    }

    public void setNacimiento(Integer nacimiento) {
        this.nacimiento = nacimiento;
    }

    public Integer getDefuncion() {
        return defuncion;
    }

    public void setDefuncion(Integer defuncion) {
        this.defuncion = defuncion;
    }
}
