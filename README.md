# 🏥 Smart Health Assistant

A full-stack healthcare management web application built with **Java Spring Boot, React, and PostgreSQL**. The application helps patients receive basic symptom guidance, request hospital appointments, and receive billing information, while hospitals can manage appointments and generate bills.

## ✨ Features

### 👤 Patient Module
- User registration and login.
- JWT-based authentication.
- Basic symptom guidance for minor health concerns.
- Submit appointment requests to hospitals for major symptoms.
- View appointment status and hospital responses.
- Receive appointment and billing notifications through email.

### 🏥 Hospital Module
- Secure hospital login and dashboard.
- View and manage patient appointment requests.
- Accept or reject appointment requests.
- Provide doctor, specialist, date, time, and appointment notes.
- Access symptom-related medicine suggestions.
- Generate patient bills with service charges and medicine costs.
- Generate PDF invoices and email bills to patients.
- Optional WhatsApp billing notifications.

### 🛡️ Admin Module
- Administrative dashboard.
- Manage and review application data.
- Export supported records to Excel.

### 🔐 Security
- Spring Security integration.
- JWT authentication.
- Role-based access for patients, hospitals, and administrators.
- Backend API protection.

## 🛠️ Tech Stack

| Component | Technologies |
|---|---|
| Frontend | React, Vite, JavaScript, CSS |
| Backend | Java, Spring Boot |
| Security | Spring Security, JWT |
| Database | PostgreSQL |
| ORM | Spring Data JPA, Hibernate |
| API Communication | REST APIs, Axios |
| Email | Spring Boot Mail |
| PDF Generation | OpenPDF |
| Excel Export | Apache POI |
| Build Tools | Maven, npm |

## 🏗️ Project Structure

```text
Smart-Health---Java-Springboot/
├── backend/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/smarthealth/
│   │       └── resources/
│   │           └── application-example.properties
│   ├── pom.xml
│   └── README.md
├── frontend/
│   ├── src/
│   ├── public/
│   ├── package.json
│   ├── vite.config.js
│   └── README.md
└── .gitignore
```

## ⚙️ Prerequisites

Install the following before running the project:

- Java JDK 17 or the version specified in `backend/pom.xml`.
- Maven, or use the Maven Wrapper if provided.
- Node.js and npm.
- PostgreSQL.
- Git.

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/Sarveshsingh2007/Smart-Health---Java-Springboot.git
cd Smart-Health---Java-Springboot
```

### 2. Configure PostgreSQL

Create a database in PostgreSQL:

```sql
CREATE DATABASE smart_health;
```

Configure the database URL, username, and password using environment variables or your local Spring Boot configuration.

Example database URL:

```text
jdbc:postgresql://localhost:5432/smart_health
```

### 3. Configure the Backend

Navigate to the backend directory:

```bash
cd backend
```

Configure the required database, JWT, email, and other settings using your local configuration. Refer to the project's example configuration if available.

**Important:** Never commit real database passwords, email app passwords, JWT secrets, or API credentials to GitHub.

Run the Spring Boot application using Maven:

```bash
mvn spring-boot:run
```

The backend will use the port configured in your application settings. Spring Boot's default is `8080` if no other port is configured.

### 4. Configure the Frontend

Open another terminal from the repository root:

```bash
cd frontend
npm install
```

Configure the backend API URL using the frontend environment configuration supported by the project.

Start the development server:

```bash
npm run dev
```

Open the local URL displayed in the terminal by Vite, commonly `http://localhost:5173`.

## 🔄 Application Workflow

1. A patient registers or logs in.
2. The patient selects symptoms and receives basic guidance for minor concerns.
3. For symptoms requiring hospital consultation, the patient submits an appointment request.
4. The hospital reviews the request and accepts or rejects it.
5. Appointment details and notifications are sent where configured.
6. The hospital prepares a bill containing service charges and applicable medicine costs.
7. The application generates a PDF bill and emails it to the patient when email configuration is available.

## 📄 Billing and Reports

- Appointment-linked billing.
- Medicine selection and pricing.
- PDF invoice generation.
- Email delivery of invoices.
- Optional WhatsApp notifications.
- Excel export for supported administrative records.

## 🔒 Configuration and Privacy

- Keep `application.properties` and environment files containing real credentials out of version control.
- Use environment variables for passwords, JWT secrets, and API keys.
- Share only sanitized example configuration files.
- Do not use symptom guidance as a replacement for professional medical diagnosis or emergency care.

## 🎯 Project Objective

The objective of Smart Health Assistant is to simplify basic healthcare interactions by combining symptom guidance, hospital appointment management, secure access, digital billing, and automated notifications in one web application.

## 🔮 Future Enhancements

- Online appointment availability and scheduling.
- Patient medical history and appointment tracking.
- Improved hospital and doctor search.
- More detailed analytics and reporting.
- Cloud deployment and automated testing.
- Enhanced accessibility and mobile responsiveness.

## 👨‍💻 Author

**Sarvesh Singh**

GitHub: [@Sarveshsingh2007](https://github.com/Sarveshsingh2007)

---

*Smart Health Assistant — simplifying healthcare management through technology.*
