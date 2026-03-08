# GDrive API
A secure, name-based file management system built with Spring Boot.

This is a school project designed to mimic Google Drive. It is a backend project 
for a cloud storage service with focus on good architectural design, REST principles, 
and human-readable URLs instead of UUIDs.

## 🛠 Tech Stack

- Java & Spring Boot
- Spring Security, JWT & OAuth2 (Authentication/Authorization)
- Spring HATEOAS (Hypermedia-driven REST)
- PostgreSQL (Binary storage via BYTEA)
- Hibernate & JPA (Relational mapping)

## ✨ Key Features

**Zero-ID Interface:** Access resources via names (e.g., `/download/Photos/cat.jpg`) 
instead of cryptic IDs.

**Hypermedia-Driven API:** Responses include HATEOAS links, allowing clients to 
navigate the API without hardcoding URLs.

**GitHub Authentication:** Log in via GitHub using OpenID Connect. New accounts are 
created automatically on first login.

**Pro-Grade Security:** Every request is validated against the authenticated user. 
Unauthorized access is blocked at the database query level.

**Hierarchical Storage:** Full support for nested folders with ownership validation.
