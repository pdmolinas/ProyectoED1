
package org.example;

import org.example.elementosTransito.Event;
import org.example.elementosTransito.Interseccion;
import org.example.interfaces.SearchTree;

class SistemaControl {
    private final ArbolMulticamino<String> ciudad;
    private SearchTree<Interseccion> bst;
    private SearchTree<Interseccion> avl;
    private SearchTree<Interseccion> activeTree;
    private final PriorityQueueMaxHeap<Event> heap;

    public SistemaControl() {

        this.bst = new BST<>((a, b) -> a.getId() - b.getId());
        this.avl = new AVL<>((a, b) -> a.getId() - b.getId());
        this.activeTree = bst; // BST por defecto
        this.heap = new PriorityQueueMaxHeap<>((a, b) -> a.getRiskLevel() - b.getRiskLevel());
        this.ciudad = new ArbolMulticamino<>(5, 10);
    }

    public void usarBST() {
        this.activeTree = bst;
        System.out.println("Usando BST como estructura de indexación.");
    }

    public void usarAVL() {
        this.activeTree = avl;
        System.out.println("Usando AVL como estructura de indexación.");
    }

    public boolean insertarInterseccion(Interseccion interseccion) {
        return activeTree.insert(interseccion);
    }

    public boolean buscarInterseccion(Interseccion interseccion) {
        return activeTree.search(interseccion);
    }

    public boolean eliminarInterseccion(Interseccion interseccion) {
        return activeTree.delete(interseccion);
    }

    public void insertarEvento(Event evento) {
        heap.insert(evento);
    }

    public Event procesarEventoMasPrioritario() {
        return heap.pop();
    }

    public Event verEventoMasPrioritario() {
        return heap.peek();
    }

    public boolean modificarPrioridad(Event oldEvento, Event newEvento) {
        return heap.updatePriority(oldEvento, newEvento);
    }

    // Cambiar criterio de prioridad dinámicamente con lambda
    public void ordenarEventosPorRiesgo() {
        heap.changeComparator((a, b) -> a.getRiskLevel() - b.getRiskLevel());
        System.out.println("Eventos ordenados por nivel de riesgo.");
    }

    public void ordenarEventosPorTiempo() {
        heap.changeComparator((a, b) -> a.getReportTime() - b.getReportTime());
        System.out.println("Eventos ordenados por tiempo de reporte.");
    }

    public void insertarRaizCiudad(String nombre) {
        ciudad.insertarRaiz(nombre);
    }

    public boolean agregarElementoCiudad(String padre, String hijo) {
        return ciudad.agregarHijo(padre, hijo);
    }

    public int contarNodosEnDistrito(String distrito) {
        return ciudad.contarNodosEnSubarbol(distrito);
    }

    public int profundidadMaximaCiudad() {
        return ciudad.profundidadMaxima();
    }

    public void mostrarCiudadPorNiveles() {
        ciudad.recorridoPorNiveles();
    }

    public void mostrarInOrder() {
        activeTree.inOrderTraversal();
    }

    public void mostrarPreOrder() {
        activeTree.preOrderTraversal();
    }

    public void mostrarPostOrder() {
        activeTree.postOrderTraversal();
    }

    public void mostrarPorNiveles() {
        activeTree.levelOrderTraversal();
    }

    public void mostrarEstadisticas() {
        System.out.println("\nEstadísticas");
        System.out.println("-----------------------");
        System.out.println("Altura árbol activo: " + activeTree.height());
        System.out.println("Altura BST: " + bst.height());
        System.out.println("Altura AVL: " + avl.height());
        System.out.println("Profundidad máxima ciudad: " + ciudad.profundidadMaxima());
        System.out.println("Hojas en ciudad: " + ciudad.contarHojas());
        System.out.println("Nodos internos en ciudad: " + ciudad.contarNodosInternos());
        System.out.println("Factor ramificación ciudad: " + ciudad.factorPromedioRamificacion());
        System.out.println("Eventos en cola: " + heap.size());
        activeTree.getMetrics();
    }

    public void resetearMetricas() {
        activeTree.resetMetrics();
    }

    public void cambiarComparadorPorCongestion() {
        bst = new BST<>((a, b) -> a.getCongestionLevel() - b.getCongestionLevel());
        avl = new AVL<>((a, b) -> a.getCongestionLevel() - b.getCongestionLevel());
        System.out.println("Comparador cambiado a nivel de congestión.");
    }

    public void cambiarComparadorPorRiesgo() {
        bst = new BST<>((a, b) -> a.getRiskLevel() - b.getRiskLevel());
        avl = new AVL<>((a, b) -> a.getRiskLevel() - b.getRiskLevel());
        System.out.println("Comparador cambiado a nivel de riesgo.");
    }


}