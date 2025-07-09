# FarmSure - Smart Contract Farming Platform

A modern contract farming management system built with Spring Boot that connects farmers and merchants, enabling efficient contract creation, bidding, and management.

---

## Table of Contents
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Setup Instructions](#setup-instructions)
- [Database Migrations](#database-migrations)
- [Key Features by Role](#key-features-by-role)
- [Theme Support](#theme-support)
- [Security](#security)
- [Troubleshooting](#troubleshooting)
- [Contributing](#contributing)
- [License](#license)
- [Support](#support)

---

## Features
- 🔐 Role-based authentication (Farmer, Merchant)
- 📝 Contract creation and management
- 💰 Bidding system
- 💬 Messaging system
- 🌓 Dark/Light theme support
- 📊 Dashboard with statistics
- 🔍 Contract search and filtering
- 📱 Responsive design

## Tech Stack
- Java 17
- Spring Boot 3.1.0
- MySQL 8.0
- Thymeleaf
- Bootstrap 5
- Flyway Migration
- Spring Security
- Spring Data JPA
- Maven

## Project Structure
```
src/
├── main/
│   ├── java/com/farmsure/
│   │   ├── config/          # Configuration classes
│   │   ├── controller/      # MVC Controllers
│   │   │   └── InfoController.java  # Serves public info pages like privacy and terms
│   │   ├── model/           # Entity classes
│   │   ├── repository/      # Data repositories
│   │   ├── service/         # Business logic
│   │   └── FarmsureApplication.java
│   └── resources/
│       ├── db/migration/    # Flyway migrations
│       ├── static/          # CSS, JS, images
│       ├── templates/       # Thymeleaf templates including info/privacy.html and info/terms.html
│       └── application.properties
├── test/
│   └── java/com/farmsure/   # Unit and integration tests
contract_farming_schema.sql  # Optional: manual DB setup
mvnw, mvnw.cmd               # Maven wrapper scripts
pom.xml                      # Maven project file
```

## Prerequisites
- JDK 17 or later
- MySQL 8.0 or later
- Maven 3.6 or later
- IDE (recommended: VS Code with Spring Boot Extension Pack)

## Setup Instructions
1. **Clone the Repository**
   ```bash
   git clone <repository-url>
   cd FARMSURE
   ```
2. **Database Setup**
   - Ensure MySQL 8.0+ is running.
   - Create the database:
     ```sql
     CREATE DATABASE contract_farming;
     ```
   - (Optional) To manually set up the schema and sample data, run the SQL in `contract_farming_schema.sql` or let Flyway migrations handle it automatically on first app start.
3. **Configure Application**
   - Edit `src/main/resources/application.properties`:
     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/contract_farming?createDatabaseIfNotExist=true
     spring.datasource.username=your_username
     spring.datasource.password=your_password
     spring.jpa.hibernate.ddl-auto=none
     spring.flyway.enabled=true
     ```
   - Ensure Flyway is enabled for automatic schema migration.
4. **Build and Run**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
5. **Access the Application**
   - Open your browser at: [http://localhost:3000](http://localhost:3000)
   - Default users (for testing):

     | Role     | Username      | Password    |
     |----------|---------------|-------------|
     | Farmer   | john_farmer   | farmer123   |
     | Farmer   | sarah_farmer  | farmer123   |
     | Merchant | mike_merchant | merchant123 |
     | Merchant | lisa_merchant | merchant123 |
     | Admin    | admin_user    | admin123    |

6. **Public Information Pages**
   - Privacy Policy: Accessible at `/info/privacy` without login.
   - Terms of Service: Accessible at `/info/terms` without login.
   - Both pages include complete UI with header, footer, and navigation.
   - Footer includes links to Privacy Policy and Terms of Service on all pages including the register page.
6. **Troubleshooting**
   - If migrations fail, check Flyway logs and database permissions.
   - If the app won’t start, verify Java 17+, Maven, and MySQL are installed and running.
   - For port issues, ensure port 3000 is free.

## Database Migrations
Database schema and sample data are managed through Flyway migrations:
- `V1__create_tables.sql`: Initial schema
- `V2__update_contracts_table.sql`: Contract table updates
- `V3__insert_sample_data.sql`: Sample data
- `V4__add_bid_status.sql`: Bid status tracking
- `contract_farming_schema.sql`: (Optional) Full schema for manual setup

## Key Features by Role
### For Farmers
- View available contracts
- Place bids on contracts
- Track bid status
- Manage ongoing contracts
- Message merchants

### For Merchants
- Create new contracts
- Review and manage bids
- Track contract progress
- Message farmers

### For Admins
- Manage users and roles
- Oversee contracts and platform activity

## Theme Support
The application supports both light and dark themes:
- Theme toggle button in bottom-right corner
- Persistent theme preference
- Smooth transitions
- Mobile-friendly design

## Security
- BCrypt password encryption
- Role-based access control
- Session management
- CSRF protection
- Secure form submissions
- Public access allowed for `/info/**` pages including privacy and terms

## Troubleshooting
1. **Database Connection Issues**
   - Verify MySQL is running
   - Check credentials in application.properties
   - Ensure database exists
2. **Application Won't Start**
   - Check port 3000 is available
   - Verify Java version (17+)
   - Check logs in console
3. **Migration Issues**
   - Clear flyway_schema_history table
   - Verify database permissions
   - Check migration file syntax

## Contributing
1. Fork the repository
2. Create feature branch (`git checkout -b feature/your-feature`)
3. Commit changes (`git commit -m 'Add your feature'`)
4. Push to branch (`git push origin feature/your-feature`)
5. Open a Pull Request

## License
[MIT License](LICENSE)

## Support
For support and queries, please contact through the Issues section or at 
rohanadat4852@gmail.com
