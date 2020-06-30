package naumen.com.phonebook.util.cache;

class Node<T, U> {
    Node<T, U> previous;
    Node<T, U> next;
    T key;
    U value;

    public Node(Node<T, U> previous, Node<T, U> next, T key, U value){
        this.previous = previous;
        this.next = next;
        this.key = key;
        this.value = value;
    }
}