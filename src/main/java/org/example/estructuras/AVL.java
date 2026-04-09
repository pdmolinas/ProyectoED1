package org.example.estructuras;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Comparator;


public class AVL<T> implements org.example.interfaces.SearchTree<T> {

    private final Comparator<T> comparador;
    private Node<T> root;
    private int size;
    private int comparisons;
    private int rotations;
    private boolean inserted;
    private boolean deleted;

    public AVL(Comparator<T> comparator) {
        this.comparador = comparator;
        this.root = null;
        this.size = 0;
    }

    @Override   
    public boolean insert(T value) {
        inserted = false;
        if (this.root == null) {
            this.root = new Node<>(value);
            this.size++;
            return true;
        }
        this.root = insert(this.root, value);

        return inserted;
    }

    private Node<T> insert(Node<T> root, T value) {
        if (root == null) {
            this.size++;
            inserted = true;
            return new Node<>(value);
        }
        int compare = compare(value, root.value);
        if (compare < 0)
            root.left = insert(root.left, value);
        else if (compare > 0)
            root.right = insert(root.right, value);
        else {
            return root;
        }

        root.height = 1 + Math.max(getHeight(root.left), getHeight(root.right));
        int balance = getBalance(root);

        // Left heavy (LL case)
        if (balance < -1 && compare(value, root.left.value) < 0) {
            return rotateRight(root);
        }

        // Left heavy (LR case)
        if (balance < -1 && compare(value, root.left.value) > 0) {
            root.left = rotateLeft(root.left);
            return rotateRight(root);
        }

        // Right heavy (RR case)
        if (balance > 1 && compare(value, root.right.value) > 0) {
            return rotateLeft(root);
        }

        // Right heavy (RL case)
        if (balance > 1 && compare(value, root.right.value) < 0) {
            root.right = rotateRight(root.right);
            return rotateLeft(root);
        }

        return root;
    }

    private Node<T> rotateLeft(Node<T> root) {
        if (root == null)
            return null;
        rotations++;
        if (root.right == null)
            return root;

        Node<T> newRoot = root.right;
        root.right = newRoot.left;
        newRoot.left = root;

        updateHeights(root);
        updateHeights(newRoot);
        return newRoot;

    }

    private Node<T> rotateRight(Node<T> root) {
        if (root == null)
            return null;
        rotations++;
        if (root.left == null)
            return root;

        Node<T> newRoot = root.left;
        root.left = newRoot.right;
        newRoot.right = root;

        updateHeights(root);
        updateHeights(newRoot);
        return newRoot;

    }

    private void updateHeights(Node<T> root) {
        if (root == null)
            return;
        root.height = 1 + Math.max(getHeight(root.left), getHeight(root.right));
    }

    @Override
    public boolean delete(T value) {
        deleted = false;
        if (this.root == null) {
            return deleted;
        }
        this.root = delete(this.root, value, true);
        return deleted;
    }

    private Node<T> delete(Node<T> root, T value, boolean countDeletion) {
        if (root == null) {
            return null;
        }
        int compare = compare(value, root.value);
        if (compare < 0) {
            root.left = delete(root.left, value, countDeletion);
        } else if (compare > 0) {
            root.right = delete(root.right, value, countDeletion);
        } else {
            if (countDeletion) {
                deleted = true;
                this.size--;
            }

            if (root.left == null || root.right == null) {
                return root.left != null ? root.left : root.right;
            }

            Node<T> successor = minNode(root.right);
            root.value = successor.value;
            root.right = delete(root.right, successor.value, false);

        }
        updateHeights(root);
        int balance = getBalance(root);

        // Left heavy after delete (LL case)
        if (balance < -1 && getBalance(root.left) <= 0) {
            return rotateRight(root);
        }

        // Left heavy after delete (LR case)
        if (balance < -1 && getBalance(root.left) > 0) {
            root.left = rotateLeft(root.left);
            return rotateRight(root);
        }

        // Right heavy after delete (RR case)
        if (balance > 1 && getBalance(root.right) >= 0) {
            return rotateLeft(root);
        }

        // Right heavy after delete (RL case)
        if (balance > 1 && getBalance(root.right) < 0) {
            root.right = rotateRight(root.right);
            return rotateLeft(root);
        }

        return root;
    }

    private Node<T> minNode(Node<T> root) {
        Node<T> current = root;
        while (current != null && current.left != null) {
            current = current.left;
        }
        return current;
    }

    @Override
    public boolean search(T value) {
        Node<T> current = this.root;

        while (current != null) {
            int compare = compare(value, current.value);
            if (compare == 0) {
                return true;
            }
            current = compare < 0 ? current.left : current.right;
        }

        return false;
    }

    @Override
    public void inOrderTraversal() {
        inOrderTraversal(this.root);
    }

     private void inOrderTraversal(Node<T> node) {
        if (node == null)
            return;
        inOrderTraversal(node.left);
        System.out.print(node.value + " ");
        inOrderTraversal(node.right);
    }
    @Override
    public void preOrderTraversal() {
        preOrderTraversal(this.root);
    }
    private void preOrderTraversal(Node<T> node) {
        if (node == null)
            return;
        System.out.print(node.value + " ");
        preOrderTraversal(node.left);
        preOrderTraversal(node.right);
    }
    @Override
    public void postOrderTraversal() {
        postOrderTraversal(this.root);
    }
    private void postOrderTraversal(Node<T> node) {
        if (node == null)
            return;
        postOrderTraversal(node.left);
        postOrderTraversal(node.right);
        System.out.print(node.value + " ");
    }
    @Override
    public void levelOrderTraversal() {
        if (this.root == null)
            return;
        Queue<Node<T>> queue = new LinkedList<>();
        queue.add(this.root);
        while (!queue.isEmpty()) {
            Node<T> current = queue.poll();
            System.out.print(current.value + " ");
            if (current.left != null)
                queue.add(current.left);
            if (current.right != null)
                queue.add(current.right);
        }
    }

    public int height() {
        if (this.root == null)
            return 0;
        return this.root.height;
    }

    public int size() {
        return this.size;
    }

    private int getBalance(Node<T> root) {
        if (root == null)
            return 0;
        return getHeight(root.right) - getHeight(root.left);
    }

    private int getHeight(Node<T> root) {
        if (root == null)
            return 0;
        return root.height;
    }

    @Override
    public void resetMetrics() {
        comparisons = 0;
        rotations = 0;
    }

    public int getComparisons() {
        return comparisons;
    }

    public int getRotations() {
        return rotations;
    }

    private int compare(T a, T b) {
        comparisons++;
        return comparador.compare(a, b);
    }

    public void getMetrics() {
        System.out.println("Comparisons: " + comparisons);
        System.out.println("Rotations: " + rotations);
    }
    protected static class Node<T> {
        protected T value;
        protected Node<T> left;
        protected Node<T> right;
        protected int height = 1;

        public Node(T value) {
            this.value = value;
        }
    }
}
