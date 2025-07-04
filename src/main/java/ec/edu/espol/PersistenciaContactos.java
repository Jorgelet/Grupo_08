package ec.edu.espol;

import java.io.*;
import java.util.Scanner;

public class PersistenciaContactos {
    private static final String ARCHIVO_CONTACTOS = System.getProperty("user.dir") + File.separator + "contactos.txt";

    // Guardar todos los contactos en el archivo
    public static void guardarContactos(ListaCircularDoble<Contacto> contactos) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARCHIVO_CONTACTOS))) {
            if (contactos.estaVacia())
                return;

            var nodo = contactos.getCabeza();
            do {
                Contacto c = nodo.dato;
                // Formato: CONTACTO|tipo|nombre|telefono|direccion
                writer.println("CONTACTO|" + c.getTipo() + "|" + c.getNombre() + "|" +
                        c.getNumeroTelefono() + "|" + c.getDireccion());

                // Guardar atributos
                var atributos = c.getAtributos();
                if (!atributos.estaVacia()) {
                    var nodoAtributo = atributos.getCabeza();
                    do {
                        Atributo a = nodoAtributo.dato;
                        writer.println("ATRIBUTO|" + a.getNombre() + "|" + a.getValor());
                        nodoAtributo = nodoAtributo.siguiente;
                    } while (nodoAtributo != atributos.getCabeza());
                }

                // Guardar fotos
                var fotos = c.getFotos();
                if (!fotos.estaVacia()) {
                    var nodoFoto = fotos.getCabeza();
                    do {
                        Foto f = nodoFoto.dato;
                        writer.println("FOTO|" + f.getNombreArchivo());
                        nodoFoto = nodoFoto.siguiente;
                    } while (nodoFoto != fotos.getCabeza());
                }

                writer.println("---"); // Separador entre contactos
                nodo = nodo.siguiente;
            } while (nodo != contactos.getCabeza());

            System.out.println("\033[0;32mContactos guardados exitosamente.\033[0m");
        } catch (IOException e) {
            System.out.println("\033[0;31mError al guardar contactos: " + e.getMessage() + "\033[0m");
        }
    }

    // Cargar contactos desde el archivo
    public static void cargarContactos(ListaCircularDoble<Contacto> contactos) {
        File archivo = new File(ARCHIVO_CONTACTOS);
        if (!archivo.exists()) {
            System.out.println("\033[0;33mNo se encontró archivo de contactos previo.\033[0m");
            return;
        }

        try (Scanner scanner = new Scanner(archivo)) {
            Contacto contactoActual = null;
            int contactosCargados = 0;

            while (scanner.hasNextLine()) {
                String linea = scanner.nextLine().trim();

                if (linea.equals("---") || linea.isEmpty()) {
                    continue;
                }

                String[] partes = linea.split("\\|");

                switch (partes[0]) {
                    case "CONTACTO":
                        if (partes.length >= 5) {
                            contactoActual = new Contacto(partes[1], partes[2], partes[3], partes[4]);
                            contactos.agregar(contactoActual);
                            contactosCargados++;
                        }
                        break;

                    case "ATRIBUTO":
                        if (contactoActual != null && partes.length >= 3) {
                            contactoActual.agregarAtributo(new Atributo(partes[1], partes[2]));
                        }
                        break;

                    case "FOTO":
                        if (contactoActual != null && partes.length >= 2) {
                            contactoActual.agregarFoto(new Foto(partes[1]));
                        }
                        break;
                }
            }

            if (contactosCargados > 0) {
                System.out.println("\033[0;32m" + contactosCargados + " contactos cargados exitosamente.\033[0m");
            } else {
                System.out.println("\033[0;33mNo se encontraron contactos en el archivo.\033[0m");
            }

        } catch (IOException e) {
            System.out.println("\033[0;31mError al cargar contactos: " + e.getMessage() + "\033[0m");
        }
    }
}
