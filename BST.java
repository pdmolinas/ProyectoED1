import java.util.Comparator;
import java.util.HashSet;
import java.util.Stack;

public class BST<T> {
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

    public BST(Comparator<T> comparador) {
        this.comparador = comparador;
        this.raiz = null;
    }

    public boolean insertar(T valor) {
        if (this.raiz == null) {
            this.raiz = new Nodo<>(valor);
            return true;
        }
        return insertar(valor, this.raiz) != null;

    }
    private Nodo<T> insertar(T valor, Nodo<T> nodo) {
        if (nodo == null) {
            return new Nodo<>(valor);
        }
        if (compare(valor, nodo.obtenerDato()) == 0) {
            return null;
        }
        if (compare(valor, nodo.obtenerDato()) < 0) {
            nodo.left = insertar(valor, nodo.left);
            return nodo;
        }
        nodo.right = insertar(valor, nodo.right);
        return nodo;
    }
    public Nodo<T> buscar(T valor) {
        return buscar(this.raiz, valor);
    }

    private Nodo<T> buscar(Nodo<T> raiz, T valor) {
        if (raiz == null)
            return null;
        if (compare(raiz.obtenerDato(), valor) == 0)
            return raiz;
        if (compare(raiz.obtenerDato(), valor) > 0)
            return buscar(raiz.left, valor);
        return buscar(raiz.right, valor);
    }

    public Nodo<T> eliminar(T valor) {
        Nodo<T> eliminado = buscar(valor);
        if (eliminado == null) {
            return null;
        }
        this.raiz = eliminar(this.raiz, valor);
        return eliminado;
    }

    private Nodo<T> eliminar(Nodo<T> raiz, T valor) {
        if (raiz == null) {
            return null;
        }

        int comparacion = compare(valor, raiz.obtenerDato());

        if (comparacion < 0) {
            raiz.left = eliminar(raiz.left, valor);
            return raiz;
        }

        if (comparacion > 0) {
            raiz.right = eliminar(raiz.right, valor);
            return raiz;
        }

        if (raiz.left == null) {
            return raiz.right;
        }

        if (raiz.right == null) {
            return raiz.left;
        }

        Nodo<T> sucesor = sucesorInOrder(raiz);
        raiz.dato = sucesor.dato;
        raiz.right = eliminar(raiz.right, sucesor.dato);
        return raiz;

    }

    public void inOrder() {
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

    public void preOrder() {
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

    public void postOrder() {
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

    public void recorridoPorNivel() {
        if (this.raiz == null) {
            return;
        }

        int alturaArbol = altura();
        for (int nivel = 1; nivel <= alturaArbol; nivel++) {
            System.out.print("Nivel " + nivel + ": ");
            imprimirNivel(this.raiz, nivel);
            System.out.println();
        }
    }

    public int altura() {
        return altura(this.raiz);
    }

    private int altura(Nodo<T> nodo) {
        if (nodo == null) {
            return 0;
        }

        int alturaIzquierda = altura(nodo.left);
        int alturaDerecha = altura(nodo.right);
        return Math.max(alturaIzquierda, alturaDerecha) + 1;
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

    // Un arbol esta balanceado si para cada nodo, la diferencia de altura
    // entre su subarbol izquierdo y derecho es como maximo 1
    private boolean estaBalanceado(Nodo<T> nodo) {
        if (nodo == null) {
            return true;
        }
        int alturaIzquierda = altura(nodo.left);
        int alturaDerecha = altura(nodo.right);
        if (Math.abs(alturaIzquierda - alturaDerecha) > 1) {
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
    public void clearMetrics() {
        comparisons = 0;
    }
    public int getComparisons() {
        return comparisons;
    }




}

