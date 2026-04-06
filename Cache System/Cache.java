import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class Cache<K,V> {
    Map<K,CacheNode<K,V>> map;
    DoublyLinkedList<K, V> lruList;
    int capacity;

    ReentrantLock lock = new ReentrantLock();

    public Cache(int capacity){
        this.map=new HashMap<>();
        this.lruList=new DoublyLinkedList<>();
        this.capacity=capacity;
    }
    public V get(K key){
        try{
            lock.lock();
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
        }finally{
            lock.unlock();
        }
        return null;
    }
    public void set(K key, V value, long ttl){
       try{
        lock.lock();
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
       }finally{
        lock.unlock();
       }

    }

    public void delete(K key){
       try{
        lock.lock();
         if(map.containsKey(key)){
            CacheNode<K,V> node=map.get(key);
            lruList.removeNode(node);
            map.remove(key);
        }
       }finally{
        lock.unlock();
       }
    }

    public boolean exists(K key) {
        try{
            lock.lock();
            return get(key) != null;
        }finally{
            lock.unlock();
        }
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
