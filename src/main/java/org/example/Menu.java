package org.example;

import org.example.elementosTransito.Event;
import org.example.elementosTransito.Interseccion;
import org.example.enums.EventType;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {

    private final SistemaControl sistema;
    private final Scanner scanner;

    public Menu() {
        this.sistema = new SistemaControl();
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n=== Menu Principal ===");
            System.out.println("1. Cargar ciudad desde archivo");
            System.out.println("2. Elegir estructura de indexacion / criterio de orden");
            System.out.println("3. Gestionar eventos de trafico");
            System.out.println("4. Mostrar estadisticas");
            System.out.println("5. Ejecutar benchmark");
            System.out.println("6. Ver jerarquia de la ciudad");
            System.out.println("7. Gestionar intersecciones (BST/AVL)");
            System.out.println("0. Salir");
            System.out.print("Opcion: ");

            try {
                opcion = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                scanner.nextLine();
                opcion = -1;
            }

            switch (opcion) {
                case 1 -> menuCargarCiudad();
                case 2 -> menuIndexacion();
                case 3 -> menuEventos();
                case 4 -> menuEstadisticas();
                case 5 -> Benchmark.run();
                case 6 -> menuCiudad();
                case 7 -> menuIntersecciones();
                case 0 -> System.out.println("Saliendo.");
                default -> System.out.println("Opcion invalida.");
            }
        }
        scanner.close();
    }

    private void menuCargarCiudad() {
        System.out.print("Ruta del archivo (Enter para usar ciudad.csv): ");
        String ruta = scanner.nextLine().trim();
        if (ruta.isEmpty()) ruta = "ciudad.csv";
        sistema.cargarCiudadDesdeArchivo(ruta);
    }

    private void menuIndexacion() {
        System.out.println("\n--- Estructura e Indexacion ---");
        System.out.println("1. Usar BST");
        System.out.println("2. Usar AVL");
        System.out.println("3. Cambiar criterio de orden: por ID");
        System.out.println("4. Cambiar criterio de orden: por nivel de congestion");
        System.out.println("5. Cambiar criterio de orden: por nivel de riesgo");
        System.out.print("Opcion: ");
        int sub = leerInt();
        switch (sub) {
            case 1 -> sistema.usarBST();
            case 2 -> sistema.usarAVL();
            case 3 -> sistema.cambiarComparadorPorId();
            case 4 -> sistema.cambiarComparadorPorCongestion();
            case 5 -> sistema.cambiarComparadorPorRiesgo();
            default -> System.out.println("Opcion invalida.");
        }
    }

    private void menuEventos() {
        if (!sistema.isCiudadCargada()) {
            System.out.println("Primero carga una ciudad (opcion 1).");
            return;
        }
        System.out.println("\n--- Gestionar Eventos ---");
        System.out.println("1. Insertar evento");
        System.out.println("2. Procesar evento mas prioritario");
        System.out.println("3. Ver siguiente evento (sin extraer)");
        System.out.println("4. Modificar prioridad de un evento");
        System.out.println("5. Reordenar cola por nivel de riesgo");
        System.out.println("6. Reordenar cola por tiempo de reporte");
        System.out.print("Opcion: ");
        int sub = leerInt();
        switch (sub) {
            case 1 -> insertarEvento();
            case 2 -> {
                Event e = sistema.procesarEventoMasPrioritario();
                if (e == null) {
                    System.out.println("No hay eventos en cola.");
                } else {
                    System.out.println("Procesando: [" + e.getType() + "] " + e.getName()
                            + " (riesgo=" + e.getRiskLevel() + ")");
                    var inter = sistema.obtenerInterseccion(e.getIntersectionId());
                    if (inter != null)
                        System.out.println("  → Interseccion " + e.getIntersectionId()
                                + " actualizada: congestion=" + inter.getCongestionLevel()
                                + ", riesgo=" + inter.getRiskLevel());
                    else
                        System.out.println("  → Interseccion " + e.getIntersectionId()
                                + " no registrada en el sistema.");
                }
            }
            case 3 -> {
                Event e = sistema.verEventoMasPrioritario();
                if (e == null) System.out.println("No hay eventos en cola.");
                else System.out.println("Siguiente: " + e.getName() + " (riesgo=" + e.getRiskLevel() + ")");
            }
            case 4 -> modificarPrioridad();
            case 5 -> sistema.ordenarEventosPorRiesgo();
            case 6 -> sistema.ordenarEventosPorTiempo();
            default -> System.out.println("Opcion invalida.");
        }
    }

    private void insertarEvento() {
        System.out.print("Nombre del evento: ");
        String nombre = scanner.nextLine();
        System.out.println("Tipo (1=ACCIDENT 2=EMERGENCY_VEHICLE 3=TRAFFIC_JAM 4=WEATHER_HAZARD): ");
        int tipoOp = leerInt();
        EventType tipo = switch (tipoOp) {
            case 2 -> EventType.EMERGENCY_VEHICLE;
            case 3 -> EventType.TRAFFIC_JAM;
            case 4 -> EventType.WEATHER_HAZARD;
            default -> EventType.ACCIDENT;
        };
        System.out.print("Nivel de riesgo (1-10): ");
        int riesgo = leerInt();
        System.out.print("ID interseccion: ");
        int id = leerInt();
        sistema.insertarEvento(new Event(nombre, tipo, id, riesgo, System.currentTimeMillis()));
        System.out.println("Evento insertado.");
    }

    private void modificarPrioridad() {
        System.out.println("-- Evento a modificar --");
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Tipo (1=ACCIDENT 2=EMERGENCY_VEHICLE 3=TRAFFIC_JAM 4=WEATHER_HAZARD): ");
        int tipoOp = leerInt();
        EventType tipo = switch (tipoOp) {
            case 2 -> EventType.EMERGENCY_VEHICLE;
            case 3 -> EventType.TRAFFIC_JAM;
            case 4 -> EventType.WEATHER_HAZARD;
            default -> EventType.ACCIDENT;
        };
        System.out.print("Riesgo actual: ");
        int riesgoViejo = leerInt();
        System.out.print("ID interseccion: ");
        int id = leerInt();
        Event viejo = new Event(nombre, tipo, id, riesgoViejo, 0);

        System.out.print("Nuevo nivel de riesgo: ");
        int riesgoNuevo = leerInt();
        Event nuevo = new Event(nombre, tipo, id, riesgoNuevo, 0);

        boolean ok = sistema.modificarPrioridad(viejo, nuevo);
        System.out.println(ok ? "Prioridad actualizada." : "Evento no encontrado en la cola.");
    }

    private void menuEstadisticas() {
        if (!sistema.isCiudadCargada()) {
            System.out.println("Primero carga una ciudad (opcion 1).");
            return;
        }
        sistema.mostrarEstadisticas();
    }

    private void menuCiudad() {
        if (!sistema.isCiudadCargada()) {
            System.out.println("Primero carga una ciudad (opcion 1).");
            return;
        }
        System.out.println("\n--- Ciudad ---");
        System.out.println("1. Ver jerarquia completa");
        System.out.println("2. Ver por niveles (BFS)");
        System.out.println("3. Contar nodos en un distrito");
        System.out.print("Opcion: ");
        int sub = leerInt();
        switch (sub) {
            case 1 -> sistema.mostrarJerarquiaCiudad();
            case 2 -> sistema.mostrarCiudadPorNiveles();
            case 3 -> {
                System.out.print("Nombre del distrito: ");
                String nombre = scanner.nextLine();
                int total = sistema.contarNodosEnDistrito(nombre);
                System.out.println("Nodos en '" + nombre + "': " + total);
            }
            default -> System.out.println("Opcion invalida.");
        }
    }

    private void menuIntersecciones() {
        System.out.println("\n--- Gestionar Intersecciones (estructura activa) ---");
        System.out.println("1. Insertar interseccion");
        System.out.println("2. Buscar interseccion por ID");
        System.out.println("3. Eliminar interseccion por ID");
        System.out.println("4. Ver recorrido InOrder");
        System.out.println("5. Ver recorrido PreOrder");
        System.out.println("6. Ver recorrido PostOrder");
        System.out.println("7. Ver recorrido por niveles (BFS)");
        System.out.print("Opcion: ");
        int sub = leerInt();
        switch (sub) {
            case 1 -> {
                System.out.print("ID: ");          int id  = leerInt();
                System.out.print("Avenida: ");     String av = scanner.nextLine();
                System.out.print("Congestion (0-10): "); int cong = leerInt();
                System.out.print("Riesgo (0-10): ");     int rsk  = leerInt();
                Interseccion inter = new Interseccion(id, av);
                inter.setCongestionLevel(cong);
                inter.setRiskLevel(rsk);
                boolean ok = sistema.insertarInterseccion(inter);
                System.out.println(ok ? "Interseccion insertada." : "Ya existe una interseccion con ese ID/criterio.");
            }
            case 2 -> {
                System.out.print("ID a buscar: ");
                int id = leerInt();
                boolean found = sistema.buscarInterseccion(new Interseccion(id, ""));
                System.out.println(found ? "Interseccion encontrada." : "No encontrada.");
            }
            case 3 -> {
                System.out.print("ID a eliminar: ");
                int id = leerInt();
                boolean del = sistema.eliminarInterseccion(new Interseccion(id, ""));
                System.out.println(del ? "Interseccion eliminada." : "No encontrada.");
            }
            case 4 -> { System.out.println("InOrder:"); sistema.mostrarInOrder(); System.out.println(); }
            case 5 -> { System.out.println("PreOrder:"); sistema.mostrarPreOrder(); System.out.println(); }
            case 6 -> { System.out.println("PostOrder:"); sistema.mostrarPostOrder(); System.out.println(); }
            case 7 -> { System.out.println("BFS:"); sistema.mostrarPorNiveles(); System.out.println(); }
            default -> System.out.println("Opcion invalida.");
        }
    }

    private int leerInt() {
        try {
            int val = scanner.nextInt();
            scanner.nextLine();
            return val;
        } catch (InputMismatchException e) {
            scanner.nextLine();
            return -1;
        }
    }
}
