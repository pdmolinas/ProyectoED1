package org.example;

import org.example.elementosTransito.Event;
import org.example.elementosTransito.Interseccion;
import org.example.enums.EventType;
import org.example.estructuras.AVL;
import org.example.estructuras.BST;
import org.example.estructuras.PriorityQueueMaxHeap;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Benchmark {

    private static final int[] SIZES = {1000, 10000, 50000, 100000};
    private static final int SEARCH_SAMPLES = 100;
    private static final int EVENT_COUNT = 10000;

    public static void run() {
        System.out.println("Iniciando benchmark...");

        try (FileWriter csv = new FileWriter("benchmark_results.csv")) {
            csv.write("Tamaño,Estructura,Tipo,TiempoInsercionNs,TiempoBusquedaNs,AlturaBST,AlturaAVL,Comparaciones,Rotaciones\n");

            for (int n : SIZES) {
                System.out.println("Probando con " + n + " intersecciones...");

                BST<Interseccion> bst = new BST<>((a, b) -> a.getId() - b.getId());
                AVL<Interseccion> avl = new AVL<>((a, b) -> a.getId() - b.getId());

                // Insercion ordenada (peor caso BST)
                long bstOrdenadoInsercion = medirInsercionOrdenada(bst, n);
                long avlOrdenadoInsercion = medirInsercionOrdenada(avl, n);

                int alturaBSTOrdenado = bst.height();
                int alturaAVLOrdenado = avl.height();

                bst.resetMetrics();
                avl.resetMetrics();
                long bstOrdenadoBusqueda = medirBusqueda(bst, n);
                long avlOrdenadoBusqueda = medirBusqueda(avl, n);

                csv.write(n + ",BST,Ordenada," + bstOrdenadoInsercion + "," + bstOrdenadoBusqueda + "," + alturaBSTOrdenado + "," + alturaAVLOrdenado + "," + bst.getComparisons() + ",0\n");
                csv.write(n + ",AVL,Ordenada," + avlOrdenadoInsercion + "," + avlOrdenadoBusqueda + "," + alturaBSTOrdenado + "," + alturaAVLOrdenado + "," + avl.getComparisons() + "," + avl.getRotations() + "\n");

                // Insercion aleatoria (caso promedio)
                BST<Interseccion> bstRandom = new BST<>((a, b) -> a.getId() - b.getId());
                AVL<Interseccion> avlRandom = new AVL<>((a, b) -> a.getId() - b.getId());

                long bstRandomInsercion = medirInsercionAleatoria(bstRandom, n);
                long avlRandomInsercion = medirInsercionAleatoria(avlRandom, n);

                int alturaBSTRandom = bstRandom.height();
                int alturaAVLRandom = avlRandom.height();

                bstRandom.resetMetrics();
                avlRandom.resetMetrics();
                long bstRandomBusqueda = medirBusqueda(bstRandom, n);
                long avlRandomBusqueda = medirBusqueda(avlRandom, n);

                csv.write(n + ",BST,Aleatoria," + bstRandomInsercion + "," + bstRandomBusqueda + "," + alturaBSTRandom + "," + alturaAVLRandom + "," + bstRandom.getComparisons() + ",0\n");
                csv.write(n + ",AVL,Aleatoria," + avlRandomInsercion + "," + avlRandomBusqueda + "," + alturaBSTRandom + "," + alturaAVLRandom + "," + avlRandom.getComparisons() + "," + avlRandom.getRotations() + "\n");

                // Procesamiento de eventos
                long tiempoEventos = medirEventos();
                csv.write(n + ",Heap,Eventos," + tiempoEventos + ",0,0,0,0,0\n");

                System.out.println("  BST ordenado altura: " + alturaBSTOrdenado + " | AVL ordenado altura: " + alturaAVLOrdenado);
                System.out.println("  BST aleatorio altura: " + alturaBSTRandom + " | AVL aleatorio altura: " + alturaAVLRandom);
            }

            System.out.println("\nBenchmark completado. Resultados en benchmark_results.csv");

        } catch (IOException e) {
            System.out.println("Error escribiendo CSV: " + e.getMessage());
        }
    }

    private static long medirInsercionOrdenada(org.example.interfaces.SearchTree<Interseccion> tree, int n) {
        long inicio = System.nanoTime();
        for (int i = 1; i <= n; i++) {
            tree.insert(new Interseccion(i, "Avenida" + i));
        }
        return System.nanoTime() - inicio;
    }

    private static long medirInsercionAleatoria(org.example.interfaces.SearchTree<Interseccion> tree, int n) {
        Random random = new Random(42);
        long inicio = System.nanoTime();
        for (int i = 0; i < n; i++) {
            tree.insert(new Interseccion(random.nextInt(n * 10), "Avenida" + i));
        }
        return System.nanoTime() - inicio;
    }

    private static long medirBusqueda(org.example.interfaces.SearchTree<Interseccion> tree, int n) {
        Random random = new Random(42);
        long inicio = System.nanoTime();
        for (int i = 0; i < SEARCH_SAMPLES; i++) {
            tree.search(new Interseccion(random.nextInt(n), ""));
        }
        return (System.nanoTime() - inicio) / SEARCH_SAMPLES;
    }

    private static long medirEventos() {
        PriorityQueueMaxHeap<Event> heap = new PriorityQueueMaxHeap<>((a, b) -> a.getRiskLevel() - b.getRiskLevel());
        Random random = new Random(42);

        long inicio = System.nanoTime();
        for (int i = 0; i < EVENT_COUNT; i++) {
            heap.insert(new Event("Evento" + i, EventType.ACCIDENT, i, random.nextInt(10), i));
        }
        while (!heap.isEmpty()) {
            heap.pop();
        }
        return System.nanoTime() - inicio;
    }
}