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
            System.out.println("7. Guardar contactos");
            System.out.println("8. Cargar contactos");
            System.out.println("9. Eliminar un contacto");
            System.out.println("10. Editar atributo de un contacto");
            System.out.println("11. Remover atributo de un contacto");
            System.out.println("12. Editar un contacto");
            System.out.println("13. Filtrar por nombre y apellido");
            System.out.println("14. Filtrar por cantidad de atributos");
            System.out.println("15. Filtrar por tipo de contacto");
            System.out.println("\n\033[0;31m0. Salir\033[0m");
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
                    case 7 -> gestor.guardarContactos();
                    case 8 -> gestor.cargarContactos();
                    case 9 -> gestor.eliminarContacto(sc);
                    case 10 -> gestor.editarAtributoContacto(sc);
                    case 11 -> gestor.removerAtributoContacto(sc);
                    case 12 -> gestor.editarContacto(sc);
                    case 13 -> gestor.filtrarPorNombreApellido(sc);
                    case 14 -> gestor.filtrarPorCantidadAtributos(sc);
                    case 15 -> gestor.filtrarPorTipoContacto(sc);
                    case 0 -> {
                        // Guardar automáticamente al salir
                        gestor.guardarContactos();
                        System.out.println("Saliendo del programa...");
                    }
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