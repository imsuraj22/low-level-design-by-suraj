<div align="center" >
<img width="400" height="400" alt="ChatGPT Image Jul 29, 2025, 12_42_47 AM" src="https://github.com/user-attachments/assets/ad157e59-f02c-4f1e-b706-c81880ba83c9" />
</div>




  <img src="https://www.animatedimages.org/data/media/562/animated-line-image-0184.gif" width="1920" />
</p>

<h1 align="center">💸 Splitwise Clone - Console Based App (LLD + OOP in Java)</h1>

<p align="center">
A lightweight, console-based Splitwise clone to practice and demonstrate concepts of <strong>Object-Oriented Programming (OOP)</strong> and <strong>Low-Level Design (LLD)</strong> using Java.
</p>

---

### 🧾 Features
- 👥 Add multiple users
- 💰 Create group splits for shared expenses
- 📊 Split expenses using **Equal**, **Exact**, or **Percentage** strategy
- 🔄 Track balances per user
- 🔍 View all expenses per split

---

### 📚 Learning Goals

This project helped me reinforce:
- SOLID principles & clean OOP architecture
- Java collections and inheritance
- Strategy Pattern for expense splitting
- LLD practices like modular design, separation of concerns, and scalable entity relationships

---

### 🛠️ Tech Stack
- **Language**: Java
- **Design Concepts**: OOP, Strategy Design Pattern, LLD principles
- **Data Handling**: In-memory (no external DB)

---

### 🛠️ Class Diagram

<img width="600" height="600" alt="UML class (1)" src="https://github.com/user-attachments/assets/01367351-b999-48d0-b98c-8d0dd601e482" />



---

### 🧠 Design Patterns Used
- ✅ Strategy Pattern: Dynamically switch between Equal, Exact, and Percentage logic
- ✅ Singleton Pattern : Ensure a single shared instance for each strategy (memory efficient)
- ✅ Service Pattern : Clean separation of logic for better maintainability

- 

---

### 🧩 System Design Components

- `User` – maintains user details and balance map
- `Split` – stores split metadata, list of users, and list of expenses
- `Expense` – represents individual expense (amount, paidBy, sharedBy)
- `SplitStrategy` – interface implemented by 3 strategies (Equal, Exact, Percentage)
- `SplitService` – handles business logic
- `Main` – console input & app coordination

---


### ⭐ Show some love!

If you found this project helpful, feel free to star ⭐ the repo and connect on [LinkedIn](https://www.linkedin.com/in/suraj-gaikwad-78013221b/)

---




