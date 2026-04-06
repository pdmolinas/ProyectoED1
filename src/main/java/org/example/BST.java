package org.example;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

// Checklist para interfaz
// boolean insert(T value);
// boolean search(T value);
// boolean delete(T value);
// void inOrderTraversal();
// void preOrderTraversal();
// void postOrderTraversal();
// void levelOrderTraversal();
// int height();
// void getMetrics();
// void resetMetrics();

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
        return insert(valor, this.raiz) != null;

    }
    private Nodo<T> insert(T valor, Nodo<T> nodo) {
        if (nodo == null) {
            return new Nodo<>(valor);
        }
        if (compare(valor, nodo.obtenerDato()) == 0) {
            return null;
        }
        if (compare(valor, nodo.obtenerDato()) < 0) {
            nodo.left = insert(valor, nodo.left);
            return nodo;
        }
        nodo.right = insert(valor, nodo.right);
        return nodo;
    }
    @Override
    public boolean search(T valor) {
        return search(this.raiz, valor);
    }

    private boolean search(Nodo<T> raiz, T valor) {
        if (raiz == null)
            return false;
        if (compare(raiz.obtenerDato(), valor) == 0)
            return true;
        if (compare(raiz.obtenerDato(), valor) > 0)
            return search(raiz.left, valor);
        return search(raiz.right, valor);
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
        return height(this.raiz);
    }

    private int height(Nodo<T> nodo) {
        if (nodo == null) {
            return 0;
        }

        int heightIzquierda = height(nodo.left);
        int heightDerecha = height(nodo.right);
        return Math.max(heightIzquierda, heightDerecha) + 1;
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

    public boolean estaBalanceado() {
        return estaBalanceado(this.raiz);
    }

    // Un arbol esta balanceado si para cada nodo, la diferencia de height
    // entre su subarbol izquierdo y derecho es como maximo 1
    private boolean estaBalanceado(Nodo<T> nodo) {
        if (nodo == null) {
            return true;
        }
        int heightIzquierda = height(nodo.left);
        int heightDerecha = height(nodo.right);
        if (Math.abs(heightIzquierda - heightDerecha) > 1) {
            return false;
        }
        return estaBalanceado(nodo.left) && estaBalanceado(nodo.right);
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

