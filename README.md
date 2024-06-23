# Contact Management

Contact Management is a Java Spring Boot RESTful API project designed to manage contacts. It provides functionalities such as creating, reading, updating, and deleting contact information. This project is built using Spring Boot, making it easy to set up and run in any environment.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

Before you begin, ensure you have met the following requirements:

- Java JDK 17 or newer
- Maven
- MySQL

### Installation

1. **Clone the repository**

   ```bash
   git clone https://github.com/yourusername/contact-management.git
   cd contact-management
   ```

2. **Configure MySQL Database**

Create a database in MySQL and note the credentials. You will need to update the environment variables accordingly.

3. **Set Environment Variables**

Set the following environment variables based on your MySQL configuration:

- `DB_USER`: Database username
- `DB_PASSWORD`: Database password
- `DB_HOST`: Database host (e.g., localhost)
- `DB_PORT`: Database port (default is 3306)
- `DB_NAME`: Database name

You can set these variables in your IDE or export them in your terminal session.
If you're [using VS Code](https://stackoverflow.com/questions/59687712/how-to-set-java-environment-variable-in-vs-code), you can set them in the `launch.json` file under the `.vscode` directory.

4. **Run the Application**

Depending on your IDE, you can run the application by running the main class `ContactManagementApplication`, or you can run the following command:

```bash
mvn spring-boot:run
```

Make sure to export the environment variables before running the application. The application will start on port 8080 by default.

## Contributing

Contributions are welcome! For major changes, please open an issue first to discuss what you would like to change.
