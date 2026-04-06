package org.example.interfaces;

public interface SearchTree <T>{

    boolean insert(T value);

    boolean search(T value);

    boolean delete(T value);

    void inOrderTraversal();

    void preOrderTraversal();

    void postOrderTraversal();

    void levelOrderTraversal();

    int height();

    void getMetrics();

    void resetMetrics();
}