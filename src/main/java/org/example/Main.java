package org.example;

import org.example.elementosTransito.Event;
import org.example.elementosTransito.Interseccion;
import org.example.enums.EventType;

public class Main {
    public static void main(String[] args) {
        SistemaControl sistema = new SistemaControl();

        sistema.insertarRaizCiudad("Ciudad");
        sistema.agregarElementoCiudad("Ciudad", "Distrito1");
        sistema.agregarElementoCiudad("Ciudad", "Distrito2");
        sistema.agregarElementoCiudad("Distrito1", "Zona1");
        sistema.agregarElementoCiudad("Distrito1", "Zona2");
        sistema.agregarElementoCiudad("Distrito2", "Zona3");
        sistema.agregarElementoCiudad("Zona1", "Avenida1");
        sistema.agregarElementoCiudad("Zona2", "Avenida2");
        sistema.agregarElementoCiudad("Zona3", "Avenida3");

        System.out.println("Ciudad por niveles:");
        sistema.mostrarCiudadPorNiveles();
        System.out.println("Profundidad maxima: " + sistema.profundidadMaximaCiudad());
        System.out.println("Nodos en Distrito1: " + sistema.contarNodosEnDistrito("Distrito1"));

        System.out.println("\nInsertando intersecciones en BST:");
        sistema.usarBST();
        sistema.insertarInterseccion(new Interseccion(5, "Avenida1"));
        sistema.insertarInterseccion(new Interseccion(2, "Avenida2"));
        sistema.insertarInterseccion(new Interseccion(8, "Avenida3"));
        sistema.insertarInterseccion(new Interseccion(1, "Avenida1"));
        sistema.insertarInterseccion(new Interseccion(9, "Avenida2"));

        System.out.println("InOrder BST:");
        sistema.mostrarInOrder();
        System.out.println();

        sistema.usarAVL();
        sistema.insertarInterseccion(new Interseccion(1, "Avenida1"));
        sistema.insertarInterseccion(new Interseccion(2, "Avenida2"));
        sistema.insertarInterseccion(new Interseccion(3, "Avenida3"));
        sistema.insertarInterseccion(new Interseccion(4, "Avenida1"));
        sistema.insertarInterseccion(new Interseccion(5, "Avenida2"));

        System.out.println("InOrder AVL:");
        sistema.mostrarInOrder();

        Interseccion buscar = new Interseccion(5, "Avenida1");
        System.out.println("\nBuscar ID=5 (esperando true): " + sistema.buscarInterseccion(buscar));
        System.out.println("Eliminar ID=5 (esperando true): " + sistema.eliminarInterseccion(buscar));
        System.out.println("Buscar ID=5 tras eliminar (esperando false): " + sistema.buscarInterseccion(buscar));

        sistema.insertarEvento(new Event("Accidente grave", EventType.ACCIDENT, 1, 9, 100));
        sistema.insertarEvento(new Event("Congestion alta", EventType.TRAFFIC_JAM, 2, 5, 200));
        sistema.insertarEvento(new Event("Semaforo danado", EventType.ROADWORK, 3, 3, 300));
        sistema.insertarEvento(new Event("Ambulancia", EventType.ACCIDENT, 4, 8, 150));

        System.out.println("\nEvento mas prioritario: " + sistema.verEventoMasPrioritario().getName());
        System.out.println("Procesando: " + sistema.procesarEventoMasPrioritario().getName());
        System.out.println("Siguiente: " + sistema.verEventoMasPrioritario().getName());

        sistema.ordenarEventosPorTiempo();
        System.out.println("Mas prioritario por tiempo: " + sistema.verEventoMasPrioritario().getName());

        sistema.ordenarEventosPorRiesgo();
        System.out.println("Mas prioritario por riesgo: " + sistema.verEventoMasPrioritario().getName());

        sistema.mostrarEstadisticas();

    }
}