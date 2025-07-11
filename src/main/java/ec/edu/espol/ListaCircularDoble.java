package ec.edu.espol;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ListaCircularDoble<T> implements Iterable<T> {

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
        if (estaVacia()) {
            return false;
        }

        Nodo<T> actual = cabeza;
        do {
            if (actual.dato.equals(dato)) {
                // Si el nodo a eliminar es el que usamos para navegar,
                // lo movemos al siguiente para evitar errores.
                if (actual == nodoActual) {
                    nodoActual = actual.siguiente;
                }

                if (tamaño == 1) {
                    cabeza = null;
                    nodoActual = null; // La lista queda vacía
                } else {
                    actual.anterior.siguiente = actual.siguiente;
                    actual.siguiente.anterior = actual.anterior;
                    if (actual == cabeza) {
                        cabeza = actual.siguiente;
                    }
                    // Si después de eliminar, el nodo actual apunta al nodo eliminado
                    // (porque era el único elemento), lo ponemos a null.
                    if (nodoActual == actual) {
                        nodoActual = cabeza;
                    }
                }
                tamaño--;
                return true; // Se eliminó con éxito
            }
            actual = actual.siguiente;
        } while (actual != cabeza);

        return false; // No se encontró el dato
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Nodo<T> actual = cabeza;
            private int visitados = 0;
            private boolean puedeEmpezar = tamaño > 0;

            @Override
            public boolean hasNext() {
                return puedeEmpezar && visitados < tamaño;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("No hay más elementos en la lista");
                }
                T dato = actual.dato;
                actual = actual.siguiente;
                visitados++;
                return dato;
            }
        };
    }
}
