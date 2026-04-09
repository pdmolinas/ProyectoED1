package org.example.estructuras;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;

public class ArbolMulticamino<T> {

    protected static class Nodo<T> {
        T dato;
        List<Nodo<T>> hijos;
        int profundidad = 0;

        Nodo(T dato) {
            this.dato = dato;
            this.hijos = new ArrayList<>();
            this.profundidad = 0;
        }
        public void setProfundidad(int profundidad) {
            this.profundidad = profundidad;
        }

        boolean esHoja() {
            return hijos.isEmpty();
        }
    }
    private Nodo<T> raiz;
    private int maxHeight;
    private int maxHijos;

    public ArbolMulticamino(int maxHeight, int  maxHijos) {
        this.raiz = null;
        this.maxHeight = maxHeight;
        this.maxHijos = maxHijos;

    }

    public void insertarRaiz(T dato) {
        if (raiz != null) {
            throw new IllegalStateException("La raiz ya existe");
        }
        raiz = new Nodo<>(dato);
        raiz.profundidad = 1;
    }

    public boolean agregarHijo(T padre, T hijo) {
        Nodo<T> nodoPadre = buscarNodo(raiz, padre);
        if (nodoPadre == null) {
            return false;
        }
        if (nodoPadre.profundidad >= maxHeight) {
            return false;
        }
        if (nodoPadre.hijos.size() >= maxHijos) {
            return false;
        }
        Nodo<T> nuevoHijo = new Nodo<>(hijo);
        nuevoHijo.setProfundidad(nodoPadre.profundidad + 1);
        nodoPadre.hijos.add(nuevoHijo);

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

    public int contarNodosEnSubarbol(T dato) {
        Nodo<T> nodo = buscarNodo(raiz, dato);
        if (nodo == null) return 0;
        return contarTotalHijos(nodo) + 1;
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
        if (nodosInternos == 0) return 0.0;
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

    public void mostrarJerarquia() {
        if (raiz == null) {
            System.out.println("(árbol vacío)");
            return;
        }
        mostrarJerarquia(raiz, 0);
    }

    private void mostrarJerarquia(Nodo<T> actual, int nivel) {
        String prefijo = "  ".repeat(nivel);
        String marcador = nivel == 0 ? "" : "└─ ";
        System.out.println(prefijo + marcador + actual.dato);
        for (Nodo<T> hijo : actual.hijos) {
            mostrarJerarquia(hijo, nivel + 1);
        }
    }

    public void recorridoPorNiveles() {
        if (raiz == null) return;

        Queue<Nodo<T>> cola = new LinkedList<>();
        cola.offer(raiz);

        while (!cola.isEmpty()) {
            int size = cola.size();

            for (int i = 0; i < size; i++) {
                Nodo<T> actual = cola.poll();
                System.out.print(actual.dato + " ");

                for (Nodo<T> hijo : actual.hijos) {
                    cola.offer(hijo);
                }
            }
            System.out.println();
        }
    }
}