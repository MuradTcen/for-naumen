package naumen.com.phonebook.util.cache;

import java.util.HashMap;

public class LruCache<K, V>{

    private HashMap<K, Node<K, V>> cache;
    private Node<K, V> leastRecentlyUsed;
    private Node<K, V> mostRecentlyUsed;
    private int maxSize;
    private int currentSize;

    public LruCache(int maxSize){
        this.maxSize = maxSize;
        this.currentSize = 0;
        leastRecentlyUsed = new Node<K, V>(null, null, null, null);
        mostRecentlyUsed = leastRecentlyUsed;
        cache = new HashMap<K, Node<K, V>>();
    }

    public V get(K key){
        Node<K, V> tempNode = cache.get(key);
        if (tempNode == null){
            return null;
        }
        else if (tempNode.key == mostRecentlyUsed.key){
            return mostRecentlyUsed.value;
        }

        Node<K, V> nextNode = tempNode.next;
        Node<K, V> previousNode = tempNode.previous;

        if (tempNode.key == leastRecentlyUsed.key){
            nextNode.previous = null;
            leastRecentlyUsed = nextNode;
        }

        else if (tempNode.key != mostRecentlyUsed.key){
            previousNode.next = nextNode;
            nextNode.previous = previousNode;
        }

        tempNode.previous = mostRecentlyUsed;
        mostRecentlyUsed.next = tempNode;
        mostRecentlyUsed = tempNode;
        mostRecentlyUsed.next = null;

        return tempNode.value;

    }

    public void put(K key, V value){
        if (cache.containsKey(key)){
            return;
        }

        Node<K, V> myNode = new Node<K, V>(mostRecentlyUsed, null, key, value);
        mostRecentlyUsed.next = myNode;
        cache.put(key, myNode);
        mostRecentlyUsed = myNode;

        if (currentSize == maxSize){
            cache.remove(leastRecentlyUsed.key);
            leastRecentlyUsed = leastRecentlyUsed.next;
            leastRecentlyUsed.previous = null;
        }

        else if (currentSize < maxSize){
            if (currentSize == 0){
                leastRecentlyUsed = myNode;
            }
            currentSize++;
        }
    }
}