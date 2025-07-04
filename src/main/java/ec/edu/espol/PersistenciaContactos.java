package ec.edu.espol;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class PersistenciaContactos {
    private static final String ARCHIVO_CONTACTOS = System.getProperty("user.dir") + File.separator + "contactos.txt";

    public static void guardarContactos(ListaCircularDoble<Contacto> contactos) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARCHIVO_CONTACTOS))) {

            for (Contacto c : contactos) {
                writer.println("CONTACTO|" + c.getTipo() + "|" + c.getNombre() + "|" + c.getApellido() + "|" +
                        c.getNumeroTelefono() + "|" + c.getDireccion());

                for (Atributo a : c.getAtributos()) {
                    writer.println("ATRIBUTO|" + a.getNombre() + "|" + a.getValor());
                }

                for (Foto f : c.getFotos()) {
                    writer.println("FOTO|" + f.getNombreArchivo());
                }

                for (Contacto asociado : c.getAsociados()) {
                    writer.println("ASOCIADO|" + asociado.getNumeroTelefono());
                }

                writer.println("---");
            }

            System.out.println("\033[0;32mContactos guardados exitosamente.\033[0m");

        } catch (IOException e) {
            System.out.println("\033[0;31mError al guardar contactos: " + e.getMessage() + "\033[0m");
        }
    }

    public static void cargarContactos(ListaCircularDoble<Contacto> contactos) {
        File archivo = new File(ARCHIVO_CONTACTOS);
        if (!archivo.exists()) {
            System.out.println("\033[0;33mNo se encontró archivo de contactos, se creará uno nuevo al guardar.\033[0m");
            return;
        }

        Map<String, ListaCircularDoble<String>> mapaAsociaciones = new HashMap<>();

        try (Scanner scanner = new Scanner(archivo)) {
            Contacto contactoActual = null;
            int contactosCargados = 0;

            while (scanner.hasNextLine()) {
                String linea = scanner.nextLine();
                if (linea.isEmpty() || linea.equals("---"))
                    continue;

                String[] partes = linea.split("\\|");
                String tipoDato = partes[0];

                switch (tipoDato) {
                    case "CONTACTO":
                        String tipo = partes[1];
                        String nombre = partes[2];
                        String apellido = partes[3];
                        String telefono = partes[4];
                        String direccion = partes[5];
                        contactoActual = new Contacto(tipo, nombre, apellido, telefono, direccion);
                        contactos.agregar(contactoActual);
                        mapaAsociaciones.put(telefono, new ListaCircularDoble<>());
                        contactosCargados++;
                        break;

                    case "ATRIBUTO":
                        if (contactoActual != null) {
                            contactoActual.agregarAtributo(new Atributo(partes[1], partes[2]));
                        }
                        break;

                    case "FOTO":
                        if (contactoActual != null) {
                            contactoActual.agregarFoto(new Foto(partes[1]));
                        }
                        break;

                    case "ASOCIADO":
                        if (contactoActual != null) {
                            String telefonoAsociado = partes[1];
                            mapaAsociaciones.get(contactoActual.getNumeroTelefono()).agregar(telefonoAsociado);
                        }
                        break;
                }
            }

            for (Map.Entry<String, ListaCircularDoble<String>> entry : mapaAsociaciones.entrySet()) {
                Contacto contactoOrigen = buscarPorTelefono(contactos, entry.getKey());
                if (contactoOrigen != null) {
                    for (String telefonoDestino : entry.getValue()) {
                        Contacto contactoDestino = buscarPorTelefono(contactos, telefonoDestino);
                        if (contactoDestino != null) {
                            contactoOrigen.agregarAsociado(contactoDestino);
                        }
                    }
                }
            }

            if (contactosCargados > 0) {
                System.out.println("\033[0;32m" + contactosCargados + " contactos cargados exitosamente.\033[0m");
            }

        } catch (IOException e) {
            System.out.println("\033[0;31mError al cargar contactos: " + e.getMessage() + "\033[0m");
        }
    }

    private static Contacto buscarPorTelefono(ListaCircularDoble<Contacto> contactos, String telefono) {
        for (Contacto c : contactos) {
            if (c.getNumeroTelefono().equals(telefono)) {
                return c;
            }
        }
        return null;
    }
}
