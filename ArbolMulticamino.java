import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;

public class ArbolMulticamino<T> {

    private static class Nodo<T> {
        T dato;
        List<Nodo<T>> hijos;

        Nodo(T dato) {
            this.dato = dato;
            this.hijos = new ArrayList<>();
        }

        boolean esHoja() {
            return hijos.isEmpty();
        }
    }
    private Nodo<T> raiz;

    public ArbolMulticamino() {
        this.raiz = null;

    }

    public void insertarRaiz(T dato) {
        if (raiz != null) {
            throw new IllegalStateException("La raiz ya existe");
        }
        raiz = new Nodo<>(dato);
    }

    public boolean agregarHijo(T padre, T hijo) {
        Nodo<T> nodoPadre = buscarNodo(raiz, padre);
        if (nodoPadre == null) {
            return false;
        }
        nodoPadre.hijos.add(new Nodo<>(hijo));
        return true;
    }

    private Nodo<T> buscarNodo(Nodo<T> actual, T datoBuscado) {
        if (actual == null) {
            return null;
        }

        if (actual.dato.equals(datoBuscado)) {
            return actual;
        }

        for (Nodo<T> hijo : actual.hijos) {
            Nodo<T> encontrado = buscarNodo(hijo, datoBuscado);
            if (encontrado != null) {
                return encontrado;
            }
        }

        return null;
    }

    public int profundidadMaxima() {
        return profundidadMaxima(this.raiz);
    }

    private int profundidadMaxima(Nodo<T> actual) {
        if (actual == null) {
            return 0;
        }
        if(actual.esHoja()) {
            return 1;
        }
        int maxProfundidad = 0;
        for (Nodo<T> hijo : actual.hijos) {
            int profundidadHijo = profundidadMaxima(hijo);
            if (profundidadHijo > maxProfundidad) {
                maxProfundidad = profundidadHijo;
            }
        }
        return 1 + maxProfundidad;
    }

    public int contarHojas() {
        return contarHojas(this.raiz);
    }

    private int contarHojas(Nodo<T> actual) {
        if (actual == null) {
            return 0;
        }
        if(actual.esHoja()) {
            return 1;
        }
        int totalHojas = 0;
        for (Nodo<T> hijo : actual.hijos) {
            totalHojas += contarHojas(hijo);
        }
        return totalHojas;
    }

    public int contarNodosInternos() {
        return contarNodosInternos(this.raiz);
    }

    private int contarNodosInternos(Nodo<T> actual) {
        if (actual == null || actual.esHoja()) {
            return 0;
        }
        int totalInternos = 1; 
        for (Nodo<T> hijo : actual.hijos) {
            totalInternos += contarNodosInternos(hijo);
        }
        return totalInternos;
    }

    public double factorPromedioRamificacion() {
        if (raiz == null) {
            return 0.0;
        }
        int totalHijos = contarTotalHijos(raiz);
        int nodosInternos = contarNodosInternos(raiz);
        
        return (double) totalHijos / nodosInternos;
    }
    private int contarTotalHijos(Nodo<T> actual) {
        if (actual == null) {
            return 0;
        }
        int totalHijos = actual.hijos.size();
        for (Nodo<T> hijo : actual.hijos) {
            totalHijos += contarTotalHijos(hijo);
        }
        return totalHijos;
    }

    public void recorridoPorNiveles() {
        if (raiz == null) {
            return;
        }
        Queue<Nodo<T>> cola = new LinkedList<>();
        cola.add(raiz);
        while (!cola.isEmpty()) {
            Nodo<T> actual = cola.poll();
            System.out.print(actual.dato + " ");
            for (Nodo<T> hijo : actual.hijos) {
                cola.add(hijo);
            }
            System.out.println(); 
        }
    }
}