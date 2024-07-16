package com.literalura.models;

import com.literalura.dtos.DatosAutor;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "autores")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String nombre;
    private Integer añoDeNacimiento;
    private Integer añoDeMuerte;
    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Libro> libros;

    public Autor() {
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        libros.forEach(l -> l.setAutor(this));
        this.libros = libros;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getAñoDeNacimiento() {
        return añoDeNacimiento;
    }

    public void setAñoDeNacimiento(Integer añoDeNacimiento) {
        this.añoDeNacimiento = añoDeNacimiento;
    }

    public Integer getAñoDeMuerte() {
        return añoDeMuerte;
    }

    public void setAñoDeMuerte(Integer añoDeMuerte) {
        this.añoDeMuerte = añoDeMuerte;
    }

    public Autor (DatosAutor datosAutor) {
        this.nombre = datosAutor.nombre();
        this.añoDeMuerte = datosAutor.fechaDeMuerte();
        this.añoDeNacimiento = datosAutor.fechaDeNacimiento();
    }

    @Override
    public String toString() {
        return "Nombre = " + nombre + '\n' +
                "FechaDeNacimiento = " + añoDeNacimiento + '\n' +
                "FechaDeMuerte = " + añoDeMuerte + '\n' +
                "Libros = " + libros.stream().map(Libro::getTitulo).toList() +"\n";
    }
}
