# Energy Buddy - Energy Monitoring Web Application

A full-stack web application for monitoring and managing energy consumption of household appliances.

## Features

- **User Authentication**: Secure registration and login with JWT tokens
- **Appliance Management**: Register and manage appliances with power ratings
- **Energy Logging**: Log weekly energy consumption for each appliance
- **Data Visualization**: View energy trends through interactive charts (using Recharts)
- **AI Suggestions**: Receive intelligent energy-saving recommendations
- **Push Notifications**: Get alerts for high consumption appliances
- **Energy Calculations**: Automatic calculations using P × t = E formula (Watts to Wh to kWh)

## Tech Stack

### Backend
- **Spring Boot 3.2.0**: RESTful API framework
- **Spring Security**: Authentication and authorization
- **JWT**: Token-based authentication
- **JPA/Hibernate**: Database ORM
- **H2 Database**: In-memory database (can be switched to PostgreSQL)
- **Maven**: Dependency management

### Frontend
- **React 18**: UI framework
- **React Router**: Client-side routing
- **Recharts**: Data visualization library
- **Axios**: HTTP client
- **Context API**: State management for authentication

## Project Structure

```
EnergyBuddy/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/energybuddy/
│   │   │   │   ├── config/          # Security configuration
│   │   │   │   ├── controller/      # REST API controllers
│   │   │   │   ├── dto/             # Data Transfer Objects
│   │   │   │   ├── model/           # JPA entities
│   │   │   │   ├── repository/      # JPA repositories
│   │   │   │   ├── security/        # JWT filter and user details
│   │   │   │   ├── service/         # Business logic
│   │   │   │   └── util/            # Utility classes
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/
│   └── pom.xml
├── frontend/
│   ├── public/
│   ├── src/
│   │   ├── components/      # React components
│   │   ├── context/         # AuthContext
│   │   ├── pages/           # Page components
│   │   ├── services/        # API service layer
│   │   ├── App.js
│   │   └── index.js
│   └── package.json
└── Energy.md                # Requirements documentation
```

## Getting Started

### Prerequisites
- Java 17 or higher
- Node.js 16 or higher
- Maven 3.6+
- npm or yarn

### Backend Setup

1. Navigate to the backend directory:
   ```bash
   cd backend
   ```

2. Build the project:
   ```bash
   ./mvnw clean install
   ```

3. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

The backend server will start on `http://localhost:8080`

### Frontend Setup

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the development server:
   ```bash
   npm start
   ```

The frontend application will start on `http://localhost:3000`

## API Endpoints

### Authentication
- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Login user

### Appliances
- `GET /api/appliances` - Get all user appliances
- `POST /api/appliances` - Create new appliance
- `PUT /api/appliances/{id}` - Update appliance
- `DELETE /api/appliances/{id}` - Delete appliance

### Energy Consumption
- `POST /api/energy/log` - Log energy consumption
- `GET /api/energy/consumption` - Get consumption history
- `GET /api/energy/trends` - Get trend data with optional date filters

### Suggestions
- `GET /api/suggestions` - Get AI-based energy-saving suggestions

### Notifications
- `GET /api/notifications` - Get all notifications
- `GET /api/notifications/unread` - Get unread notifications
- `GET /api/notifications/stats` - Get notification statistics
- `POST /api/notifications/{id}/mark-read` - Mark notification as read

## Energy Calculation

The application uses the fundamental physics formula for energy calculation:

```
E = P × t

Where:
- E = Energy
- P = Power (in Watts)
- t = Time (in hours)

Result: Energy in Watt-hours (Wh)
Conversion: 1 kWh = 1000 Wh
```

Example:
- A 150W refrigerator running for 24 hours
- E = 150W × 24h = 3600 Wh = 3.6 kWh

## Features in Detail

### User Authentication
- Secure registration with password hashing (BCrypt)
- JWT-based authentication
- Token stored in sessionStorage (no localStorage as per requirements)
- Automatic logout on token expiration

### Appliance Management
- Add appliances with name, power rating, and category
- View all registered appliances
- Delete appliances
- Null-safe validation on all inputs

### Energy Logging
- Select appliance from dropdown
- Enter usage hours
- Select date
- Automatic calculation of Wh and kWh
- Week number and year tracking

### Data Visualization
- Line chart showing daily consumption trends
- Bar chart showing consumption by appliance
- Interactive tooltips with detailed information
- Responsive design

### AI Suggestions
- Analyzes last 30 days of consumption
- Identifies high-consumption appliances
- Provides actionable energy-saving tips
- Calculates potential savings

### Notifications
- Automatic alerts for high consumption (>5 kWh daily)
- Visual indicators for unread notifications
- Mark as read functionality
- Categorized by type (HIGH_CONSUMPTION, SUGGESTION, INFO)

## Database Schema

### User Table
- id, username, email, password, created_at

### Appliance Table
- id, user_id, name, power_rating_watts, category, created_at

### Energy_Consumption Table
- id, appliance_id, usage_hours, energy_wh, energy_kwh, logged_date, week_number, year

### Notification Table
- id, user_id, message, type, is_read, created_at

## Security Features

- CORS configured for frontend origin
- JWT token-based authentication
- Password encryption with BCrypt
- Protected API endpoints
- Input validation on all forms
- SQL injection prevention through JPA
- XSS protection

## Error Handling

- Null-safe validation across all endpoints
- Comprehensive error messages
- User-friendly error display in UI
- Automatic redirect on authentication failure
- Try-catch blocks for all API calls

## Testing

Run backend tests:
```bash
cd backend
./mvnw test
```

Run frontend tests:
```bash
cd frontend
npm test
```

## Production Deployment

### Backend
1. Update `application.properties` for production database
2. Build JAR file: `./mvnw clean package`
3. Run: `java -jar target/energy-monitoring-backend-1.0.0.jar`

### Frontend
1. Build production bundle: `npm run build`
2. Serve build folder with a web server (nginx, Apache, etc.)

## Environment Variables

### Backend (application.properties)
- `spring.datasource.url` - Database URL
- `jwt.secret` - JWT signing secret
- `jwt.expiration` - Token expiration time
- `cors.allowed-origins` - Allowed CORS origins

### Frontend
Create `.env` file:
```
REACT_APP_API_URL=http://localhost:8080/api
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is open-source and available under the MIT License.

## Support

For issues and questions, please open an issue on the GitHub repository.

## Future Enhancements

- Machine learning for consumption predictions
- Mobile app integration
- Real-time monitoring with IoT devices
- Cost calculations based on utility rates
- Multi-user households
- Export data to CSV/PDF
- Email notifications
- Dark mode theme
- Multi-language support

---

**Built with ❤️ by the Energy Buddy Team**
