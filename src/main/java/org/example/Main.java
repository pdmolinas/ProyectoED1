package org.example;

public class Main {
    public static void main(String[] args) {
        BST<Integer> BST = new BST<>(Integer::compareTo);

        BST.insert(84);
        BST.insert(32);
        BST.insert(15);
        BST.insert(7);
        BST.insert(91);
        BST.insert(3);
        BST.insert(10);
        BST.insert(85);
        BST.insert(99);

        System.out.println("Probando imprimir en In-order:");
        BST.inOrderTraversal();
        System.out.println("\nProbando imprimir en Pre-order:");
        BST.preOrderTraversal();
        System.out.println("\nProbando imprimir en Post-order:");
        BST.postOrderTraversal();
        System.out.println("\nProbando imprimir por niveles:");
        BST.levelOrderTraversal();

        System.out.println("\n\nProbando funciones del Arbolinix:");
        System.out.println("Altura: " + BST.height());
        System.out.println("Min: " + BST.min());
        System.out.println("Max: " + BST.max());
        System.out.println("Total nodos: " + BST.contarNodos());
        System.out.println("Total hojas: " + BST.contarHojas());
        System.out.println("Está balanceado?: " + BST.estaBalanceado());

        System.out.println("\nProbando Metricas:");
        BST.getMetrics();

        System.out.println("\nBuscar 50 (esperando false): " + BST.search(50));
        System.out.println("Buscar 15 (esperando true): " + BST.search(15));
        System.out.println("Insertando 84 un duplicado (esperando false): " + BST.insert(84));
        System.out.println("Eliminar 3 (esperado true): " + BST.delete(3));
        System.out.println("Buscar 3 después de eliminar (esperando false): " + BST.search(3));
        System.out.println("Estado del arbol:");
        BST.inOrderTraversal();
        System.out.println("\nEliminar 7 (un hijo, esperando true): " + BST.delete(7));
        System.out.println("Estado del arbol:");
        BST.inOrderTraversal();
        System.out.println("\nEliminar 32 (dos hijos, esperando true): " + BST.delete(32));
        System.out.println("Estado del arbol:");
        BST.inOrderTraversal();
        System.out.println("\nEliminar 999 (no existe, esperando false): " + BST.delete(999));

        BST.resetMetrics();
        System.out.println("\nMetricas despues de reset:");
        BST.getMetrics();

    }
}