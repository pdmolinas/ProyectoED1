package org.example;
import java.util.ArrayList;
import java.util.Comparator;

public class PriorityQueueMaxHeap<T> {

    private final ArrayList<T> heap;
    private Comparator<T> comparator;
    private int swaps;
    private int comparisons;

    public PriorityQueueMaxHeap(Comparator<T> comparator) {
        if (comparator == null) {
            throw new IllegalArgumentException("Comparator cannot be null");
        }
        this.heap = new ArrayList<>();
        this.comparator = comparator;
        this.swaps = 0;
        this.comparisons = 0;
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    public int size() {
        return heap.size();
    }

    public T peek() {
        if (isEmpty()) {
            return null;
        }
        return heap.get(0);
    }

    public void insert(T value) {
        if (value == null) {
            throw new IllegalArgumentException("Null values are not allowed");
        }

        heap.add(value);
        heapifyUp(heap.size() - 1);
    }

    public T pop() {
        if (heap.isEmpty()) {
            return null;
        }

        T max = heap.get(0);
        T last = heap.remove(heap.size() - 1);

        if (heap.isEmpty()) {
            return max;
        }

        heap.set(0, last);
        heapifyDown(0);

        return max;
    }

    public void changeComparator(Comparator<T> newComparator) {
        if (newComparator == null) {
            throw new IllegalArgumentException("Comparator cannot be null");
        }

        this.comparator = newComparator;
        rebuildHeap();
    }

    public int getSwaps() {
        return swaps;
    }

    public int getComparisons() {
        return comparisons;
    }

    public void resetMetrics() {
        swaps = 0;
        comparisons = 0;
    }

    public boolean updatePriority(T oldValue, T newValue) {
        // Buscar el elemento
        int index = -1;
        for (int i = 0; i < heap.size(); i++) {
            if (heap.get(i).equals(oldValue)) {
                index = i;
                break;
            }
        }

        if (index == -1) return false; // no existe

        heap.set(index, newValue);

        // Si la nueva prioridad es mayor, sube
        // Si es menor, baja
        if (compare(newValue, oldValue) > 0) {
            heapifyUp(index);
        } else {
            heapifyDown(index);
        }

        return true;
    }

    private void heapifyUp(int index) {
        while (index > 0 && compare(heap.get(index), heap.get(parent(index))) > 0) {
            int parent = parent(index);
            swap(index, parent);
            index = parent;
        }
    }

    private void heapifyDown(int index) {
        int size = heap.size();

        int left = leftChild(index);
        int right = rightChild(index);
        int largest = index;

        if (left < size && compare(heap.get(left), heap.get(largest)) > 0) {
            largest = left;
        }

        if (right < size && compare(heap.get(right), heap.get(largest)) > 0) {
            largest = right;
        }

        if (largest != index) {
            swap(index, largest);
            heapifyDown(largest);
        }
    }

    private int compare(T a, T b) {
        comparisons++;
        return comparator.compare(a, b);
    }

    private void swap(int i, int j) {
        swaps++;
        T temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    private int parent(int index) {
        return (index - 1) / 2;
    }

    private int leftChild(int index) {
        return 2 * index + 1;
    }

    private int rightChild(int index) {
        return 2 * index + 2;
    }

    private void rebuildHeap() {
        for (int i = parent(heap.size() - 1); i >= 0; i--) {
            heapifyDown(i);
        }
    }
}