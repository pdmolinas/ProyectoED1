package org.example;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class BST<T> implements org.example.interfaces.SearchTree<T> {
    protected static class Nodo<T> {
        private T dato;
        private Nodo<T> left;
        private Nodo<T> right;

        public Nodo(T dato) {
            this.dato = dato;
            this.left = null;
            this.right = null;
        }

        public T obtenerDato() {
            return this.dato;
        }
    }
    private Nodo<T> raiz;
    private Comparator<T> comparador;
    private int comparisons = 0;
    private boolean deleted;

    public BST(Comparator<T> comparador) {
        this.comparador = comparador;
        this.raiz = null;
    }

    @Override
    public boolean insert(T valor) {
        if (this.raiz == null) {
            this.raiz = new Nodo<>(valor);
            return true;
        }
        Nodo<T> actual = this.raiz;
        while (true) {
            int cmp = compare(valor, actual.obtenerDato());
            if (cmp == 0) return false;
            if (cmp < 0) {
                if (actual.left == null) {
                    actual.left = new Nodo<>(valor);
                    return true;
                }
                actual = actual.left;
            } else {
                if (actual.right == null) {
                    actual.right = new Nodo<>(valor);
                    return true;
                }
                actual = actual.right;
            }
        }
    }

    @Override
    public boolean search(T valor) {
        Nodo<T> actual = this.raiz;
        while (actual != null) {
            int cmp = compare(valor, actual.obtenerDato());
            if (cmp == 0) return true;
            if (cmp < 0) actual = actual.left;
            else actual = actual.right;
        }
        return false;
    }

    @Override
    public boolean delete(T valor) {
        deleted = false;
        if (!search(valor)) {
            return false;
        }
        this.raiz = delete(this.raiz, valor);
        return deleted;
    }

    private Nodo<T> delete(Nodo<T> raiz, T valor) {
        if (raiz == null) {
            deleted = false;
            return null;
        }

        int comparacion = compare(valor, raiz.obtenerDato());

        if (comparacion < 0) {
            raiz.left = delete(raiz.left, valor);
            return raiz;
        }

        if (comparacion > 0) {
            raiz.right = delete(raiz.right, valor);
            return raiz;
        }

        if (raiz.left == null) {
            deleted = true;
            return raiz.right;
        }

        if (raiz.right == null) {
            deleted = true;
            return raiz.left;
        }

        deleted = true;
        Nodo<T> sucesor = sucesorInOrder(raiz);
        raiz.dato = sucesor.dato;
        raiz.right = delete(raiz.right, sucesor.dato);
        return raiz;

    }
    @Override
    public void inOrderTraversal() {
        if (this.raiz == null) {
            return;
        }
        Nodo<T> auxiliar = this.raiz;
        Stack<Nodo<T>> stack = new Stack<>();
        while (!stack.isEmpty() || auxiliar != null) {
            while (auxiliar != null) {
                stack.push(auxiliar);
                auxiliar = auxiliar.left;
            }
            auxiliar = stack.pop();
            System.out.print(auxiliar.obtenerDato() + " ");
            auxiliar = auxiliar.right;
        }

    }
    @Override
    public void preOrderTraversal() {
        if (this.raiz == null) {
            return;
        }

        Stack<Nodo<T>> stack = new Stack<>();
        stack.push(this.raiz);

        while (!stack.isEmpty()) {
            Nodo<T> actual = stack.pop();
            System.out.print(actual.obtenerDato() + " ");

            if (actual.right != null) {
                stack.push(actual.right);
            }
            if (actual.left != null) {
                stack.push(actual.left);
            }
        }
    }

    @Override
    public void postOrderTraversal() {
        if (this.raiz == null) return;

        Stack<Nodo<T>> s1 = new Stack<>();
        Stack<Nodo<T>> s2 = new Stack<>();
        s1.push(this.raiz);

        while (!s1.isEmpty()) {
            Nodo<T> actual = s1.pop();
            s2.push(actual);

            if (actual.left != null) s1.push(actual.left);
            if (actual.right != null) s1.push(actual.right);
        }

        while (!s2.isEmpty()) {
            System.out.print(s2.pop().obtenerDato() + " ");
        }
    }

    @Override
    public void levelOrderTraversal() {
        if (this.raiz == null) return;

        Queue<Nodo<T>> queue = new LinkedList<>();
        queue.add(this.raiz);

        while (!queue.isEmpty()) {
            Nodo<T> actual = queue.poll();
            System.out.print(actual.obtenerDato() + " ");

            if (actual.left != null)  queue.add(actual.left);
            if (actual.right != null) queue.add(actual.right);
        }
    }

    @Override
    public int height() {
        if (this.raiz == null) return 0;

        int altura = 0;
        java.util.Queue<Nodo<T>> queue = new java.util.LinkedList<>();
        queue.offer(this.raiz);

        while (!queue.isEmpty()) {
            int size = queue.size();
            altura++;
            for (int i = 0; i < size; i++) {
                Nodo<T> actual = queue.poll();
                if (actual.left != null) queue.offer(actual.left);
                if (actual.right != null) queue.offer(actual.right);
            }
        }
        return altura;
    }

    public int contarNodos() {
        return contarNodos(this.raiz);
    }

    private int contarNodos(Nodo<T> nodo) {
        if (nodo == null) {
            return 0;
        }
        return 1 + contarNodos(nodo.left) + contarNodos(nodo.right);
    }

    public int contarHojas() {
        return contarHojas(this.raiz);
    }

    private int contarHojas(Nodo<T> nodo) {
        if (nodo == null) {
            return 0;
        }
        if (nodo.left == null && nodo.right == null) {
            return 1;
        }
        return contarHojas(nodo.left) + contarHojas(nodo.right);
    }

    public T min() {
        if (this.raiz == null) throw new IllegalStateException("El árbol está vacío");
        Nodo<T> actual = this.raiz;
        while (actual.left != null) actual = actual.left;
        return actual.obtenerDato();
    }

    public T max() {
        if (this.raiz == null) throw new IllegalStateException("El árbol está vacío");
        Nodo<T> actual = this.raiz;
        while (actual.right != null) actual = actual.right;
        return actual.obtenerDato();
    }

    private Nodo<T> sucesorInOrder(Nodo<T> raiz) {
        raiz = raiz.right;
        while (raiz != null && raiz.left != null) {
            raiz = raiz.left;
        }
        return raiz;
    }
    private int compare(T a, T b) {
        comparisons++;
        return comparador.compare(a, b);
    }
    @Override
    public void resetMetrics() {
        comparisons = 0;
    }
    public int getComparisons() {
        return comparisons;
    }

    @Override

    public void getMetrics() {
        System.out.println("Comparaciones: " + comparisons);
    }



}

