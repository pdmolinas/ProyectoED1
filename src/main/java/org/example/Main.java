package org.example;

public class Main {
    public static void main(String[] args) {
        ArbolMulticamino<String> arbol = new ArbolMulticamino<>(5, 3);

        arbol.insertarRaiz("Ciudad");

        arbol.agregarHijo("Ciudad", "Distrito1");
        arbol.agregarHijo("Ciudad", "Distrito2");
        arbol.agregarHijo("Ciudad", "Distrito3");

        arbol.agregarHijo("Distrito1", "Zona1");
        arbol.agregarHijo("Distrito1", "Zona2");
        arbol.agregarHijo("Distrito2", "Zona3");
        arbol.agregarHijo("Distrito3",  "Zona4");

        arbol.agregarHijo("Zona1", "Avenida1");
        arbol.agregarHijo("Zona1", "Avenida2");
        arbol.agregarHijo("Zona1", "Avenida3");
        arbol.agregarHijo("Zona2", "Avenida4");
        arbol.agregarHijo("Zona2", "Avenida5");
        arbol.agregarHijo("Zona3", "Avenida6");

        arbol.agregarHijo("Avenida1", "Interseccion1");
        arbol.agregarHijo("Avenida1", "Interseccion2");
        arbol.agregarHijo("Avenida2", "Interseccion3");
        arbol.agregarHijo("Avenida3", "Interseccion4");

        System.out.println("\nRecorrido por niveles:");
        arbol.recorridoPorNiveles();

        System.out.println("\n\nProfundidad maxima (esperando 5): " + arbol.profundidadMaxima());
        System.out.println("Total hojas (esperando 8): " + arbol.contarHojas());
        System.out.println("Nodos internos (esperando 10): " + arbol.contarNodosInternos());
        System.out.println("Factor promedio de ramificacion: " + arbol.factorPromedioRamificacion());
        System.out.println("\nNodos en subárbol de Distrito1 (esperando 11): " + arbol.contarNodosEnSubarbol("Distrito1"));
        System.out.println("Nodos en subárbol de Distrito2 (esperando 2): " + arbol.contarNodosEnSubarbol("Distrito2"));
        System.out.println("Nodos en subárbol inexistente (esperando 0): " + arbol.contarNodosEnSubarbol("NoExiste"));
        System.out.println("\nAgregar 4to hijo a Zona1 con maxHijos=3 (esperando false): " + arbol.agregarHijo("Zona1", "Avenida4"));
        System.out.println("Agregar hijo a Interseccion1 que supera maxHeight=5 (esperado false): " + arbol.agregarHijo("Interseccion1", "Sensor1"));

    }
}