package org.example;

import org.example.elementosTransito.*;
import org.example.enums.urbanLevel;
import org.example.estructuras.AVL;
import org.example.estructuras.ArbolMulticamino;
import org.example.estructuras.BST;
import org.example.estructuras.PriorityQueueMaxHeap;
import org.example.interfaces.SearchTree;
import org.example.interfaces.cityPart;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class SistemaControl {

    private final ArbolMulticamino<cityPart> ciudad;
    private SearchTree<Interseccion> bst;
    private SearchTree<Interseccion> avl;
    private SearchTree<Interseccion> activeTree;
    private final PriorityQueueMaxHeap<Event> heap;
    private final Map<Integer, Interseccion> indiceIntersecciones = new HashMap<>();
    private boolean ciudadCargada = false;

    public SistemaControl() {
        this.bst = new BST<>((a, b) -> a.getId() - b.getId());
        this.avl = new AVL<>((a, b) -> a.getId() - b.getId());
        this.activeTree = bst;
        this.heap = new PriorityQueueMaxHeap<>((a, b) -> a.getRiskLevel() - b.getRiskLevel());
        this.ciudad = new ArbolMulticamino<>(6, 10);
    }

    public void usarBST() {
        this.activeTree = bst;
        System.out.println("Usando BST como estructura de indexación.");
    }

    public void usarAVL() {
        this.activeTree = avl;
        System.out.println("Usando AVL como estructura de indexación.");
    }

    public void cambiarComparadorPorId() {
        boolean usandoBST = (activeTree == bst);
        bst = new BST<>((a, b) -> a.getId() - b.getId());
        avl = new AVL<>((a, b) -> a.getId() - b.getId());
        activeTree = usandoBST ? bst : avl;
        System.out.println("Comparador cambiado a ID. Nota: el árbol se reinició.");
    }

    public void cambiarComparadorPorCongestion() {
        boolean usandoBST = (activeTree == bst);
        bst = new BST<>((a, b) -> a.getCongestionLevel() - b.getCongestionLevel());
        avl = new AVL<>((a, b) -> a.getCongestionLevel() - b.getCongestionLevel());
        activeTree = usandoBST ? bst : avl;
        System.out.println("Comparador cambiado a nivel de congestión. Nota: el árbol se reinició.");
    }

    public void cambiarComparadorPorRiesgo() {
        boolean usandoBST = (activeTree == bst);
        bst = new BST<>((a, b) -> a.getRiskLevel() - b.getRiskLevel());
        avl = new AVL<>((a, b) -> a.getRiskLevel() - b.getRiskLevel());
        activeTree = usandoBST ? bst : avl;
        System.out.println("Comparador cambiado a nivel de riesgo. Nota: el árbol se reinició.");
    }

    public boolean insertarInterseccion(Interseccion interseccion) {
        boolean ok = activeTree.insert(interseccion);
        if (ok) indiceIntersecciones.put(interseccion.getId(), interseccion);
        return ok;
    }

    public boolean buscarInterseccion(Interseccion interseccion) {
        return activeTree.search(interseccion);
    }

    public boolean eliminarInterseccion(Interseccion interseccion) {
        boolean ok = activeTree.delete(interseccion);
        if (ok) indiceIntersecciones.remove(interseccion.getId());
        return ok;
    }

    public Interseccion obtenerInterseccion(int id) {
        return indiceIntersecciones.get(id);
    }

    public void insertarEvento(Event evento) {
        heap.insert(evento);
    }

    public Event procesarEventoMasPrioritario() {
        Event evento = heap.pop();
        if (evento != null) actualizarInterseccion(evento);
        return evento;
    }

    private void actualizarInterseccion(Event evento) {
        Interseccion inter = indiceIntersecciones.get(evento.getIntersectionId());
        if (inter == null) return;
        switch (evento.getType()) {
            case ACCIDENT ->          { inter.setRiskLevel(10);
                                        inter.setCongestionLevel(Math.min(10, inter.getCongestionLevel() + 3)); }
            case EMERGENCY_VEHICLE -> inter.setRiskLevel(Math.max(inter.getRiskLevel(), 8));
            case TRAFFIC_JAM ->       inter.setCongestionLevel(10);
            case WEATHER_HAZARD ->   { inter.setCongestionLevel(Math.min(10, inter.getCongestionLevel() + 2));
                                        inter.setRiskLevel(Math.min(10, inter.getRiskLevel() + 1)); }
        }
    }

    public Event verEventoMasPrioritario() {
        return heap.peek();
    }

    public boolean modificarPrioridad(Event oldEvento, Event newEvento) {
        return heap.updatePriority(oldEvento, newEvento);
    }

    public void ordenarEventosPorRiesgo() {
        heap.changeComparator((a, b) -> a.getRiskLevel() - b.getRiskLevel());
        System.out.println("Cola reordenada por nivel de riesgo.");
    }

    public void ordenarEventosPorTiempo() {
        heap.changeComparator((a, b) -> Long.compare(a.getReportTime(), b.getReportTime()));
        System.out.println("Cola reordenada por tiempo de reporte.");
    }

    public int contarNodosEnDistrito(String nombreDistrito) {
        return ciudad.contarNodosEnSubarbol(new district(0, nombreDistrito));
    }

    public void mostrarInOrder()    { activeTree.inOrderTraversal(); }
    public void mostrarPreOrder()   { activeTree.preOrderTraversal(); }
    public void mostrarPostOrder()  { activeTree.postOrderTraversal(); }
    public void mostrarPorNiveles() { activeTree.levelOrderTraversal(); }

    public void mostrarJerarquiaCiudad() {
        ciudad.mostrarJerarquia();
    }

    public void mostrarCiudadPorNiveles() {
        ciudad.recorridoPorNiveles();
    }

    public void mostrarEstadisticas() {
        System.out.println("\nEstadísticas");
        System.out.println("-----------------------");
        System.out.println("Altura árbol activo : " + activeTree.height());
        System.out.println("Altura BST          : " + bst.height());
        System.out.println("Altura AVL          : " + avl.height());
        System.out.println("Profundidad máx. ciudad   : " + ciudad.profundidadMaxima());
        System.out.println("Hojas en ciudad           : " + ciudad.contarHojas());
        System.out.println("Nodos internos en ciudad  : " + ciudad.contarNodosInternos());
        System.out.println("Factor ramificación ciudad: " + ciudad.factorPromedioRamificacion());
        System.out.println("Eventos en cola de prioridad: " + heap.size());
        activeTree.getMetrics();
    }

    public void cargarCiudadDesdeArchivo(String rutaArchivo) {
        if (ciudadCargada) {
            System.out.println("Ya hay una ciudad cargada. Reinicia el sistema para cargar otra.");
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            br.readLine();
            String linea;
            Map<String, cityPart> nodos = new HashMap<>();
            int idCounter = 0;
            boolean primeraLinea = true;

            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length != 2) continue;
                String padre = partes[0].trim();
                String hijo  = partes[1].trim();

                if (primeraLinea) {
                    city raiz = new city(idCounter++, padre);
                    ciudad.insertarRaiz(raiz);
                    nodos.put(padre, raiz);
                    primeraLinea = false;
                }

                cityPart padreObj = nodos.get(padre);
                if (padreObj == null) continue;

                urbanLevel nivelHijo = nextLevel(padreObj.getLevel());
                if (nivelHijo == null) continue;

                if (!nodos.containsKey(hijo)) {
                    cityPart hijoObj = crearNodo(nivelHijo, idCounter++, hijo);
                    nodos.put(hijo, hijoObj);
                    ciudad.agregarHijo(padreObj, hijoObj);
                    if (hijoObj instanceof Interseccion i) {
                        bst.insert(i);
                        avl.insert(i);
                        indiceIntersecciones.put(i.getId(), i);
                    }
                }
            }
            ciudadCargada = true;
            System.out.println("Ciudad cargada desde " + rutaArchivo);
        } catch (IOException e) {
            System.out.println("Error leyendo archivo: " + e.getMessage());
        }
    }

    private cityPart crearNodo(urbanLevel nivel, int id, String nombre) {
        return switch (nivel) {
            case CITY         -> new city(id, nombre);
            case DISTRICT     -> new district(id, nombre);
            case ZONE         -> new zone(id, nombre);
            case AVENUE       -> new avenue(id, nombre);
            case INTERSECTION -> new Interseccion(id, nombre);
        };
    }

    private urbanLevel nextLevel(urbanLevel nivel) {
        return switch (nivel) {
            case CITY     -> urbanLevel.DISTRICT;
            case DISTRICT -> urbanLevel.ZONE;
            case ZONE     -> urbanLevel.AVENUE;
            case AVENUE   -> urbanLevel.INTERSECTION;
            default       -> null;
        };
    }

    public boolean isCiudadCargada() {
        return ciudadCargada;
    }
}
