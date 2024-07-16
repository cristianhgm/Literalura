package com.literalura.principal;

import com.literalura.dtos.Datos;
import com.literalura.dtos.DatosAutor;
import com.literalura.dtos.DatosLibros;
import com.literalura.models.Autor;
import com.literalura.models.Libro;
import com.literalura.repository.AutorRepository;
import com.literalura.repository.LibroRepository;
import com.literalura.service.ConsumoAPI;
import com.literalura.service.ConvierteDatos;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class Principal {

    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos convierteDatos = new ConvierteDatos();
    private final String URL_BASE = "https://gutendex.com/books/";
    private final String URL_BUSQUEDA = "?search=";
    private LibroRepository LRepository;
    private AutorRepository ARepository;

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.LRepository = libroRepository;
        this.ARepository = autorRepository;
    }

    public void mostrarMenu() throws UnsupportedEncodingException {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n" + "Elija la opción a través de su número:\n" +
                    "1 Buscar por título\n" +
                    "2 Listar libros registrados\n" +
                    "3 Listar autores registrados\n" +
                    "4 Listar autores vivos en un determinado año\n" +
                    "5 Listar libros por idioma\n" +
                    "0 Salir\n");

            try {
                opcion = teclado.nextInt();
                teclado.nextLine();
            } catch (InputMismatchException e) {
                ;
                teclado.nextLine();
                opcion = -1;
            }

            switch (opcion) {
                case 1:
                    buscarPorTitulo();
                    break;
                case 2:
                    mostrarLibrosRegistrados();
                    break;
                case 3:
                    listaDeAutores();
                    break;
                case 4:
                    busquedaPorFechaDeAutores();
                    break;
                case 5:
                    listaDeLibrosPorIdioma();
                    break;
                case 0:
                    System.out.println("Saliendo del programa");
                    break;
                default:
                    if (opcion != 0) {
                        System.out.println("No has ingresado una opción válida");
                    }
                    break;
            }
        }
        teclado.close();
    }

    private void buscarPorTitulo() throws UnsupportedEncodingException {
        System.out.println("Escribe el nombre del libro");
        var busqueda = teclado.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE + URL_BUSQUEDA + URLEncoder.encode(busqueda, "UTF-8"));
        var datosBusqueda = convierteDatos.obtenerDatos(json, Datos.class);

        Optional<DatosLibros> libroBuscado = datosBusqueda.resultados().stream()
                .filter(l -> l.titulo().toUpperCase().contains(busqueda.toUpperCase()))
                .findFirst();

        if (libroBuscado.isPresent()) {
            System.out.println("\nLibro Encontrado \n");

            DatosLibros datosLibros = libroBuscado.get();
            DatosAutor datosAutor = libroBuscado.get().autor().get(0);

            Optional<Libro> libroDuplicado = LRepository.findByTitulo(datosLibros.titulo());

            if (libroDuplicado.isPresent()) {
                System.out.println(libroDuplicado.get());
            } else {
                Optional<Autor> autorExistente = ARepository.findByNombre(datosAutor.nombre());
                Libro libro;

                if (autorExistente.isPresent()) {
                    libro = new Libro(datosLibros, autorExistente.get());
                } else {
                    Autor autor = new Autor(datosAutor);
                    ARepository.save(autor);
                    libro = new Libro(datosLibros, autor);
                }
                LRepository.save(libro);
                System.out.println(libro);
            }
        } else {
            System.out.println("Libro no encontrado \n");
        }
    }

    private void mostrarLibrosRegistrados() {
        List<Libro> libros = LRepository.findAll();

        List<Libro> titulosOrdenados = libros.stream()
                .sorted(Comparator.comparing(Libro::getTitulo))
                .collect(Collectors.toList());

        titulosOrdenados.forEach(l -> System.out.println("Título: " + l.getTitulo() +
                "\nAutor: " + l.getAutor().getNombre() +
                "\nIdioma: " + l.getIdioma() +
                "\nDescargas: " + l.getNumeroDeDescargas() + "\n"));
    }

    public List<Autor> obtenerAutores() {
        return ARepository.findAll();
    }

    public void listaDeAutores() {
        obtenerAutores().stream()
                .sorted(Comparator.comparing((Autor::getNombre)))
                .forEach(System.out::println);
    }

    private boolean autorVivoEn(Autor autor, int year) {
        Integer nacimiento = autor.getAñoDeNacimiento();
        Integer muerte = autor.getAñoDeMuerte();
        return nacimiento != null && (muerte == null || muerte >= year) && year >= nacimiento;
    }

    public List<Autor> obtenerAutoresVivosEn(int year) {
        return obtenerAutores().stream()
                .filter(autor -> autorVivoEn(autor, year))
                .collect(Collectors.toList());
    }


    public void busquedaPorFechaDeAutores() {
        System.out.println("Indique el año que quiere revisar autores vivos");

        try {
            Calendar calendar = Calendar.getInstance();
            int añoActual = calendar.get(Calendar.YEAR);
            var fechaDeBusqueda = teclado.nextInt();
            teclado.nextLine();

            if (fechaDeBusqueda > 0 && fechaDeBusqueda <= añoActual) {
                obtenerAutoresVivosEn(fechaDeBusqueda).stream()
                        .sorted(Comparator.comparing((Autor::getNombre)))
                        .forEach(System.out::println);
            } else {
                System.out.println("El año ingresado no es válido.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Valor Inválido");
        }
    }

    private void listaDeLibrosPorIdioma() {
        System.out.println("Elija el idioma:\n" +
                "ES- Español\n" +
                "EN- English\n" +
                "FR- Français\n" +
                "PT- Português\n"
        );
        var idioma = teclado.nextLine().toLowerCase();
        List<Libro> titulosYAutores = LRepository.findAll().stream()
                .filter(l -> l.getIdioma().equals(idioma))
                .sorted(Comparator.comparing(Libro::getTitulo))
                .collect(Collectors.toList());

        if (titulosYAutores.isEmpty()) {
            System.out.println("El idioma no es valido o no se encuentra ningun libro con este idioma");
        } else {
            titulosYAutores.forEach(System.out::println);
        }
    }
}

