package org.example;

import org.example.elementosTransito.Event;
import org.example.enums.EventType;

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
            System.out.println("\nMenu Principal");
            System.out.println("______________");
            System.out.println("1. Cargar ciudad desde archivo");
            System.out.println("2. Elegir estructura de indexacion");
            System.out.println("3. Insertar y procesar eventos");
            System.out.println("4. Mostrar estadisticas");
            System.out.println("5. Ejecutar benchmark");
            System.out.println("0. Salir");
            System.out.print("Opcion: ");

            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1 -> {
                    System.out.print("Ruta del archivo (Enter para usar ciudad.csv): ");
                    String ruta = scanner.nextLine().trim();
                    if (ruta.isEmpty()) ruta = "ciudad.csv";
                    sistema.cargarCiudadDesdeArchivo(ruta);
                }
                case 2 -> {
                    System.out.println("1. BST  2. AVL");
                    System.out.print("Opcion: ");
                    int sub = scanner.nextInt();
                    scanner.nextLine();
                    if (sub == 1) sistema.usarBST();
                    else if (sub == 2) sistema.usarAVL();
                    else System.out.println("Opcion invalida.");
                }
                case 3 -> {
                    if (!sistema.isCiudadCargada()) {
                        System.out.println("Primero se debe cargar una ciudad.");
                    } else {
                        System.out.println("1. Insertar evento  2. Procesar evento mas prioritario  3. Ver siguiente evento");
                        System.out.print("Opcion: ");
                        int sub = scanner.nextInt();
                        scanner.nextLine();
                        switch (sub) {
                            case 1 -> {
                                System.out.print("Nombre: ");
                                String nombre = scanner.nextLine();
                                System.out.print("Nivel de riesgo (1-10): ");
                                int riesgo = scanner.nextInt();
                                System.out.print("ID interseccion: ");
                                int id = scanner.nextInt();
                                scanner.nextLine();
                                sistema.insertarEvento(new Event(nombre, EventType.ACCIDENT, id, riesgo, (int) System.currentTimeMillis()));
                                System.out.println("Evento insertado.");
                            }
                            case 2 -> {
                                Event evento = sistema.procesarEventoMasPrioritario();
                                if (evento == null) System.out.println("No hay eventos.");
                                else System.out.println("Procesando: " + evento.getName() + " (riesgo=" + evento.getRiskLevel() + ")");
                            }
                            case 3 -> {
                                Event evento = sistema.verEventoMasPrioritario();
                                if (evento == null) System.out.println("No hay eventos.");
                                else System.out.println("Siguiente: " + evento.getName() + " (riesgo=" + evento.getRiskLevel() + ")");
                            }
                            default -> System.out.println("Opcion invalida.");
                        }
                    }
                }
                case 4 -> {
                    if (!sistema.isCiudadCargada()) {
                        System.out.println("Primero se debe cargar una ciudad.");
                    } else {
                        sistema.mostrarEstadisticas();
                    }
                }
                case 5 -> Benchmark.run();
                case 0 -> System.out.println("Saliendo.");
                default -> System.out.println("Opcion invalida.");
            }
        }
        scanner.close();
    }
}