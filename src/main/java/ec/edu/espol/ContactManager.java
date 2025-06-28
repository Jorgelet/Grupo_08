package ec.edu.espol;

import java.util.Scanner;

public class ContactManager {
    private ListaCircularDoble<Contacto> contactos;

    public ContactManager() {
        contactos = new ListaCircularDoble<>();
    }

    // Método para crear un nuevo contacto
    public void crearContacto(Scanner sc) {
        System.out.println();
        System.out.print("Tipo de contacto (Persona/Empresa): ");
        String tipo = sc.nextLine().trim();
        while (!tipo.equalsIgnoreCase("Persona") && !tipo.equalsIgnoreCase("Empresa")) {
            System.out.print("Tipo invalido. Debe ser 'Persona' o 'Empresa'. Intente de nuevo: ");
            tipo = sc.nextLine().trim();
        }

        System.out.print("Nombre: ");
        String nombre = sc.nextLine().trim();
        System.out.print("Numero de telefono: ");
        String numeroTelefono = sc.nextLine().trim();

        while (numeroTelefono.isEmpty() || !numeroTelefono.matches("\\d+")) {
            System.out.print("Numero de telefono invalido. Debe contener solo digitos. Intente de nuevo: ");
            numeroTelefono = sc.nextLine().trim();
        }

        System.out.print("Direccion: ");
        String direccion = sc.nextLine().trim();

        Contacto c = new Contacto(tipo, nombre, numeroTelefono, direccion);

        System.out.print("Cuantos atributos mas desea añadir? ");
        int n = Integer.parseInt(sc.nextLine());

        if (n <= 0) {
            System.out.println("Perfecto no se agregaron más atributos al contacto.");
        }

        for (int i = 0; i < n; i++) {
            System.out.print("Nombre del atributo: ");
            String nombreA = sc.nextLine();
            System.out.print("Valor del atributo: ");
            String valorA = sc.nextLine();
            c.agregarAtributo(new Atributo(nombreA, valorA));
        }

        System.out.println("Cuantas fotos quieres anadir?");
        int f = Integer.parseInt(sc.nextLine());

        while (f < 2) {
            System.out.print("Debe agregar al menos 2 fotos. Ingrese una cantidad valida: ");
            f = Integer.parseInt(sc.nextLine());
        }

        for (int i = 0; i < f; i++) {
            System.out.print("Nombre o ruta de la foto: ");
            String nombreF = sc.nextLine();
            c.agregarFoto(new Foto(nombreF));
        }

        contactos.agregar(c);
        System.out.println("Contacto creado con exito.");
    }

    // Metodos para listar los contactos
    public void listarContactos() {
        if (contactos.estaVacia()) {
            System.out.println("No hay contactos.");
            return;
        }
        System.out.println("Lista de contactos:");
        contactos.imprimir();
    }

    // Metodo para ver los detalles de un contacto especifico
    public void verDetallesContacto(Scanner sc) {
        if (contactos.estaVacia()) {
            System.out.println("No hay contactos.");
            return;
        }
        System.out.print("Nombre del contacto a buscar: ");
        String nombre = sc.nextLine();
        var nodo = contactos.getCabeza();
        boolean encontrado = false;
        do {
            if (nodo.dato.getNombre().equalsIgnoreCase(nombre)) {
                nodo.dato.imprimirDetalles();
                encontrado = true;
                break;
            }
            nodo = nodo.siguiente;
        } while (nodo != contactos.getCabeza());

        if (!encontrado) {
            System.out.println("Contacto no encontrado.");
        }
    }

    // Metodos para navegar por los contactos
    public void verContactoSiguiente() {
        if (contactos.estaVacia()) {
            System.out.println("No hay contactos para mostrar.");
            return;
        }
        var siguiente = contactos.next();
        System.out.println("Contacto siguiente:");
        siguiente.imprimirDetalles();
    }

    // Metodo para ver el contacto anterior
    public void verContactoAnterior() {
        if (contactos.estaVacia()) {
            System.out.println("No hay contactos para mostrar.");
            return;
        }
        var anterior = contactos.prev();
        System.out.println("Contacto anterior:");
        anterior.imprimirDetalles();
    }

    // Metodo para ver el contacto actual
    public void verContactoActual() {
        if (contactos.estaVacia()) {
            System.out.println("No hay contactos para mostrar.");
            return;
        }
        var actual = contactos.getActual();
        System.out.println("Contacto actual:");
        actual.imprimirDetalles();
    }
}
