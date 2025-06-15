# FarmSure - Smart Contract Farming Platform

A modern contract farming management system built with Spring Boot that connects farmers and merchants, enabling efficient contract creation, bidding, and management.

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

## Prerequisites

- JDK 17 or later
- MySQL 8.0 or later
- Maven 3.6 or later
- IDE (recommended: VS Code with Spring Boot Extension Pack)

## Setup Instructions

1. **Clone the Repository**
   ```bash
   git clone <repository-url>
   cd farmsure
   ```

2. **Database Setup**
   ```sql
   CREATE DATABASE contract_farming;
   ```

3. **Configure Application**
   
   Update `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/contract_farming?createDatabaseIfNotExist=true
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

4. **Build and Run**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

5. **Access Application**
   - Open browser and navigate to: `http://localhost:3000`
   - Initial setup will create sample users:

   | Role     | Username      | Password    |
   |----------|---------------|-------------|
   | Farmer   | john_farmer   | farmer123   |
   | Farmer   | sarah_farmer  | farmer123   |
   | Merchant | mike_merchant | merchant123 |
   | Merchant | lisa_merchant | merchant123 |
   | Admin    | admin_user    | admin123    |

## Project Structure

```
src/
├── main/
│   ├── java/com/farmsure/
│   │   ├── config/          # Configuration classes
│   │   ├── controller/      # MVC Controllers
│   │   ├── model/          # Entity classes
│   │   ├── repository/     # Data repositories
│   │   ├── service/       # Business logic
│   │   └── FarmsureApplication.java
│   └── resources/
│       ├── db/migration/   # Flyway migrations
│       ├── static/         # CSS, JS, images
│       ├── templates/      # Thymeleaf templates
│       └── application.properties
```

## Key Features

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

## Security

- BCrypt password encryption
- Role-based access control
- Session management
- CSRF protection
- Secure form submissions

## Database Migrations

Database schema and sample data are managed through Flyway migrations:
- `V1__create_tables.sql`: Initial schema
- `V2__update_contracts_table.sql`: Contract table updates
- `V3__insert_sample_data.sql`: Sample data
- `V4__add_bid_status.sql`: Bid status tracking

## Theme Support

The application supports both light and dark themes:
- Theme toggle button in bottom-right corner
- Persistent theme preference
- Smooth transitions
- Mobile-friendly design

## Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/your-feature`)
3. Commit changes (`git commit -m 'Add your feature'`)
4. Push to branch (`git push origin feature/your-feature`)
5. Open a Pull Request

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

## License

[MIT License](LICENSE)

## Support

For support and queries, please contact through the Issues section or at 
rohanadat4852@gmail.com
