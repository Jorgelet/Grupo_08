package ec.edu.espol;

public class ListaCircularDoble<T> {

    private Nodo<T> cabeza;
    private Nodo<T> nodoActual; // Nodo actual para iteración
    private int tamaño;

    // Clase interna para representar un nodo de la lista circular doble
    public static class Nodo<T> {
        T dato;
        Nodo<T> siguiente;
        Nodo<T> anterior;

        Nodo(T dato) {
            this.dato = dato;
        }
    }

    // Constructor de la lista circular doble
    public ListaCircularDoble() {
        cabeza = null;
        tamaño = 0;
    }

    // Método para agregar un nuevo dato a la lista
    public void agregar(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        if (cabeza == null) {
            cabeza = nuevo;
            cabeza.siguiente = cabeza;
            cabeza.anterior = cabeza;
            nodoActual = cabeza; // Inicializar nodoActual cuando se agrega el primer elemento
        } else {
            Nodo<T> ultimo = cabeza.anterior;
            ultimo.siguiente = nuevo;
            nuevo.anterior = ultimo;
            nuevo.siguiente = cabeza;
            cabeza.anterior = nuevo;
        }
        tamaño++;
    }

    public boolean estaVacia() {
        return tamaño == 0;
    }

    public int getTamaño() {
        return tamaño;
    }

    public Nodo<T> getCabeza() {
        return cabeza;
    }

    // Método para obtener el nodo actual
    public T getActual() {
        return nodoActual != null ? nodoActual.dato : null;
    }

    // Método para establecer el nodo actual al nodo siguiente
    public T next() {
        if (estaVacia())
            return null;
        if (nodoActual == null)
            nodoActual = cabeza; // Inicializar si es null
        nodoActual = nodoActual.siguiente;
        return nodoActual.dato;
    }

    // Método para establecer el nodo actual al nodo anterior
    public T prev() {
        if (estaVacia())
            return null;
        if (nodoActual == null)
            nodoActual = cabeza; // Inicializar si es null
        nodoActual = nodoActual.anterior;
        return nodoActual.dato;
    }

    public void imprimir() {
        if (estaVacia()) {
            System.out.println("Lista vacia.");
            return;
        }
        Nodo<T> actual = cabeza;
        do {
            System.out.println(actual.dato);
            actual = actual.siguiente;
        } while (actual != cabeza);
    }

    // Eliminar un nodo que contiene cierto dato
    public boolean eliminar(T dato) {
        if (estaVacia())
            return false;
        Nodo<T> actual = cabeza;
        do {
            if (actual.dato.equals(dato)) {
                if (tamaño == 1) {
                    cabeza = null;
                } else {
                    actual.anterior.siguiente = actual.siguiente;
                    actual.siguiente.anterior = actual.anterior;
                    if (actual == cabeza) {
                        cabeza = actual.siguiente;
                    }
                }
                tamaño--;
                return true;
            }
            actual = actual.siguiente;
        } while (actual != cabeza);
        return false;
    }
}
