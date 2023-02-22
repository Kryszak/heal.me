# Project description

Create classes:
```
patient:
    name,
    surname,
    address.
```
```
doctor:
    name,
    surname,
    specialization (ie. dentist, surgeon, etc.).
```
```
visit:
    date,
    time,
    place,
    doctor,
    patient.
```

Create a REST API that will allow for:
- standard CRUD operations on patient and doctor: POST, GET, PUT, DELETE,
- operations for visit:
   - create a visit,
   - delete a visit
   - adjust time of a visit(with a validation for another visit existing at given time)
   - retrieve all visits with option to retrieve visits for single patient(passing patient's id in url)
  
- support for multi-tenant, in which every patient and doctor is assigned to different organisations

API and logic needs to be covered with tests. Classes, fields and comments needs to be written in english. Nice to have pagination and sorting.

Stack:
- Kotlin,
- Spring Boot,
- Gradle,
- database: relational (preferred PostgreSQL), nosql (prefferred MongoDB) or in-memory,
- test framework: kotest,
- mocking library: MockK
