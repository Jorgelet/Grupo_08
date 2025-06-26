package ec.edu.espol;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ContactManager gestor = new ContactManager();
        Scanner sc = new Scanner(System.in);
        int opcion;
        do {
            System.out.println("MENU DE CONTACTOS");
            System.out.println("1. Crear nuevo contacto");
            System.out.println("2. Ver lista de contactos");
            System.out.println("3. Ver detalles de un contacto");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = Integer.parseInt(sc.nextLine());

            switch (opcion) {
                case 1 -> gestor.crearContacto(sc);
                case 2 -> gestor.listarContactos();
                case 3 -> gestor.verDetallesContacto(sc);
                case 0 -> System.out.println("¡Hasta pronto!");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
        sc.close();
    }
}