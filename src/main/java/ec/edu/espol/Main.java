package ec.edu.espol;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ContactManager gestor = new ContactManager();
        Scanner sc = new Scanner(System.in);
        int opcion;
        do {
            System.out.println("\n\033[0;31m========= MENU DE GESTION DE CONTACTOS =========\033[0m");
            System.out.println("1. Crear nuevo contacto");
            System.out.println("2. Ver lista completa de contactos");
            System.out.println("3. Ver contacto actual");
            System.out.println("4. Ver contacto siguiente");
            System.out.println("5. Ver contacto anterior");
            System.out.println("6. Ver detalles de un contacto");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opcion: ");
            try {
                opcion = Integer.parseInt(sc.nextLine());

                switch (opcion) {
                    case 1 -> gestor.crearContacto(sc);
                    case 2 -> gestor.listarContactos();
                    case 3 -> gestor.verContactoActual();
                    case 4 -> gestor.verContactoSiguiente();
                    case 5 -> gestor.verContactoAnterior();
                    case 6 -> gestor.verDetallesContacto(sc);
                    case 0 -> System.out.println("Saliendo del programa...");
                    default -> System.out.println("Opcion invalida. Intente nuevamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida. Use numeros enteros.");
                opcion = -1;
            }
        } while (opcion != 0);
        sc.close();
        System.out.println("Programa finalizado. Gracias por usar el gestor!");
    }
}