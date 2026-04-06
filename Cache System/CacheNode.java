public class CacheNode<K,V>{
    K key;
    V value;
    long expireAt;

    CacheNode<K,V> prev;
    CacheNode<K,V> next;

    public CacheNode(K key, V value){
        this.key=key;
        this.value=value;
        this.expireAt=0;
    }

   

    public CacheNode(K key, V value, long ttlMillis) {
        this.key = key;
        this.value = value;
        this.expireAt = System.currentTimeMillis() + ttlMillis;
    }

     public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public long getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(long expireAt) {
        this.expireAt = expireAt;
    }

    public boolean isExpired(){
        return expireAt>0 && System.currentTimeMillis()>expireAt;
    }

}
