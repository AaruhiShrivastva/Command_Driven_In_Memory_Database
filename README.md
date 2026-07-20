# Command-Driven In-Memory Database (Thread-Safe, TTL Enabled)

## Overview

The **Command-Driven In-Memory Database** is a Java-based mini project that implements a generic, thread-safe key-value database that stores data in memory. The application accepts user commands through the console, supports automatic expiration of records using **Time-To-Live (TTL)**, and safely handles concurrent access from multiple threads.

The project is designed to demonstrate core Java concepts including Object-Oriented Programming, Collections Framework, Multithreading, Synchronization, Volatile Variables, Exception Handling, and Concurrent Collections.

---

# Features

* Store values using Integer keys
* Generic value support
* Command-line interface
* Optional TTL (Time-To-Live) for entries
* Lazy expiration during GET operations
* Background cleanup thread
* Thread-safe database operations
* Database START/STOP lifecycle
* Custom exception handling
* ConcurrentHashMap optimization

---

# Technologies Used

* Java 17/21
* Collections Framework
* HashMap
* ConcurrentHashMap
* Multithreading
* synchronized
* volatile
* Exception Handling
* Generics
* OOP Principles

---

# Project Objectives

* Build an in-memory database from scratch.
* Design reusable and maintainable object-oriented components.
* Understand synchronization and race conditions.
* Implement thread-safe CRUD operations.
* Implement TTL-based automatic expiration.
* Learn the difference between HashMap and ConcurrentHashMap.
* Demonstrate lifecycle management using volatile variables.

---

# Supported Commands

| Command                       | Description                 |
| ----------------------------- | --------------------------- |
| PUT <key> <value>             | Insert a value              |
| PUT <key> <value> <ttlMillis> | Insert with expiration time |
| GET <key>                     | Retrieve a value            |
| DELETE <key>                  | Delete a value              |
| STOP                          | Stop database operations    |
| START                         | Restart database            |
| EXIT                          | Exit application            |

---

# Sample Input

```text
PUT 1 Hello

PUT 2 Java

PUT 3 SpringBoot 5000

GET 1

DELETE 2

STOP

START

PUT 5 Database

GET 5

EXIT
```

---

# OOP Design

The project follows Object-Oriented Programming principles.

## Classes

### Command

Represents a parsed user command.

Responsibilities

* Stores command type
* Stores key
* Stores value
* Stores TTL

---

### CommandParser

Responsible for converting raw user input into a Command object.

Responsibilities

* Parse commands
* Validate syntax
* Validate key
* Validate TTL
* Throw parsing exceptions

---

### Entry<T>

Represents a database record.

Fields

* value
* expiryTime

Responsibilities

* Store actual value
* Store expiration timestamp

---

### InMemoryDatabase<T>

Core database implementation.

Responsibilities

* put()
* get()
* delete()
* cleanupExpiredKeys()
* start()
* stop()

---

### CleanerThread

Background daemon thread.

Responsibilities

* Remove expired entries periodically
* Sleep between cleanup cycles

---

### Custom Exceptions

* InvalidCommandException
* InvalidTTLException
* DatabaseStoppedException
* KeyNotFoundException

---

# Thread Safety Strategy

The project demonstrates two approaches.

## Phase 1–9

Uses

* HashMap
* synchronized methods

Every database operation is synchronized so that only one thread can modify or access the shared data structure at a time.

Example operations

* put()
* get()
* delete()
* cleanupExpiredKeys()

This guarantees atomic operations and prevents race conditions.

---

## Phase 10

Database storage is upgraded to

```java
ConcurrentHashMap<Integer, Entry<T>>
```

Advantages

* Better concurrency
* Multiple reads can happen simultaneously
* Reduced lock contention
* Higher scalability

TTL validation is still performed before returning values to maintain correctness.

---

# synchronized Usage

Synchronization is used to protect shared resources from concurrent modification.

Methods protected

* put()
* get()
* delete()
* cleanupExpiredKeys()

Lock used

```java
this
```

Since synchronized instance methods are used, the monitor lock belongs to the database object.

Example

```java
public synchronized void put(...)
```

Only one thread can execute a synchronized method on the same database instance at a time.

Benefits

* Prevents race conditions
* Prevents inconsistent updates
* Prevents data corruption

Trade-off

* Lower throughput under heavy contention
* GET operations may block PUT or DELETE operations while the lock is held

---

# volatile Usage

Database lifecycle is controlled using

```java
private volatile boolean running;
```

Purpose

The volatile keyword guarantees visibility of changes across all threads.

Commands

```text
STOP

START
```

modify this variable.

Without volatile

* Worker threads may continue using an outdated value.
* STOP command might not immediately stop database operations.

Volatile is sufficient because only visibility is required. Compound operations on the flag are not performed.

---

# TTL (Time-To-Live)

TTL allows entries to expire automatically.

Example

```text
PUT 10 Hello 5000
```

The entry expires after five seconds.

Each entry stores

```java
expiryTime = System.currentTimeMillis() + ttl;
```

Rules

* TTL is optional
* TTL must be greater than zero
* Expired entries behave as missing entries

---

# Lazy Expiration

Expiration is checked during GET.

Process

1. Read entry
2. Check expiry time
3. If expired

   * Remove entry
   * Return null or throw exception

Advantages

* Simple implementation
* No additional scheduling required

Limitation

Expired entries remain in memory until accessed.

---

# Background Cleanup Thread

A dedicated thread periodically removes expired entries.

Pseudo code

```java
while (running) {

    cleanupExpiredKeys();

    Thread.sleep(1000);

}
```

Benefits

* Frees memory automatically
* Removes expired data even if GET is never called

---

# Concurrency Handling

Initially the project intentionally demonstrates race conditions using multiple command executor threads.

Problems observed

* Lost updates
* Inconsistent reads
* Data corruption
* HashMap internal corruption

The solution uses synchronization and later ConcurrentHashMap to eliminate these issues.

---

# Exception Handling

The project follows a fail-fast approach.

## InvalidCommandException

Thrown when command syntax is invalid.

Example

```text
PUT A Hello
```

---

## InvalidTTLException

Thrown when

```text
TTL <= 0
```

---

## DatabaseStoppedException

Thrown when PUT or DELETE operations are attempted while the database is stopped.

---

## KeyNotFoundException

Thrown when a requested key is not present.

---

# Internal Storage

Initial implementation

```java
HashMap<Integer, Entry<T>>
```

Optimized implementation

```java
ConcurrentHashMap<Integer, Entry<T>>
```

Each entry contains

```java
value

expiryTime
```

---

# Project Workflow

1. Read command from console.
2. Parse the command.
3. Validate input.
4. Execute requested database operation.
5. Check database lifecycle.
6. Apply TTL logic.
7. Return result.
8. Continue until EXIT command.

---

# Learning Outcomes

This project demonstrates practical implementation of

* Encapsulation
* Abstraction
* Generic Classes
* Collections Framework
* HashMap
* ConcurrentHashMap
* Synchronization
* Volatile Variables
* Multithreading
* Exception Handling
* Thread Safety
* TTL Cache Design
* Command Parsing
* Background Worker Threads
* Java Memory Model basics

---

# Future Improvements

* Persistence to file
* Logging framework (SLF4J/Logback)
* LRU cache support
* Read-Write Locks
* ScheduledExecutorService for cleanup
* REST API using Spring Boot
* Unit testing with JUnit
* Benchmarking using JMH

---

# Author

**Aaruhi**

B.Tech Computer Science and Engineering

Lovely Professional University

Java | Spring Boot | Data Structures | Backend Development
