
PEN TASK MANAGEMENT APP

This application is based on the organisation of a project´s workflow. It will help you the creation of a new project, in which You could define every step of it, which task are meant to be done and by whom. Multiple users will be able to join the same project and They can choose their own task to do. Once the task has been assigned, the status will be changing depending on which stage you are in that moment. We will be adding AI assistance with a chat assistant using Groq

Stack: 

Language & Version: Java 21
Framework: Spring Boot x3.0.0.4
Persistance: PostgreSQL
Infraestructure: Docker
Integration: GROQ
Security: JWT 

Entities: 

User,Task and Project. We will be creating relationships between then, where a Project has a one to many relationship with Task and User, where one project can have many task and many users. At the same time, a task can have several users working in the same thing, so many to many relationship as well. 