# Notification Service - Low Level Design (LLD)

## 📌 Overview

This project is a **Notification Service** built for **low-level design interview preparation**.

It supports:
- **Multi-channel notifications**
- **Extensible sender architecture**
- **Notification status tracking**
- **Retry-ready design**
- **Scalable async architecture discussion**

---

## 🚀 Features

- **Email Notifications**
- **SMS Notifications**
- **Push Notifications**
- **In-App Notifications**
- **Multi-channel support**
- **Factory + Strategy Pattern**
- **In-memory repository**
- **Extensible design**

---

## ⚙️ Supported Operations

| Method | Description |
|--------|------------|
| `send(NotificationRequest request)` | Sends notification immediately |
| `schedule(NotificationRequest request)` | Schedules notification for future |
| `getStatus(String id)` | Returns notification status |

---

## 🏗️ Design

### 🔹 Core Components

---

### 1. NotificationService

Main orchestration layer.

Responsibilities:
- Validate request
- Create notifications
- Select sender
- Update status
- Coordinate flow

---

### 2. NotificationSender (Strategy Pattern)

Common interface for all notification channels.

```java
boolean send(Notification notification);
```

Implementations:

```
EmailSender
SmsSender
PushSender
InAppSender
```

---

### 3. SenderFactory (Factory Pattern)

Returns sender implementation based on channel type.

```java
senderFactory.getSender(ChannelType.EMAIL);
```

This makes the system extensible.

---

### 4. NotificationRepository

Handles persistence operations.

Current implementation:

```
InMemoryNotificationRepository
```

Uses:

```
ConcurrentHashMap
In-memory storage
```

---

### 5. Notification

Represents the notification entity.

Contains:

```
Notification ID
User ID
Channel Type
Subject
Body
Status
Retry Count
Timestamps
```

---

## 🧩 How It Works

### 🔹 Send Notification Flow

```
1. Caller creates NotificationRequest
2. NotificationServiceImpl receives request
3. Notification objects created per channel
4. Factory returns sender implementation
5. Sender sends notification
6. Repository updates final status
```

### 🔹 Multi-Channel Processing

Example:

```
EMAIL
SMS
PUSH
IN_APP
```

Each channel uses its own sender implementation.

### 🔹 Retry Handling

If notification fails:

```
Status becomes FAILED
Retry count increases
Retry worker can process later
```

Current design supports:

```
retryCount tracking
failed notification lookup
```

### 🔹 In-App Notification Handling

Unlike Email/SMS:

```
In-App notifications are generally stored internally
Frontend/mobile app fetches them later
```

---

## 🧠 Design Patterns Used

| Pattern | Usage |
|---------|-------|
| Strategy Pattern | Different sender implementations |
| Factory Pattern | Sender creation |
| Repository Pattern | Persistence abstraction |

---

## 🧵 Thread Safety

Current repository can use:

```java
ConcurrentHashMap
```

to support concurrent operations safely.

Production systems may additionally use:

```
DB transactions
Optimistic locking
Distributed locks
```

---

## 📈 Scaling Design

### 🔹 Current Flow

```
Caller Service
    ->
NotificationService
    ->
Sender
```

### 🔹 Scalable Async Flow

```
Caller Service
    ->
NotificationService
    ->
Queue
    ->
Worker
    ->
Sender
```

Benefits:

```
Fast API response
Horizontal scaling
Retry support
Fault isolation
```

---

## 🔄 Sync vs Async

### 🔹 Synchronous

Caller waits until notification is sent.

Useful for:

```
OTP
Critical alerts
```

### 🔹 Asynchronous

Notification pushed to queue and processed later.

Useful for:

```
Bulk notifications
Push notifications
Marketing campaigns
```

---

## 📬 How to Add WhatsApp Support

**Step 1** — Add new enum:

```java
WHATSAPP
```

**Step 2** — Create:

```java
WhatsAppSender implements NotificationSender
```

**Step 3** — Register inside:

```java
SenderFactoryImpl
```

> No changes required in orchestration logic.

---

## 🛠️ Future Improvements

```
Kafka/RabbitMQ integration
Dead Letter Queue (DLQ)
Scheduled workers
Template engine
User notification preferences
Rate limiting
Provider fallback
Metrics & monitoring
```

---

## 📂 Project Structure

```
src/
│
├── enums/
│   ├── ChannelType.java
│   ├── NotificationStatus.java
│   └── NotificationType.java
│
├── model/
│   ├── Notification.java
│   └── NotificationRequest.java
│
├── repository/
│   ├── NotificationRepository.java
│   └── InMemoryNotificationRepository.java
│
├── sender/
│   ├── NotificationSender.java
│   ├── EmailSender.java
│   ├── SmsSender.java
│   ├── PushSender.java
│   └── InAppSender.java
│
├── factory/
│   ├── SenderFactory.java
│   └── SenderFactoryImpl.java
│
├── service/
│   ├── NotificationService.java
│   └── NotificationServiceImpl.java
│
└── Main.java
```

---

## ▶️ Sample Output

```
Sending Email to User 101 : Your order has been placed successfully.

Sending SMS to User 101 : Your order has been placed successfully.

Storing In-App Notification for User 101 : Your order has been placed successfully.

Final Status : SENT
```

---

## 🎯 Key LLD Concepts Covered

```
SOLID Principles
Extensible Architecture
Loose Coupling
Separation of Concerns
Async Processing
Scalability
Fault Tolerance
```

---

## 👨‍💻 Author

Suraj Gaikwad
