import java.util.ArrayList;

public class NTree{
    private Node <T> root;
    private class Node<T> {
        private T value;
        private List<Node<T>> children;
        public Node(T value) {
            this.value = value;
            this.children = new ArrayList<>();
        }
    }
}