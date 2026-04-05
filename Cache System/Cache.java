import java.util.HashMap;
import java.util.Map;

public class Cache<K,V> {
    Map<K,CacheNode<K,V>> map;
    DoublyLinkedList<K, V> lruList;
    int capacity;

    public Cache(int capacity){
        this.map=new HashMap<>();
        this.lruList=new DoublyLinkedList<>();
        this.capacity=capacity;
    }
    public V get(K key){
        if(map.containsKey(key)){
            CacheNode<K,V> node=map.get(key);
            if(node.isExpired()) {
                lruList.removeNode(node);
                map.remove(key);
                return null;
            }
            lruList.removeNode(node);
            lruList.addToHead(node);
            return node.getValue();

        }
        return null;
    }
    public void set(K key, V value, long ttl){
        if(map.containsKey(key)){
            CacheNode<K,V> node=map.get(key);
            node.setValue(value);
            node.setExpireAt(System.currentTimeMillis()+ttl);
            lruList.removeNode(node);
            lruList.addToHead(node);
        }else{
            evictIfNeeded();
            CacheNode<K,V> node=new CacheNode<>(key, value,System.currentTimeMillis()+ttl);
            lruList.addToHead(node);
            map.put(key, node);
        }

    }

    public void delete(K key){
        if(map.containsKey(key)){
            CacheNode<K,V> node=map.get(key);
            lruList.removeNode(node);
            map.remove(key);
        }
    }

    public boolean exists(K key) {
        return get(key) != null;
    }

   private void evictIfNeeded() {
    if (capacity <= 0) return;

    while (map.size() >= capacity) {
        CacheNode<K, V> node = lruList.removeTail();
        if (node == null) break;

        if (node.isExpired()) {
            map.remove(node.key);
        } else {
            map.remove(node.key);
            break;
        }
    }
}
}
