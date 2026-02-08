GDrive API

A secure, name-based file management system built with Spring Boot.

This is a school project designed to mimic Google drive. 
It is a backend project for a cloud storage service with focus on good Architectural-Design and Human-Readable URLs, instead of UUIDs. 

🛠 Tech Stack

    Java & Spring Boot

    Spring Security & JWT (Authentication/Authorization)

    PostgreSQL (Binary storage via BYTEA)

    Hibernate & JPA (Relational mapping)

✨ Key Features

    Zero-ID Interface: Access resources via names (e.g., /download/Photos/cat.jpg) instead of cryptic IDs.

    Pro-Grade Security: Every request is validated against the authenticated currentUser. Unauthorized access is blocked at the database query level.

    Hierarchical Storage: Full support for nested folders and ownership validation.

    Optimized Persistence: High-efficiency binary handling using specialized PostgreSQL column mapping.
