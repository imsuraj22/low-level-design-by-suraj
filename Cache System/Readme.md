# 🧠 Cache System Design (Redis-like)


<p align="center">
  <img width="500" height="800" alt="Cache System Design"
  src="https://github.com/user-attachments/assets/e16140ef-9f17-4cd3-9f7d-435c67d967e2" />
</p>

## 📌 Overview
This project is an **in-memory cache system** inspired by Redis, built for **low-level design interview preparation**.

It supports:
- **LRU (Least Recently Used) eviction**
- **TTL (Time-To-Live) expiration**
- **O(1) operations**

---

## 🚀 Features

- **Generic Key-Value storage (`<K, V>`)**
- **LRU eviction policy**
- **TTL-based expiration (lazy cleanup)**
- **O(1) time complexity for core operations**
- **Clean and modular design**

---

## ⚙️ Supported Operations

| Method | Description |
|--------|------------|
| `get(K key)` | Returns value if present and not expired |
| `set(K key, V value, long ttl)` | Inserts/updates value with TTL |
| `delete(K key)` | Removes key from cache |
| `exists(K key)` | Checks if key is valid and not expired |

---

## 🏗️ Design

### 🔹 Core Components

#### 1. Cache
- Main class handling all operations
- Uses:
  - `HashMap<K, CacheNode<K,V>>` → O(1) lookup
  - `DoublyLinkedList<K,V>` → maintains LRU order

---

#### 2. CacheNode
Represents each entry:
- **Key**
- **Value**
- **Expiry timestamp (`expireAt`)**
- **Pointers (`prev`, `next`)**

---

#### 3. DoublyLinkedList
Maintains access order:
- **Head → Most Recently Used (MRU)**
- **Tail → Least Recently Used (LRU)**

Operations:
- Add to head
- Remove node
- Remove tail

---

## 🧩 How It Works

### 🔹 Get Operation
1. Lookup key in map  
2. If expired → remove and return null  
3. Move node to head  
4. Return value  

---

### 🔹 Set Operation
1. If key exists:
   - Update value and TTL  
   - Move node to head  
2. If new key:
   - Evict if needed  
   - Insert at head  

---

### 🔹 Eviction Strategy

When cache reaches capacity:
1. Check LRU node (tail)  
2. If expired → remove it  
3. Else → evict LRU node  

---

### 🔹 TTL Handling
- Stored as `expireAt = currentTime + ttl`
- Checked during `get()` and `exists()`
- Uses **lazy expiration** (no background thread)

---

## ⏱️ Time Complexity

| Operation | Complexity |
|----------|-----------|
| `get` | O(1) |
| `set` | O(1) (amortized) |
| `delete` | O(1) |
| eviction | O(1) |

