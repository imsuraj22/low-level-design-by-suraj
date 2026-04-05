public class DoublyLinkedList<K, V> {
    CacheNode<K, V> head; // Most recently used
    CacheNode<K, V> tail; // Least recently used

    public DoublyLinkedList() {
        head = null;
        tail = null;
    }

    // Add node to head (MRU)
    public void addToHead(CacheNode<K, V> node) {
        node.prev = null;
        node.next = head;
        if (head != null) {
            head.prev = node;
        }
        head = node;

        if (tail == null) { // first node
            tail = head;
        }
    }

    // Remove a given node
    public void removeNode(CacheNode<K, V> node) {
        if (node.prev != null) {
            node.prev.next = node.next;
        } else { // node is head
            head = node.next;
        }

        if (node.next != null) {
            node.next.prev = node.prev;
        } else { // node is tail
            tail = node.prev;
        }

        node.prev = null;
        node.next = null;
    }

    // Remove tail node (LRU)
    public CacheNode<K, V> removeTail() {
        if (tail == null) return null;
        CacheNode<K, V> removed = tail;
        removeNode(tail);
        return removed;
    }

    public CacheNode<K, V> getHead() {
        return head;
    }

    public void setHead(CacheNode<K, V> head) {
        this.head = head;
    }

    public CacheNode<K, V> getTail() {
        return tail;
    }

    public void setTail(CacheNode<K, V> tail) {
        this.tail = tail;
    }

    
}