package com.aluracursos.literalura.main;

import com.aluracursos.literalura.models.Author;
import com.aluracursos.literalura.models.DatosLibro;
import com.aluracursos.literalura.models.Libro;
import com.aluracursos.literalura.repository.AuthorRepository;
import com.aluracursos.literalura.repository.LibroRepository;
import com.aluracursos.literalura.services.ConvierteDatos;
import com.aluracursos.literalura.services.RequestAPI;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private RequestAPI requestAPI = new RequestAPI();
    private Scanner scanner = new Scanner(System.in);
    private String urlBase ="https://gutendex.com/books/";
    private ConvierteDatos convierteDatos = new ConvierteDatos();
    private LibroRepository libroRepository;
    private AuthorRepository authorRepository;
    private List<Libro> libros;
    private List<Author> autores;

    public Main(LibroRepository libroRepository, AuthorRepository authorRepository) {
        this.libroRepository = libroRepository;
        this.authorRepository = authorRepository;
    }

    // Mostrar el menu en consola
    public void showMenu()
    {
        var opcion = -1;
        while (opcion != 0){
            var menu ="""
                    BIENVENIDO A LA LIBRERIA ALURA
                    
                    MENU DE OPCIONES DISPONIBLE: 
                    
                    1 - BUSCAR UN LIBRO
                    2 - LISTAR LIBROS CONSURTADOS
                    3 - LISTAR AUTORES DE LIBROS CONSURTADOS
                    4 - LISTAR AUTORES POR AÑO ESPECIFICO
                    5 - LISTAR LIBROS POR IDIOMAS
                     
                    6 - SALIR               
                    """;

            try {
                System.out.println(menu);
                opcion = scanner.nextInt();
                scanner.nextLine();
            }catch (Exception e){

                System.out.println("OPCION NO VALIDA");
            }

            switch (opcion){
                case 1:
                    buscarLibro();
                    break;
                case 2:
                    consultarLibros();
                    break;
                case 3:
                    consultarAutores();
                    break;
                case 4:
                    consultarAutoresPorAno();
                    break;
                case 5:
                    consultarLibrosLenguaje();
                    break;
                case 6:
                    System.out.println("Saliendo del sistema");
                    break;
                default:
                    System.out.println("OPCION NO VALIDA");
            }
        }
    }

    //buscarlibro
    private DatosLibro getDatosLibro() {
        System.out.println("NOMBRE DEL LIBRO");
        var busqueda = scanner.nextLine().toLowerCase().replace(" ","%20");
        var json = requestAPI.getData(urlBase +
                "?search=" +
                busqueda);

        DatosLibro datosLibro = convierteDatos.obtenerDatos(json, DatosLibro.class);
        return datosLibro;
    }

    // para guardar en la bd
    private void buscarLibro()
    {
        DatosLibro datosLibro = getDatosLibro();

        try {
            Libro libro = new Libro(datosLibro.resultados().get(0));
           Author author = new Author(datosLibro.resultados().get(0).autorList().get(0));
           // Author author = new Author(datosLibro.resultados().get(0);

            System.out.println("""
                    libro[
                        titulo: %s
                        author: %s
                        lenguaje: %s
                        descargas: %s
                    ]
                    """.formatted(libro.getTitulo(),
                    libro.getAutor(),
                    libro.getLenguaje(),
                    libro.getDescargas().toString()));

            libroRepository.save(libro);
            authorRepository.save(author);

        }catch (Exception e){
            System.out.println("ESTE LIBRO NO EXISTE");
        }

    }

    // Trae los libros guardados en la BD
    private void consultarLibros() {
        libros = libroRepository.findAll();
        libros.stream().forEach(l -> {
            System.out.println("""    
                        Titulo: %s
                        Author: %s
                        Lenguaje: %s
                        Descargas: %s
                    """.formatted(l.getTitulo(),
                    l.getAutor(),
                    l.getLenguaje(),
                    l.getDescargas().toString()));
        });
    }

    // buscarlos autores
    private void consultarAutores() {
        autores = authorRepository.findAll();
        autores.stream().forEach(a -> {
            System.out.println("""
                        Autor: %s
                        Año de nacimiento: %s
                        Año de defuncion: %s
                    """.formatted(a.getAutor(),
                    a.getNacimiento().toString(),
                    a.getDefuncion().toString()));
        });
    }


    public void consultarAutoresPorAno()
    {
        System.out.println("AÑO APARTIR DEL CUAL BUSCARA:");
        var anoBusqueda = scanner.nextInt();
        scanner.nextLine();

        List<Author> authors = authorRepository.autorPorFecha(anoBusqueda);
        authors.forEach( a -> {
            System.out.println("""
                    Nombre: %s
                    Fecha de nacimiento: %s
                    Fecha de defuncion: %s
                    """.formatted(a.getAutor(),a.getNacimiento().toString(),a.getDefuncion().toString()));
        });
    }


    private void consultarLibrosLenguaje()
    {
        System.out.println("""
                    
                    SELECCIONE EL IDIOMA
                
                1 - Ingles
                2 - Español
                """);

        try {

            var opcion2 = scanner.nextInt();
            scanner.nextLine();

            switch (opcion2) {
                case 1:
                    libros = libroRepository.findByLenguaje("en");
                    break;
                case 2:
                    libros = libroRepository.findByLenguaje("es");
                    break;

                default:
                    System.out.println("ESTA OPCION NO ES VALIDA");
            }

            libros.stream().forEach(l -> {
                System.out.println("""    
                        Titulo: %s
                        Author: %s
                        Lenguaje: %s
                        Descargas: %s
                    """.formatted(l.getTitulo(),
                        l.getAutor(),
                        l.getLenguaje(),
                        l.getDescargas().toString()));
            });

        } catch (Exception e){
            System.out.println("ESTA OPCION NO ES VALIDA");
        }
    }
}
