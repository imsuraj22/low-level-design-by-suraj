
<div align="center">
<img width="400" height="400" alt="url-shortener-logo" src="https://github.com/user-attachments/assets/e7c93085-fe58-4643-b169-11a3c5c3348d" />
</div>

<img src="https://www.animatedimages.org/data/media/562/animated-line-image-0184.gif" width="1920" />

<h1 align="center">🔗 URL Shortener - Console Based App (LLD + OOP in Java)</h1>

A lightweight, console-based <strong>URL Shortener</strong> clone to practice and demonstrate concepts of <strong>Object-Oriented Programming (OOP)</strong>, <strong>System Design</strong>, and <strong>Concurrency Safety</strong> using Java.
</p>

---

### 🧾 Features
- 👤 User management (create & track shortened URLs per user)
- 🔗 Shorten long URLs into unique, compact links
- ✏️ Support for **custom aliases** and **auto-generated keys**
- 🛡 Prevent collisions & duplicates
- 📅 Optional expiry date for each short link
- 📋 Retrieve all URLs created by a user

---

### 📚 Learning Goals
This project helped reinforce:
- Applying OOP principles in real-world services
- Ensuring thread safety in concurrent environments
- Designing extensible and modular systems
- Using **Base62 encoding** for efficient short key generation
- Handling uniqueness constraints for aliases and keys

---

### 🛠️ Tech Stack
- **Language**: Java  
- **Design Concepts**: OOP, Strategy Design Pattern, Thread Safety, LLD principles  
- **Data Handling**: In-memory storage (can be extended to DBs like MySQL/Redis)  

---

### 🛠️ Class Diagram
<img width="600" height="600" alt="url-shortener-uml" src="https://github.com/user-attachments/assets/4993b2f4-807f-4895-a335-da33a0a22b72" />

---

### 🧠 Design Patterns Used
- ✅ **Singleton Pattern** – Manage single instance of key generator & services  
- ✅ **Service Pattern** – Separate concerns (UserService, URLService, KeyGenerator)  
- ✅ **Strategy Pattern** – Plug different key generation algorithms if needed (e.g., Base62, Hash-based)  

---

### 🧩 System Design Components
- `User` – stores user details and associated short URLs  
- `URL` – represents original link, shortened key, and metadata (expiry, owner)  
- `KeyGenerator` – generates unique Base62 short keys  
- `URLService` – handles creation, retrieval, and alias validation  
- `UserService` – manages users and their URL associations  
- `Main` – console input/output to interact with the system  

---

### ⚙️ Core Functionalities
- **Custom Alias** → Users can create their own short keys (`/myblog`)  
- **Auto Key Generation** → Unique Base62 6-char random key  
- **Expiry Support** → URLs can expire after a given date  
- **Duplicate Handling** → Return existing URL instead of recreating  
- **Thread Safety** → Safe alias assignment & key generation without blocking unrelated requests  

---

### 🧩 Key Design Decisions
1. **Preventing Duplicate Aliases**  
   - Alias registry ensures uniqueness using atomic checks  
   - Fast rejection if alias exists, without blocking other requests  

2. **Collision-Free Auto Keys**  
   - Base62 encoding ensures 57B unique possibilities with 6 chars  
   - Keys loop until uniqueness is confirmed  

3. **User-Aware Behavior**  
   - Duplicate requests by the same user return existing short URL  

---

### 🚀 Extensibility
This project can be extended to include:
- 📊 Analytics (click counts, geolocation tracking)  
- 🔒 Authentication & Authorization  
- 🗄 Persistent storage (MySQL, Redis)  
- ⏱ Rate limiting to prevent abuse  

---

### ⭐ Show Some Love!
If you found this project helpful:  
- ⭐ Star the repo  
- 🧑‍💻 Fork & enhance it  
- 📤 Share with friends learning LLD & OOP  

---

