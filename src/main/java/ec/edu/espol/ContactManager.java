package ec.edu.espol;

import java.util.Scanner;


public class ContactManager {
    private ListaCircularDoble<Contacto> contactos;

    public ContactManager() {
        contactos = new ListaCircularDoble<>();
    }
    // Método para crear un nuevo contacto
    public void crearContacto(Scanner sc) {
        System.out.println("Tipo de contacto (Persona/Empresa): ");
        String tipo = sc.nextLine().trim();

        System.out.println("Nombre: ");
        String nombre = sc.nextLine().trim();

        Contacto c = new Contacto(nombre, tipo);

        System.out.println("¿Cuántos atributos quieres añadir?");
        int n = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < n; i++) {
            System.out.print("Nombre del atributo: ");
            String nombreA = sc.nextLine();
            System.out.print("Valor del atributo: ");
            String valorA = sc.nextLine();
            c.agregarAtributo(new Atributo(nombreA, valorA));
        }

        System.out.println("¿Cuántas fotos quieres añadir?");
        int f = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < f; i++) {
            System.out.print("Nombre o ruta de la foto: ");
            String nombreF = sc.nextLine();
            c.agregarFoto(new Foto(nombreF));
        }

        contactos.agregar(c);
        System.out.println("Contacto creado con éxito.");
    }
    // Métodos para listar los contactos
    public void listarContactos() {
        if (contactos.estaVacia()) {
            System.out.println("No hay contactos.");
            return;
        }
        System.out.println("Lista de contactos:");
        contactos.imprimir();
    }
    // Método para ver los detalles de un contacto específico
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
    // Métodos para navegar por los contactos
    public void verContactoSiguiente() {
    if (contactos.estaVacia()) {
        System.out.println("No hay contactos para mostrar.");
        return;
    }
        var siguiente = contactos.next();
        System.out.println("Contacto siguiente:");
        siguiente.imprimirDetalles();
    }
    // Método para ver el contacto anterior
    public void verContactoAnterior() {
        if (contactos.estaVacia()) {
            System.out.println("No hay contactos para mostrar.");
            return;
        }
        var anterior = contactos.prev();
        System.out.println("⬅️ Contacto anterior:");
        anterior.imprimirDetalles();
    }
    // Método para ver el contacto actual
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

