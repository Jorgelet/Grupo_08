package ec.edu.espol;

public class ListaCircularDoble<T> {

    private Nodo<T> cabeza;
    private Nodo<T> nodoActual;
    private int tamaño;

    public static class Nodo<T> {
        T dato;
        Nodo<T> siguiente;
        Nodo<T> anterior;

        Nodo(T dato) {
            this.dato = dato;
        }
    }

    public ListaCircularDoble() {
        cabeza = null;
        tamaño = 0;
    }

    public void agregar(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        if (cabeza == null) {
            cabeza = nuevo;
            cabeza.siguiente = cabeza;
            cabeza.anterior = cabeza;
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

    public T getActual() {
        return nodoActual != null ? nodoActual.dato : null;
    }

    public T next() {
        if (estaVacia()) return null;
        nodoActual = nodoActual.siguiente;
        return nodoActual.dato;
    }

    public T prev() {
        if (estaVacia()) return null;
        nodoActual = nodoActual.anterior;
        return nodoActual.dato;
    }

    public void imprimir() {
        if (estaVacia()) {
            System.out.println("Lista vacía.");
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
        if (estaVacia()) return false;
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
