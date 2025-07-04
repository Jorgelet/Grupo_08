package ec.edu.espol;

import java.util.Scanner;

public class ContactManager {
    private ListaCircularDoble<Contacto> contactos;

    public ContactManager() {
        contactos = new ListaCircularDoble<>();
        // Cargar contactos existentes al iniciar
        PersistenciaContactos.cargarContactos(contactos);
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

        String numeroTelefono;
        boolean numeroValido;
        do {
            System.out.print("Numero de telefono: ");
            numeroTelefono = sc.nextLine().trim();
            numeroValido = true; // Suponemos que es válido siempre al inicio

            if (numeroTelefono.isEmpty() || !numeroTelefono.matches("\\d+")) {
                System.out.print("Numero de telefono invalido. Debe contener solo digitos. Intente de nuevo: ");
                numeroValido = false; // No es válido, el bucle se repetirá
            }
            // 2. Si el formato es correcto, validar que el número no exista ya
            else if (existeNumeroTelefono(numeroTelefono)) {
                System.out.println("Error: Ya existe un contacto con ese número de teléfono. Intente con otro.");
                numeroValido = false; // No es válido, el bucle se repetirá
            }
        } while (!numeroValido);

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

        // Guardar automáticamente después de crear un contacto
        PersistenciaContactos.guardarContactos(contactos);
    }

    private boolean existeNumeroTelefono(String numeroTelefono) {
        for (Contacto c : contactos) {
            if (c.getNumeroTelefono().equalsIgnoreCase(numeroTelefono)) {
                return true; // El número ya existe
            }
        }
        return false; // El número no existe
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

    // Método para eliminar un contacto
    public void eliminarContacto(Scanner sc) {
        if (contactos.estaVacia()) {
            System.out.println("No hay contactos para eliminar.");
            return;
        }
        System.out.print("Numero de teléfono del contacto a eliminar: ");
        String numeroTelefono = sc.nextLine();

        // Buscamos el contacto en la lista
        Contacto contactoAEliminar = null;
        for (Contacto c : contactos) {
            if (c.getNumeroTelefono().equalsIgnoreCase(numeroTelefono)) {
                contactoAEliminar = c;
                break;
            }
        }

        // Si lo encontramos, lo eliminamos
        if (contactoAEliminar != null) {
            if (contactos.eliminar(contactoAEliminar)) {
                System.out.println("Contacto '" + numeroTelefono + "' eliminado con éxito.");
                // Guardamos los cambios en el archivo
                PersistenciaContactos.guardarContactos(contactos);
            } else {
                // Este mensaje es por si algo falla internamente en la lista
                System.out.println("Error: No se pudo eliminar el contacto.");
            }
        } else {
            System.out.println("Contacto no encontrado.");
        }
    }

    // Método para editar un contacto existente
    public void editarContacto(Scanner sc) {
        if (contactos.estaVacia()) {
            System.out.println("No hay contactos para editar.");
            return;
        }

        System.out.print("Ingrese el número de teléfono del contacto a editar: ");
        String numeroBusqueda = sc.nextLine();

        Contacto contactoAEditar = null;
        for (Contacto c : contactos) {
            if (c.getNumeroTelefono().equalsIgnoreCase(numeroBusqueda)) {
                contactoAEditar = c;
                break;
            }
        }

        if (contactoAEditar == null) {
            System.out.println("No se encontró ningún contacto con ese número de teléfono.");
            return;
        }

        int opcion;
        do {
            System.out.println("\n--- Editando a: " + contactoAEditar.getNombre() + " ---");
            contactoAEditar.imprimirDetalles();
            System.out.println("\n¿Qué desea editar?");
            System.out.println("1. Nombre");
            System.out.println("2. Número de teléfono");
            System.out.println("3. Dirección");
            System.out.println("0. Volver al menú principal");
            System.out.print("Seleccione una opción: ");

            opcion = Integer.parseInt(sc.nextLine());

            switch (opcion) {
                case 1:
                    System.out.print("Nuevo nombre: ");
                    String nuevoNombre = sc.nextLine();
                    contactoAEditar.setNombre(nuevoNombre);
                    System.out.println("Nombre actualizado con éxito.");
                    break;
                case 2:
                    System.out.print("Nuevo número de teléfono: ");
                    String nuevoNumero = sc.nextLine();
                    if (existeNumeroTelefono(nuevoNumero)) {
                        System.out.println("Error: Ese número ya pertenece a otro contacto.");
                    } else {
                        contactoAEditar.setNumeroTelefono(nuevoNumero);
                        System.out.println("Número de teléfono actualizado con éxito.");
                    }
                    break;
                case 3:
                    System.out.print("Nueva dirección: ");
                    String nuevaDireccion = sc.nextLine();
                    contactoAEditar.setDireccion(nuevaDireccion);
                    System.out.println("Dirección actualizada con éxito.");
                    break;
                case 0:
                    System.out.println("Finalizando edición.");
                    break;
                default:
                    System.out.println("Opción no válida.");
                    break;
            }
            if (opcion >= 1 && opcion <= 3) {
                PersistenciaContactos.guardarContactos(contactos);
            }

        } while (opcion != 0);
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

    // Método para guardar contactos manualmente
    public void guardarContactos() {
        PersistenciaContactos.guardarContactos(contactos);
    }

    // Método para cargar contactos manualmente
    public void cargarContactos() {
        contactos = new ListaCircularDoble<>(); // Limpiar lista actual
        PersistenciaContactos.cargarContactos(contactos);
    }

    // Editar un atributo de un contacto
    public void editarAtributoContacto(Scanner sc) {
        if (contactos.estaVacia()) {
            System.out.println("No hay contactos.");
            return;
        }
        System.out.print("Nombre del contacto: ");
        String nombre = sc.nextLine();
        Contacto contacto = null;
        for (Contacto c : contactos) {
            if (c.getNombre().equalsIgnoreCase(nombre)) {
                contacto = c;
                break;
            }
        }
        if (contacto == null) {
            System.out.println("Contacto no encontrado.");
            return;
        }
        System.out.print("Nombre del atributo a editar: ");
        String nombreAtributo = sc.nextLine();
        boolean encontrado = false;
        for (Atributo a : contacto.getAtributos()) {
            if (a.getNombre().equalsIgnoreCase(nombreAtributo)) {
                System.out.print("Nuevo valor: ");
                String nuevoValor = sc.nextLine();
                a.setValor(nuevoValor);
                System.out.println("Atributo editado con exito.");
                encontrado = true;
                break;
            }
        }
        if (!encontrado) {
            System.out.println("Atributo no encontrado.");
        }
    }

    // Remover un atributo de un contacto
    public void removerAtributoContacto(Scanner sc) {
        if (contactos.estaVacia()) {
            System.out.println("No hay contactos.");
            return;
        }
        System.out.print("Nombre del contacto: ");
        String nombre = sc.nextLine();
        Contacto contacto = null;
        for (Contacto c : contactos) {
            if (c.getNombre().equalsIgnoreCase(nombre)) {
                contacto = c;
                break;
            }
        }
        if (contacto == null) {
            System.out.println("Contacto no encontrado.");
            return;
        }
        System.out.print("Nombre del atributo a remover: ");
        String nombreAtributo = sc.nextLine();
        Atributo atributoAEliminar = null;
        for (Atributo a : contacto.getAtributos()) {
            if (a.getNombre().equalsIgnoreCase(nombreAtributo)) {
                atributoAEliminar = a;
                break;
            }
        }
        if (atributoAEliminar != null) {
            contacto.getAtributos().eliminar(atributoAEliminar);
            System.out.println("Atributo eliminado con exito.");
        } else {
            System.out.println("Atributo no encontrado.");
        }
    }
    // Filtrar por apellido y primer nombre
    public void filtrarPorNombreApellido(Scanner sc) {
        if (contactos.estaVacia()) {
            System.out.println("No hay contactos.");
            return;
        }
        System.out.print("Ingrese el primer nombre a buscar: ");
        String primerNombre = sc.nextLine().trim().toLowerCase();
        System.out.print("Ingrese el apellido a buscar: ");
        String apellido = sc.nextLine().trim().toLowerCase();

        boolean encontrado = false;
        for (Contacto c : contactos) {
            String[] partes = c.getNombre().trim().toLowerCase().split("\\s+");
            if (partes.length >= 2) {
                if (partes[0].equals(primerNombre) && partes[partes.length - 1].equals(apellido)) {
                    c.imprimirDetalles();
                    encontrado = true;
                }
            }
        }
        if (!encontrado) {
            System.out.println("No se encontraron contactos con ese nombre y apellido.");
        }
    }
        // Filtrar por cantidad de atributos
    public void filtrarPorCantidadAtributos(Scanner sc) {
        if (contactos.estaVacia()) {
            System.out.println("No hay contactos.");
            return;
        }
        System.out.print("Ingrese la cantidad de atributos: ");
        int cantidad = Integer.parseInt(sc.nextLine());
        boolean encontrado = false;
        for (Contacto c : contactos) {
            if (c.getAtributos().getTamaño() == cantidad) {
                c.imprimirDetalles();
                encontrado = true;
            }
        }
        if (!encontrado) {
            System.out.println("No se encontraron contactos con esa cantidad de atributos.");
        }
    }
    // Filtrar por tipo de contacto
    public void filtrarPorTipoContacto(Scanner sc) {
        if (contactos.estaVacia()) {
            System.out.println("No hay contactos.");
            return;
        }
        System.out.print("Ingrese el tipo de contacto (Persona/Empresa): ");
        String tipo = sc.nextLine().trim().toLowerCase();
        boolean encontrado = false;
        for (Contacto c : contactos) {
            if (c.getTipo().toLowerCase().equals(tipo)) {
                c.imprimirDetalles();
                encontrado = true;
            }
        }
        if (!encontrado) {
            System.out.println("No se encontraron contactos de ese tipo.");
        }
    }
}
