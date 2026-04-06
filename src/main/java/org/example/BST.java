package org.example;
import java.util.Comparator;
import java.util.HashSet;
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
        if (this.raiz == null) {
            return;
        }
        Nodo<T> actual = this.raiz;
        HashSet<Nodo<T>> visited = new HashSet<>();
        while (actual != null && !visited.contains(actual)) {
            if (actual.left != null && !visited.contains(actual.left)) {
                actual = actual.left;
            } else if (actual.right != null && !visited.contains(actual.right)) {
                actual = actual.right;
            } else {
                System.out.print(actual.obtenerDato() + " ");
                visited.add(actual);
                actual = this.raiz;
            }
        }
    }
@Override
    public void levelOrderTraversal() {
        if (this.raiz == null) {
            return;
        }

        int heightArbol = height();
        for (int nivel = 1; nivel <= heightArbol; nivel++) {
            System.out.print("Nivel " + nivel + ": ");
            imprimirNivel(this.raiz, nivel);
            System.out.println();
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

    private void imprimirNivel(Nodo<T> nodo, int nivel) {
        if (nodo == null) {
            return;
        }

        if (nivel == 1) {
            System.out.print(nodo.obtenerDato() + " ");
            return;
        }

        imprimirNivel(nodo.left, nivel - 1);
        imprimirNivel(nodo.right, nivel - 1);
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

    public Nodo<T> min() {
        return min(this.raiz);
    }

    private Nodo<T> min(Nodo<T> nodo) {
        if (nodo == null) {
            throw new IllegalStateException("El árbol está vacío");
        }
        Nodo<T> actual = nodo;
        while (actual.left != null) {
            actual = actual.left;
        }
        if (!(actual.obtenerDato() instanceof Integer)) {
            throw new IllegalStateException("min solo funciona con datos numéricos");
        }
        return actual;
    }

    public int max() {
        return max(this.raiz);
    }

    private int max(Nodo<T> nodo) {
        if (nodo == null) {
            throw new IllegalStateException("El árbol está vacío");
        }
        Nodo<T> actual = nodo;
        while (actual.right != null) {
            actual = actual.right;
        }
        if (!(actual.obtenerDato() instanceof Integer)) {
            throw new IllegalStateException("max solo funciona con datos numéricos");
        }
        return (Integer) actual.obtenerDato();
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

    public int sumarNodos() {
        return sumarNodos(this.raiz);
    }

    private int sumarNodos(Nodo<T> nodo) {
        if (nodo == null) {
            return 0;
        }

        if (!(nodo.obtenerDato() instanceof Integer)) {
            throw new IllegalStateException("sumarNodos solo funciona con datos numéricos");
        }

        int valorActual = (Integer) nodo.obtenerDato();
        return valorActual + sumarNodos(nodo.left) + sumarNodos(nodo.right);

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

