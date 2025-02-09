# Real-Time Ticketing System

## Introduction

The Real-Time Ticketing System is a comprehensive project showcasing multi-threaded Java CLI, Angular-based frontend, and a Spring Boot backend. The system manages ticket operations, including configuration, vendor ticket addition, customer ticket purchase, and real-time updates via WebSockets.

---

## Setup Instructions

### Prerequisites

1. **Java**:

   - Version: JDK 11 or above.

2. **Node.js & npm**:
   - Version: Node.js 16.x or above.
   - Verify with `node -v` and `npm -v`.

3. **Spring Boot**:
   - Ensure Maven is installed and available in the environment.
   
4. **Development Tools**:
   - IntelliJ IDEA or VS Code for backend and CLI development.
   - Any browser (e.g., Chrome) for frontend display.

---

### Project Structure

1. **CLI**: Java-based multi-threaded CLI for system configuration and ticket operations.
2. **Backend**: Spring Boot RESTful API for ticket and log management.
3. **Frontend**: Angular application displaying real-time updates.

---

### Setup Instructions

#### CLI Application

1. Clone the repository or download the ZIP file.

2. Navigate to the `CLI` directory in the terminal.

3. Compile the project:

   ```bash
   javac -d bin src/*.java
   ```

4. Run the application:
   ```bash
   java -cp bin Main
   ```

#### Backend (Spring Boot)

1. Navigate to the backend directory:

   ```bash
   cd backend
   ```
2. Build the Spring Boot application:

   ```bash
   mvn clean install
   ```
3. Start the backend server:

   ```bash
   mvn spring-boot:run
   ```

#### Frontend (Angular)

1. Navigate to the Angular project directory:

   ```bash
   cd frontend
   ```

2. Install dependencies:

   ```bash
   npm install
   ```

3. Start the frontend server:

   ```bash
   ng serve
   ```

4. Open the application in a browser at `http://localhost:4200`.

---

## Usage Instructions

### CLI

1. **Configuration**:

   - Input the following parameters:

     - Total Tickets
     - Ticket Release Rate
     - Customer Retrieval Rate
     - Maximum Ticket Capacity
   - Invalid inputs will prompt for re-entry.

2. **Commands**:

   - `start`: Starts ticket operations.
   - `stop`: Pauses ticket operations.
   - `exit`: Exits the application.

3. **Simulation**:

   - Uses 3 vendor threads and 5 customer threads for balanced operations.

### Backend

1. RESTful endpoints are available for:

   - `/api/configure`: Configures the system.
   - `/api/logs`: Retrieves system logs.
   - `/api/ticket-status`: Fetches ticket pool status.
   - `/api/start`: Starts the system.
   - `/api/stop`: Stops the system.
   - `/api/exit`: Exits the system.

2. Logs and system status are updated in real-time.

### Frontend

1. **Configuration Form**:

   - Input and submit system configuration.

2. **Control Panel**:

   - Buttons to `start`, `pause`, and `exit` the system.

3. **Logs**:

   - Displays real-time logs of system activities and real-time updates on ticket availability.


---

## Troubleshooting

1. **CLI Issues**:

   - Ensure Java is installed.
   - Check console logs for any input validation errors.

2. **Backend Issues**:

   - Ensure Spring Boot is running on port 8080.
   - Verify backend logs for API errors.

3. **Frontend Issues**:

   - Ensure `ng serve` is running and accessible at `http://localhost:4200`.
   - Check browser console for frontend errors.

---

## Contact

For further support or queries, contact Nilni Ariyarathna.


⚠️ This project is **read-only**. Contributions are not accepted.
