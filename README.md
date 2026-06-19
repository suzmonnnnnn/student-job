# Student Item Management Portal

A complete Spring Boot web application for students to post jobs, donated items, or general items with authentication, PostgreSQL, CRUD, image upload, search, and responsive Thymeleaf UI.

## Features

- Sign up, login, logout with Spring Security
- BCrypt password encryption
- PostgreSQL database with Spring Data JPA and Hibernate
- Auto table creation using `spring.jpa.hibernate.ddl-auto=update`
- Create, read, update, delete posts
- Owner/admin-only edit and delete buttons
- Upload and display images from an `uploads` folder
- Image validation: JPG, PNG, GIF, WEBP only and max 5MB
- Search by title, description, category, and location
- Modern responsive HTML/CSS design

## Requirements

- Java 21
- PostgreSQL
- Gradle wrapper included

## Database Setup

Create a PostgreSQL database named:

```sql
CREATE DATABASE student_portal;
```

Default database settings are in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/student_portal
spring.datasource.username=postgres
spring.datasource.password=postgres
```

If your PostgreSQL password is different, update `spring.datasource.password`.

## Run the Project

Windows:

```bash
gradlew.bat bootRun
```

Mac/Linux:

```bash
./gradlew bootRun
```

Open:

```text
http://localhost:8080
```

## Folder Structure

```text
src/main/java/com/example/student
├── config
├── controller
├── dto
├── entity
├── repository
└── service

src/main/resources
├── templates
├── static/css
└── application.properties
```

## Important Pages

- `/` Home page
- `/signup` Sign up
- `/login` Login
- `/dashboard` Dashboard
- `/posts/new` Create post
- `/posts/{id}` Post detail
- `/posts/{id}/edit` Edit post
- `/search?q=keyword` Search
- `/profile` Profile

## Notes

The `uploads` folder is created automatically when the app starts. Uploaded image files are served at `/uploads/filename`.


## Database Settings Updated

This project is configured for PostgreSQL:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/student_portal
spring.datasource.username=postgres
spring.datasource.password=root1234!
```

Create the database first:

```sql
CREATE DATABASE student_portal;
```

Then run:

```bash
./gradlew bootRun
```

Login, go to **Create Job**, choose category **Job**, upload an image, and submit. You can read all posts, search by keyword, edit/delete your own posts, and images display from the `uploads` folder.
