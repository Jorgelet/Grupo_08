package ec.edu.espol;

import java.util.Comparator;
import java.util.PriorityQueue;
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

        String apellido = "";
        // Si es una persona, solicitamos el apellido
        // Ya que una empresa puede no tener apellido
        if (tipo.equalsIgnoreCase("Persona")) {
            System.out.print("Apellido: ");
            apellido = sc.nextLine().trim();
        }

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

        Contacto c = new Contacto(tipo, nombre, apellido, numeroTelefono, direccion);

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

    // Método para ordenar la lista de contactos
    public void ordenarContactos(Scanner sc) {
        if (contactos.estaVacia()) {
            System.out.println("No hay contactos para ordenar.");
            return;
        }

        System.out.println("\n--- Criterios de Ordenación ---");
        System.out.println("1. Por Apellido y Nombre");
        System.out.println("2. Por Cantidad de Atributos");
        System.out.println("3. Por Tipo de Contacto (Persona/Empresa)");
        System.out.print("Seleccione una opción: ");
        int opcion = Integer.parseInt(sc.nextLine());

        Comparator<Contacto> comparador = null;
        switch (opcion) {
            case 1:
                // Ordena por apellido, luego por nombre. Las empresas van al final debido a que
                // pueden no tener apellido
                comparador = Comparator.comparing(Contacto::getTipo).reversed()
                        .thenComparing(Contacto::getApellido, String.CASE_INSENSITIVE_ORDER)
                        .thenComparing(Contacto::getNombre, String.CASE_INSENSITIVE_ORDER);
                break;
            case 2:
                // Ordena por número de atributos, de mayor a menor.
                comparador = Comparator.comparingInt((Contacto c) -> c.getAtributos().getTamaño()).reversed();
                break;
            case 3:
                // Ordena por tipo (Empresa primero, luego Persona).
                comparador = Comparator.comparing(Contacto::getTipo);
                break;
            default:
                System.out.println("Opción no válida.");
                return;
        }

        PriorityQueue<Contacto> colaPrioridad = new PriorityQueue<>(comparador);

        for (Contacto c : this.contactos) {
            colaPrioridad.add(c);
        }

        ListaCircularDoble<Contacto> listaOrdenada = new ListaCircularDoble<>();

        while (!colaPrioridad.isEmpty()) {
            listaOrdenada.agregar(colaPrioridad.poll());
        }

        this.contactos = listaOrdenada;

        System.out.println("Contactos ordenados con éxito. Puede ver el resultado en 'Ver lista completa'.");
        PersistenciaContactos.guardarContactos(contactos);
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
        System.out.print("Ingrese el apellido a buscar (deje vacio si es empresa): ");
        String apellido = sc.nextLine().trim().toLowerCase();

        boolean encontrado = false;
        for (Contacto c : contactos) {
            if (c.getTipo().equalsIgnoreCase("Empresa")) {
                // Para empresa, solo compara el nombre completo
                if (c.getNombre().trim().toLowerCase().equals(primerNombre)) {
                    c.imprimirDetalles();
                    encontrado = true;
                }
            } else {
                // Para persona, compara primer nombre y apellido
                String[] partes = c.getNombre().trim().toLowerCase().split("\\s+");
                if (partes.length >= 2) {
                    if (partes[0].equals(primerNombre) && partes[partes.length - 1].equals(apellido)) {
                        c.imprimirDetalles();
                        encontrado = true;
                    }
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

    // Método para ver las fotos de un contacto
    public void verFotosDeContacto(Scanner sc) {
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
        ListaCircularDoble<Foto> fotos = contacto.getFotos();
        if (fotos.estaVacia() || fotos.getTamaño() < 2) {
            System.out.println("El contacto debe tener al menos 2 fotos.");
            return;
        }

        ListaCircularDoble.Nodo<Foto> actual = fotos.getCabeza();
        int opcion;
        do {
            System.out.println("\nFoto actual: " + actual.dato.getNombreArchivo());
            System.out.println("1. Ver foto siguiente");
            System.out.println("2. Ver foto anterior");
            System.out.println("0. Salir de la galeria de fotos");
            System.out.print("Seleccione una opcion: ");
            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }
            switch (opcion) {
                case 1:
                    actual = actual.siguiente;
                    break;
                case 2:
                    actual = actual.anterior;
                    break;
                case 0:
                    System.out.println("Saliendo de la galeria de fotos.");
                    break;
                default:
                    System.out.println("Opcion invalida.");
            }
        } while (opcion != 0);
    }

    // Método para asociar contactos
    public void asociarContactos(Scanner sc) {
        if (contactos.getTamaño() < 2) {
            System.out.println("Necesita al menos 2 contactos para crear una asociación.");
            return;
        }
        System.out.print("Teléfono del contacto principal: ");
        String tel1 = sc.nextLine();
        Contacto c1 = buscarPorTelefono(tel1);
        if (c1 == null) {
            System.out.println("Contacto principal no encontrado.");
            return;
        }

        System.out.print("Teléfono del contacto a asociar: ");
        String tel2 = sc.nextLine();
        Contacto c2 = buscarPorTelefono(tel2);
        if (c2 == null) {
            System.out.println("Contacto a asociar no encontrado.");
            return;
        }

        c1.agregarAsociado(c2);
        c2.agregarAsociado(c1);

        System.out.println("Asociación creada con éxito entre " + c1.getNombre() + " y " + c2.getNombre());
        PersistenciaContactos.guardarContactos(contactos);
    }

    private Contacto buscarPorTelefono(String telefono) {
        for (Contacto c : contactos) {
            if (c.getNumeroTelefono().equalsIgnoreCase(telefono)) {
                return c;
            }
        }
        return null;
    }

    // Método para ver los contactos asociados a un contacto específico
    public void verContactosAsociados(Scanner sc) {
        if (contactos.estaVacia()) {
            System.out.println("No hay contactos.");
            return;
        }
        System.out.print("Teléfono del contacto para ver sus asociados: ");
        String tel = sc.nextLine();
        Contacto c = buscarPorTelefono(tel);

        if (c == null) {
            System.out.println("Contacto no encontrado.");
            return;
        }
        ListaCircularDoble<Contacto> asociados = c.getAsociados();
        if (asociados.estaVacia()) {
            System.out.println("Este contacto no tiene asociados.");
            return;
        }
        System.out.println("Contactos asociados:");
        int i = 1;
        for (Contacto a : asociados) {
            System.out.println(i + ". " + a.getNombre() + " " + a.getApellido());
            i++;
        }

        System.out.print("Seleccione el número de un contacto asociado para ver detalles (0 para salir): ");
        int opcion = Integer.parseInt(sc.nextLine());

        if (opcion > 0 && opcion <= asociados.getTamaño()) {
            int contador = 1;
            for (Contacto asociadoSeleccionado : asociados) {
                if (contador == opcion) {
                    asociadoSeleccionado.imprimirDetalles();
                    break;
                }
                contador++;
            }
        } else if (opcion != 0) {
            System.out.println("Opción no válida.");
        }
    }

    // Método para Búsqueda Avanzada con múltiples criterios
    public void busquedaAvanzada(Scanner sc) {
        if (contactos.estaVacia()) {
            System.out.println("No hay contactos para buscar.");
            return;
        }

        System.out.println("\n--- Búsqueda Avanzada ---");
        System.out.println("Deje en blanco los campos opcionales que no desee usar como filtro.");

        System.out.print("Filtrar por Nombre (obligatorio): ");
        String nombreFiltro = sc.nextLine().trim().toLowerCase();

        System.out.print("Filtrar por Apellido (obligatorio): ");
        String apellidoFiltro = sc.nextLine().trim().toLowerCase();

        System.out.print("Filtrar por Dirección (obligatorio): ");
        String direccionFiltro = sc.nextLine().trim().toLowerCase();

        System.out.print("Filtrar por Nombre de Atributo específico: (opcional)");
        String nombreAtributoFiltro = sc.nextLine().trim().toLowerCase();
        String valorAtributoFiltro = "";
        if (!nombreAtributoFiltro.isEmpty()) {
            System.out.print("Valor del Atributo '" + nombreAtributoFiltro + "' (obligatorio): ");
            valorAtributoFiltro = sc.nextLine().trim().toLowerCase();
        }

        System.out.println("\n--- Resultados de la Búsqueda ---");
        boolean encontrado = false;

        for (Contacto c : contactos) {
            // Asumimos que el contacto si es un resultado válido
            // Luego vamos descartando si no cumple con los filtros
            boolean cumple = true;

            // 1. Chequeo de Nombre
            if (cumple && !nombreFiltro.isEmpty() && !c.getNombre().toLowerCase().contains(nombreFiltro)) {
                cumple = false;
            }

            // 2. Chequeo de Apellido
            if (cumple && !apellidoFiltro.isEmpty()) {
                if (c.getApellido() == null || !c.getApellido().toLowerCase().contains(apellidoFiltro)) {
                    cumple = false;
                }
            }

            // 3. Chequeo de Dirección
            if (cumple && !direccionFiltro.isEmpty() && !c.getDireccion().toLowerCase().contains(direccionFiltro)) {
                cumple = false;
            }

            // 4. Chequeo de Atributo
            if (cumple && !nombreAtributoFiltro.isEmpty()) {
                boolean atributoEncontrado = false;
                for (Atributo a : c.getAtributos()) {
                    if (a.getNombre().toLowerCase().equals(nombreAtributoFiltro)
                            && a.getValor().toLowerCase().contains(valorAtributoFiltro)) {
                        atributoEncontrado = true;
                        break;
                    }
                }
                if (!atributoEncontrado) {
                    cumple = false; // Si no se encontró el atributo con ese valor, lo descartamos
                }
            }

            // Si el contacto pasó todos los filtros, lo mostramos
            if (cumple) {
                c.imprimirDetalles();
                System.out.println("---------------------------------");
                encontrado = true;
            }
        }

        if (!encontrado) {
            System.out.println("No se encontraron contactos que coincidan con todos los criterios especificados.");
        }
    }
}
