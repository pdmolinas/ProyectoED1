import java.util.Comparator;

public class AVL<T> {

    private final Comparator<T> comparator;
    private Node<T> root;
    private int size;

    public AVL(Comparator<T> comparator) {
        this.comparator = comparator;
        this.root = null;
        this.size = 0;
    }

    public void insert(T value) {
        this.root = insert(this.root, value);
    }
    private Node<T> insert(Node<T> root, T value){
        if(root == null){
            this.size ++;
            return new Node<T>(value);
        }
        int compare = comparator.compare(value, root.value);
        if (compare < 0) root.left = insert(root.left, value);
        else if (compare > 0) root.right = insert(root.right, value);
        else{
            return root;
        }

        root.height = 1 + Math.max(getHeight(root.left), getHeight(root.right));
        int balance = getBalance(root);

        // Left heavy (LL case)
        if (balance < -1 && comparator.compare(value, root.left.value) < 0) {
            return rotateRight(root);
        }

        // Left heavy (LR case)
        if (balance < -1 && comparator.compare(value, root.left.value) > 0) {
            root.left = rotateLeft(root.left);
            return rotateRight(root);
        }

        // Right heavy (RR case)
        if (balance > 1 && comparator.compare(value, root.right.value) > 0) {
            return rotateLeft(root);
        }

        // Right heavy (RL case)
        if (balance > 1 && comparator.compare(value, root.right.value) < 0) {
            root.right = rotateRight(root.right);
            return rotateLeft(root);
        }

        return root;
    }

    private Node<T> rotateLeft(Node<T> root){
        if(root ==null) return null;
        if(root.right == null) return root;

        Node<T> newRoot = root.right;
        root.right = newRoot.left;
        newRoot.left = root;

        updateHeights(root);
        updateHeights(newRoot);
        return newRoot;


    }
    private Node<T> rotateRight (Node<T> root){
        if(root ==null) return null;
        if(root.left == null) return root;

        Node<T> newRoot = root.left;
        root.left = newRoot.right;
        newRoot.right = root;

        updateHeights(root);
        updateHeights(newRoot);
        return newRoot;

    }

    private void updateHeights (Node<T> root){
        if (root == null) return;
        root.height = 1 + Math.max(getHeight(root.left), getHeight(root.right));
    }

    public void delete(T value) {
        this.root = delete(this.root, value);
    }

    private Node<T> delete(Node<T> root, T value) {
        if (root == null) {
            return null;
        }

        int compare = comparator.compare(value, root.value);
        if (compare < 0) {
            root.left = delete(root.left, value);
        } else if (compare > 0) {
            root.right = delete(root.right, value);
        } else {
            if (root.left == null || root.right == null) {
                return root.left != null ? root.left : root.right;
            }

            Node<T> successor = minNode(root.right);
            root.value = successor.value;
            root.right = delete(root.right, successor.value);
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

    public T search(T value) {
        Node<T> current = this.root;

        while (current != null) {
            int compare = comparator.compare(value, current.value);
            if (compare == 0) {
                return current.value;
            }
            current = compare < 0 ? current.left : current.right;
        }

        return null;
    }

    public int height() {
        if(this.root == null) return 0;
        return this.root.height;
    }

    public int size() {
        return this.size;
    }

    private int getBalance(Node<T> root){
        if (root == null) return 0;
        return getHeight(root.right) - getHeight(root.left);
    }
    private int getHeight(Node<T> root){
       if (root == null) return 0;
        return root.height;
    }

    protected static class Node<T>{
        protected T value;
        protected Node<T> left;
        protected Node<T> right;
        protected int height = 1;

        public Node (T value){
            this.value = value;
            this.height = 1;
        }
            }
}
