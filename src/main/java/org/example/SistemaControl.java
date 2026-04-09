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
    private boolean ciudadCargada = false;

    public SistemaControl() {
        this.bst = new BST<>((a, b) -> a.getId() - b.getId());
        this.avl = new AVL<>((a, b) -> a.getId() - b.getId());
        this.activeTree = bst;
        this.heap = new PriorityQueueMaxHeap<>((a, b) -> a.getRiskLevel() - b.getRiskLevel());
        this.ciudad = new ArbolMulticamino<>(6, 10);
    }

    // ── Indexación ────────────────────────────────────────────────────────────

    public void usarBST() {
        this.activeTree = bst;
        System.out.println("Usando BST como estructura de indexación.");
    }

    public void usarAVL() {
        this.activeTree = avl;
        System.out.println("Usando AVL como estructura de indexación.");
    }

    /** §2 — cambiar criterio de orden dinámicamente (lambda) */
    public void cambiarComparadorPorId() {
        boolean usandoBST = (activeTree == bst);
        bst = new BST<>((a, b) -> a.getId() - b.getId());
        avl = new AVL<>((a, b) -> a.getId() - b.getId());
        activeTree = usandoBST ? bst : avl;
        System.out.println("Comparador cambiado a ID. Nota: el árbol se reinició (el orden BST/AVL depende del criterio de inserción).");
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
        return activeTree.insert(interseccion);
    }

    public boolean buscarInterseccion(Interseccion interseccion) {
        return activeTree.search(interseccion);
    }

    public boolean eliminarInterseccion(Interseccion interseccion) {
        return activeTree.delete(interseccion);
    }

    // ── Eventos ───────────────────────────────────────────────────────────────

    public void insertarEvento(Event evento) {
        heap.insert(evento);
    }

    public Event procesarEventoMasPrioritario() {
        return heap.pop();
    }

    public Event verEventoMasPrioritario() {
        return heap.peek();
    }

    /** §3.4 — Modificar prioridad */
    public boolean modificarPrioridad(Event oldEvento, Event newEvento) {
        return heap.updatePriority(oldEvento, newEvento);
    }

    /** §2 + §3.4 — Cambiar criterio de la cola dinámicamente (lambda) */
    public void ordenarEventosPorRiesgo() {
        heap.changeComparator((a, b) -> a.getRiskLevel() - b.getRiskLevel());
        System.out.println("Cola reordenada por nivel de riesgo.");
    }

    public void ordenarEventosPorTiempo() {
        heap.changeComparator((a, b) -> Long.compare(a.getReportTime(), b.getReportTime()));
        System.out.println("Cola reordenada por tiempo de reporte.");
    }

    // ── Ciudad (árbol N-ario) ─────────────────────────────────────────────────

    /** §3.3 — ¿Cuántas intersecciones hay en un distrito? */
    public int contarNodosEnDistrito(String nombreDistrito) {
        return ciudad.contarNodosEnSubarbol(new district(0, nombreDistrito));
    }

    public void mostrarJerarquiaCiudad() {
        ciudad.mostrarJerarquia();
    }

    public void mostrarCiudadPorNiveles() {
        ciudad.recorridoPorNiveles();
    }

    // ── Estadísticas ──────────────────────────────────────────────────────────

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

    // ── Carga desde archivo ───────────────────────────────────────────────────

    public void cargarCiudadDesdeArchivo(String rutaArchivo) {
        if (ciudadCargada) {
            System.out.println("Ya hay una ciudad cargada. Reinicia el sistema para cargar otra.");
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            br.readLine(); // saltar header
            String linea;
            Map<String, cityPart> nodos = new HashMap<>();
            int idCounter = 0;
            boolean primeraLinea = true;

            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length != 2) continue;
                String padre = partes[0].trim();
                String hijo  = partes[1].trim();

                // Primera vez: crear raíz (ciudad)
                if (primeraLinea) {
                    city raiz = new city(idCounter++, padre);
                    ciudad.insertarRaiz(raiz);
                    nodos.put(padre, raiz);
                    primeraLinea = false;
                }

                cityPart padreObj = nodos.get(padre);
                if (padreObj == null) continue;

                urbanLevel nivelHijo = nextLevel(padreObj.getLevel());
                if (nivelHijo == null) continue; // padre es nodo hoja, se ignora

                // Crear hijo tipado según el nivel del padre
                if (!nodos.containsKey(hijo)) {
                    cityPart hijoObj = crearNodo(nivelHijo, idCounter++, hijo);
                    nodos.put(hijo, hijoObj);
                    ciudad.agregarHijo(padreObj, hijoObj);
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
            default -> null; // nivel hoja, no tiene siguiente
        };
    }

    public boolean isCiudadCargada() {
        return ciudadCargada;
    }
}
