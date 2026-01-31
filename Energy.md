# Energy Monitoring Web Application

## Overview
A full-stack energy monitoring web application that allows users to register appliances, log weekly energy consumption, view data trends through charts, receive AI-based energy-saving suggestions, and get push notifications for high energy consumption appliances.

## Core Functionality

### User Features
- **Register Appliances**: Add and manage appliances with their power ratings
- **Log Energy Consumption**: Record weekly energy usage for each appliance
- **View Trends**: Visualize energy consumption data over time through interactive charts
- **AI Suggestions**: Receive intelligent energy-saving recommendations
- **Push Notifications**: Get alerts for appliances with high energy consumption

### Mathematical Model
The application uses the following physics formula for energy calculations:
```
P (Power in Watts) → E = P × t (Energy = Power × time) → Wh (Watt-hours) → kWh (Kilowatt-hours)
```

The system stores both Wh and kWh consumption metrics for comprehensive tracking.

## Tech Stack

### Frontend
- **React**: Main UI framework
- **Recharts**: For data visualization and charts
- **Context API**: For authentication state management
- **Axios**: For API communication

### Backend
- **Spring Boot**: RESTful API server
- **JPA/Hibernate**: Database ORM
- **H2/PostgreSQL**: Database
- **JWT**: Authentication
- **EnergyCalculator**: Core calculation logic

## Architecture

### Backend Components
1. **Models/Entities**
   - User
   - Appliance
   - EnergyConsumption
   
2. **Services**
   - UserService
   - ApplianceService
   - EnergyService
   - NotificationService
   - SuggestionService

3. **Controllers**
   - AuthController
   - ApplianceController
   - EnergyController
   - NotificationController

4. **Utilities**
   - EnergyCalculator: Converts P × t to Wh and kWh
   - JwtUtil: Token generation and validation

### Frontend Components
1. **Authentication**
   - AuthContext (JWT session management)
   - Login/Register pages

2. **Dashboard**
   - Appliance list
   - Energy consumption charts
   - Suggestions panel
   - Notifications

3. **Forms**
   - Appliance registration
   - Energy logging

## Key Requirements

### Error Handling
1. **Null-Safe Validation**: Apply across all SQL queries and JavaScript endpoints
2. **No Local Storage**: Use memory/session storage with AuthContext and JWT bearer tokens
3. **Graph Rendering**: Consume real dynamic API data, no mocked data for charts
4. **Error States**: Proper error logging and user feedback for all operations
5. **Notification System**: Real-time alerts for high consumption patterns

### Security
- JWT-based authentication
- Secure password storage (BCrypt)
- Protected API endpoints
- Input validation and sanitization

### Data Flow
1. User registers and logs in
2. User adds appliances with power ratings (Watts)
3. User logs usage time for each appliance
4. Backend calculates: E = P × t (Wh and kWh)
5. Data is stored and returned to frontend
6. Frontend renders charts using Recharts
7. AI engine analyzes patterns and generates suggestions
8. Notification service alerts on high consumption

## API Endpoints

### Authentication
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login

### Appliances
- `GET /api/appliances` - List all appliances
- `POST /api/appliances` - Register new appliance
- `PUT /api/appliances/{id}` - Update appliance
- `DELETE /api/appliances/{id}` - Delete appliance

### Energy Consumption
- `GET /api/energy/consumption` - Get consumption history
- `POST /api/energy/log` - Log energy consumption
- `GET /api/energy/trends` - Get trend data for charts

### Suggestions
- `GET /api/suggestions` - Get AI-based energy-saving suggestions

### Notifications
- `GET /api/notifications` - Get user notifications
- `POST /api/notifications/mark-read` - Mark notification as read

## Database Schema

### User Table
- id (Primary Key)
- username
- email
- password (hashed)
- created_at

### Appliance Table
- id (Primary Key)
- user_id (Foreign Key)
- name
- power_rating_watts
- category
- created_at

### Energy_Consumption Table
- id (Primary Key)
- appliance_id (Foreign Key)
- usage_hours
- energy_wh
- energy_kwh
- logged_date
- week_number
- year

### Notification Table
- id (Primary Key)
- user_id (Foreign Key)
- message
- type
- is_read
- created_at

## Development Guidelines

1. **Validation**: Implement comprehensive input validation on both frontend and backend
2. **Error Handling**: Use try-catch blocks and proper error responses
3. **Testing**: Write unit tests for critical business logic
4. **Documentation**: Keep API documentation up to date
5. **Code Quality**: Follow clean code principles and naming conventions

## Energy Calculation Formula

```java
// Backend EnergyCalculator
public class EnergyCalculator {
    public static double calculateEnergyWh(double powerWatts, double timeHours) {
        return powerWatts * timeHours;
    }
    
    public static double calculateEnergyKwh(double energyWh) {
        return energyWh / 1000.0;
    }
}
```

## Getting Started

### Backend Setup
```bash
cd backend
./mvnw spring-boot:run
```

### Frontend Setup
```bash
cd frontend
npm install
npm start
```

## Future Enhancements
- Machine learning for better predictions
- Mobile app integration
- Real-time monitoring with IoT devices
- Cost calculations based on utility rates
- Multi-user households with shared appliances
