package peaksoft.dao.impls;

import peaksoft.dao.EmployeeDao;
import peaksoft.models.Employee;
import peaksoft.models.Job;
import peaksoft.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeDaoImpl implements EmployeeDao {
    private final Connection connection;


    public EmployeeDaoImpl() {
        connection = Util.getConnection();
    }

    @Override
    public void createEmployee() {
        String query = """
                CREATE TABLE IF NOT EXISTS employees(
                 id SERIAL PRIMARY KEY,
                 first_name VARCHAR,
                 last_name VARCHAR,
                 age INT,
                 email VARCHAR UNIQUE,
                 job_id INT REFERENCES jobs(id)
                );""";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addEmployee(Employee employee) {
        String query = """
                INSERT INTO employees(first_name, last_name, age, email, job_id)
                VALUES (?, ?, ?, ?, ?);
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, employee.getFirstName());
            preparedStatement.setString(2, employee.getLastName());
            preparedStatement.setInt(3, employee.getAge());
            preparedStatement.setString(4, employee.getEmail());
            preparedStatement.setInt(5, employee.getJobId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void dropTable() {
        String query = "DROP TABLE employees;";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void cleanTable() {
        String query = "DELETE FROM employees";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateEmployee(Long id, Employee employee) {
        String query = """
                          UPDATE employees
                          SET first_name = ?, last_name = ?, age = ? , email = ?, job_id = ?
                          WHERE id = ?;
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, employee.getFirstName());
            preparedStatement.setString(2, employee.getLastName());
            preparedStatement.setInt(3, employee.getAge());
            preparedStatement.setString(4, employee.getEmail());
            preparedStatement.setInt(5, employee.getJobId());
            preparedStatement.setLong(6, employee.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Employee> getAllEmployees() {
        String query = "SELECT * FROM employees";
        List<Employee> employeeList = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Employee employee = createEmployee(resultSet);
                employeeList.add(employee);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return employeeList;
    }

    @Override
    public Employee findByEmail(String email) {
        String query = "SELECT * FROM employees WHERE email = ?";
        Employee employee = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    employee = createEmployee(resultSet);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return employee;
    }


    @Override
    public Map<Employee, Job> getEmployeeById(Long employeeId) {
        String query = "SELECT * FROM employees e INNER JOIN jobs j ON j.id = e.job_id WHERE e.id = ?";
        Map<Employee, Job> result = new HashMap<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, employeeId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Employee employee = createEmployee(resultSet);

                    Job job = new Job();
                    job.setId(resultSet.getLong("id"));  // Используйте столбцы без алиаса
                    job.setPosition(resultSet.getString("position"));
                    job.setDescription(resultSet.getString("description"));
                    job.setExperience(resultSet.getInt("experience"));
                    job.setProfession(resultSet.getString("profession"));

                    result.put(employee, job);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return result;
    }

    @Override
    public List<Employee> getEmployeeByPosition(String position) {
        String query = "SELECT * FROM employees e INNER JOIN jobs j ON j.id = e.job_id WHERE j.position = ?";
        List<Employee> employees = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, position);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Employee employee = createEmployee(resultSet);
                    employees.add(employee);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return employees;
    }

    private Employee createEmployee(ResultSet resultSet) throws SQLException {
        Employee employee = new Employee();
        employee.setId(resultSet.getLong("id"));
        employee.setFirstName(resultSet.getString("first_name"));
        employee.setLastName(resultSet.getString("last_name"));
        employee.setAge(resultSet.getInt("age"));
        employee.setEmail(resultSet.getString("email"));
        employee.setJobId(resultSet.getInt("job_id"));
        return employee;
    }
}
