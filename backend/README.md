# Smart Health Assistant — Spring Boot Backend

## Stack
- Java 17
- Spring Boot 3.5
- Spring Security + JWT
- PostgreSQL
- JavaMail
- Apache POI (Excel)
- OpenPDF (hospital bills)
- Optional Twilio WhatsApp

## Roles
1. PATIENT — can register/login, use minor-symptom guidance, request appointments and see own bills.
2. HOSPITAL — can register/login and access hospital dashboard + billing.
3. ADMIN — seeded from environment properties; **cannot be registered**.

## Database
Create PostgreSQL database:
```sql
CREATE DATABASE smart_health;
```

## Run
1. Set environment variables from `application-example.properties`.
2. Make sure PostgreSQL is running.
3. Run:
```bash
mvn clean spring-boot:run
```

API base:
`http://localhost:8080/api`

The application automatically creates/updates tables using JPA.

## Default admin
Configured with:
- ADMIN_EMAIL
- ADMIN_PASSWORD

Change these before deployment.

## Main endpoints
- POST `/api/auth/register`
- POST `/api/auth/login`
- GET `/api/minor/symptoms`
- GET `/api/minor/symptoms/{id}`
- POST `/api/appointments`
- GET `/api/appointments/my`
- GET `/api/hospital/dashboard`
- PATCH `/api/appointments/{id}/status`
- GET `/api/billing/medicines?disease=...`
- POST `/api/billing`
- GET `/api/billing/my`
- GET `/api/billing/{id}/pdf`
- GET `/api/admin/dashboard`
- GET `/api/admin/export/{type}`

## Email
For Gmail, use a Gmail App Password rather than your normal password.

## WhatsApp
Twilio is optional. If credentials are empty, the bill is still emailed and the API records that WhatsApp delivery was not configured.

## Important production changes
- Use HTTPS.
- Store secrets in environment variables/secret manager.
- Replace the development JWT secret.
- Configure CORS for your production frontend.
- Add audit logging and database backups.
- Have a qualified medical professional review all medical guidance before production use.
