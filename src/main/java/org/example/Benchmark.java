package org.example;

import org.example.elementosTransito.Event;
import org.example.elementosTransito.Interseccion;
import org.example.enums.EventType;
import org.example.estructuras.AVL;
import org.example.estructuras.BST;
import org.example.estructuras.PriorityQueueMaxHeap;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Benchmark {

    private static final int[] SIZES = {1000, 10000, 50000, 100000};
    private static final int SEARCH_SAMPLES = 100;
    private static final int EVENT_COUNT = 10000;

    public static void run() {
        System.out.println("Iniciando benchmark...");

        try (FileWriter csv = new FileWriter("benchmark_results.csv")) {
            csv.write("Tamaño,Estructura,Tipo,TiempoInsercionPromNs,TiempoBusquedaPromNs,TiempoExtraccionNs," +
                      "AlturaBST,AlturaAVL,BalanceFactorRaizAVL,Comparaciones,Rotaciones\n");

            for (int n : SIZES) {
                System.out.println("Probando con " + n + " intersecciones...");

                BST<Interseccion> bst = new BST<>((a, b) -> a.getId() - b.getId());
                AVL<Interseccion> avl = new AVL<>((a, b) -> a.getId() - b.getId());

                long bstOrdenadoIns = medirInsercionOrdenada(bst, n);
                long avlOrdenadoIns = medirInsercionOrdenada(avl, n);

                int alturaBSTOrd = bst.height();
                int alturaAVLOrd = avl.height();
                int balanceAVLOrd = avl.getRootBalanceFactor();

                bst.resetMetrics(); avl.resetMetrics();
                long bstOrdenadoBus = medirBusqueda(bst, n);
                long avlOrdenadoBus = medirBusqueda(avl, n);

                csv.write(n + ",BST,Ordenada," + (bstOrdenadoIns / n) + "," + bstOrdenadoBus + ",0,"
                        + alturaBSTOrd + "," + alturaAVLOrd + ",0," + bst.getComparisons() + ",0\n");
                csv.write(n + ",AVL,Ordenada," + (avlOrdenadoIns / n) + "," + avlOrdenadoBus + ",0,"
                        + alturaBSTOrd + "," + alturaAVLOrd + "," + balanceAVLOrd + ","
                        + avl.getComparisons() + "," + avl.getRotations() + "\n");

                BST<Interseccion> bstR = new BST<>((a, b) -> a.getId() - b.getId());
                AVL<Interseccion> avlR = new AVL<>((a, b) -> a.getId() - b.getId());

                long bstRandomIns = medirInsercionAleatoria(bstR, n);
                long avlRandomIns = medirInsercionAleatoria(avlR, n);

                int alturaBSTRnd = bstR.height();
                int alturaAVLRnd = avlR.height();
                int balanceAVLRnd = avlR.getRootBalanceFactor();

                bstR.resetMetrics(); avlR.resetMetrics();
                long bstRandomBus = medirBusqueda(bstR, n);
                long avlRandomBus = medirBusqueda(avlR, n);

                csv.write(n + ",BST,Aleatoria," + (bstRandomIns / n) + "," + bstRandomBus + ",0,"
                        + alturaBSTRnd + "," + alturaAVLRnd + ",0," + bstR.getComparisons() + ",0\n");
                csv.write(n + ",AVL,Aleatoria," + (avlRandomIns / n) + "," + avlRandomBus + ",0,"
                        + alturaBSTRnd + "," + alturaAVLRnd + "," + balanceAVLRnd + ","
                        + avlR.getComparisons() + "," + avlR.getRotations() + "\n");

                long[] heap = medirEventosHeap();
                long heapIns     = heap[0];
                long heapExtract = heap[1];

                long[] lista = medirEventosLista();
                long listaIns     = lista[0];
                long listaExtract = lista[1];

                csv.write(n + ",Heap,Eventos," + heapIns + ",0," + heapExtract + ",0,0,0,0,0\n");
                csv.write(n + ",ListaOrdenada,Eventos," + listaIns + ",0," + listaExtract + ",0,0,0,0,0\n");

                System.out.printf("  [Ordenada]  BST altura=%d | AVL altura=%d | AVL balance=%d%n",
                        alturaBSTOrd, alturaAVLOrd, balanceAVLOrd);
                System.out.printf("              BST ins prom=%,d ns/elem | AVL ins prom=%,d ns/elem%n",
                        bstOrdenadoIns / n, avlOrdenadoIns / n);
                System.out.printf("  [Aleatoria] BST altura=%d | AVL altura=%d%n",
                        alturaBSTRnd, alturaAVLRnd);
                System.out.printf("              BST ins prom=%,d ns/elem | AVL ins prom=%,d ns/elem%n",
                        bstRandomIns / n, avlRandomIns / n);
                System.out.printf("  [Heap  ] ins=%,d ns/evento | extraccion=%,d ns total (%d eventos)%n",
                        heapIns / EVENT_COUNT, heapExtract, EVENT_COUNT);
                System.out.printf("  [Lista ] ins=%,d ns/evento | extraccion=%,d ns total%n",
                        listaIns / EVENT_COUNT, listaExtract);
                System.out.printf("  Heap es %.1fx mas rapido en insercion que lista ordenada%n",
                        (double) listaIns / Math.max(heapIns, 1));
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

    private static long[] medirEventosHeap() {
        PriorityQueueMaxHeap<Event> heap =
                new PriorityQueueMaxHeap<>((a, b) -> a.getRiskLevel() - b.getRiskLevel());
        Random random = new Random(42);
        EventType[] tipos = EventType.values();

        long inicioIns = System.nanoTime();
        for (int i = 0; i < EVENT_COUNT; i++) {
            heap.insert(new Event("Evento" + i, tipos[random.nextInt(tipos.length)], i, random.nextInt(10) + 1, i));
        }
        long tiempoIns = System.nanoTime() - inicioIns;

        long inicioExt = System.nanoTime();
        while (!heap.isEmpty()) {
            heap.pop();
        }
        long tiempoExt = System.nanoTime() - inicioExt;

        return new long[]{tiempoIns, tiempoExt};
    }

    private static long[] medirEventosLista() {
        ArrayList<Event> lista = new ArrayList<>();
        Random random = new Random(42);
        EventType[] tipos = EventType.values();

        long inicioIns = System.nanoTime();
        for (int i = 0; i < EVENT_COUNT; i++) {
            Event e = new Event("Evento" + i, tipos[random.nextInt(tipos.length)], i, random.nextInt(10) + 1, i);
            int pos = Collections.binarySearch(lista, e, (a, b) -> a.getRiskLevel() - b.getRiskLevel());
            if (pos < 0) pos = -(pos + 1);
            lista.add(pos, e);
        }
        long tiempoIns = System.nanoTime() - inicioIns;

        long inicioExt = System.nanoTime();
        while (!lista.isEmpty()) {
            lista.remove(lista.size() - 1);
        }
        long tiempoExt = System.nanoTime() - inicioExt;

        return new long[]{tiempoIns, tiempoExt};
    }
}
