package peaksoft.dao.impls;

import peaksoft.dao.JobDao;
import peaksoft.models.Job;
import peaksoft.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JobDaoImpl implements JobDao {
    private final Connection connection;


    public JobDaoImpl() {
        connection = Util.getConnection();
    }
    @Override
    public void createJobTable() {
        String query = """
                CREATE TABLE IF NOT EXISTS jobs(
                 id SERIAL PRIMARY KEY,
                 position VARCHAR,
                 profession VARCHAR,
                 description VARCHAR,
                 experience INT
                )""";


        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addJob(Job job) {
        String query = """
                INSERT INTO jobs(position, profession, description, experience)
                VALUES (?, ?, ?, ?);
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, job.getPosition());
            preparedStatement.setString(2, job.getProfession());
            preparedStatement.setString(3, job.getDescription());
            preparedStatement.setInt(4, job.getExperience());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Job getJobById(Long jobId) {
        String query = "SELECT * FROM jobs WHERE id = ?";
        Job job = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, jobId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    job = new Job();
                    job.setId(resultSet.getLong("id"));
                    job.setPosition(resultSet.getString("position"));
                    job.setDescription(resultSet.getString("description"));
                    job.setExperience(resultSet.getInt("experience"));
                    job.setProfession(resultSet.getString("profession"));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return job;
    }

    @Override
    public List<Job> sortByExperience(String ascOrDesc) {
        String query = "SELECT * FROM jobs ORDER BY experience " + ascOrDesc;
        List<Job> jobs = new ArrayList<>();

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Job job = new Job();
                job.setProfession(resultSet.getString("profession"));
                job.setId(resultSet.getLong("id"));
                job.setExperience(resultSet.getInt("experience"));
                job.setDescription(resultSet.getString("description"));
                job.setPosition(resultSet.getString("position"));
                jobs.add(job);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return jobs;
    }


    @Override
    public Job getJobByEmployeeId(Long employeeId) {
        String query = "SELECT * FROM employees e INNER JOIN jobs j ON j.id = e.job_id WHERE e.id = ?";
        Job job = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, employeeId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    job = new Job();
                    job.setId((long) resultSet.getInt("id"));
                    job.setPosition(resultSet.getString("position"));
                    job.setDescription(resultSet.getString("description"));
                    job.setExperience(resultSet.getInt("experience"));
                    job.setProfession(resultSet.getString("profession"));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return job;
    }

    @Override
    public void deleteDescriptionColumn() {
        String query = "ALTER TABLE jobs DROP COLUMN description";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
