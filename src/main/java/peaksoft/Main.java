package peaksoft;

import peaksoft.services.EmployeeService;
import peaksoft.services.JobService;
import peaksoft.services.impls.EmployeeServiceImpl;
import peaksoft.services.impls.JobServiceImpl;

public class Main {
    public static void main(String[] args) {
        EmployeeService employeeService = new EmployeeServiceImpl();
        JobService jobService = new JobServiceImpl();

        jobService.createJobTable();

        employeeService.createEmployee();

//        jobService.addJob(new Job("Mentor","Java","Backend developer", 4));
//        jobService.addJob(new Job("Management","JavaScript","Frontend developer", 1));
//        jobService.addJob(new Job("Instructor","C#",".Net Developer", 7));

//        employeeService.addEmployee(new Employee("Alinur", "Abdykadyrov", 18, "alinur@gmail.com", 1));
//        employeeService.addEmployee(new Employee("Bekzat", "Khalipov", 24, "bekzat@gmail.com", 2));
//        employeeService.addEmployee(new Employee("Ustinya", "Dzharkymbaeva", 22, "ustya@gmail.com", 3));

//        System.out.println(employeeService.getEmployeeById(1L));
//        System.out.println(employeeService.getAllEmployees());
//        System.out.println(employeeService.getEmployeeByPosition("Mentor"));
//        System.out.println(employeeService.findByEmail("alinur@gmail.com"));

//        System.out.println(jobService.getJobById(1L));
//        System.out.println(jobService.getJobByEmployeeId(1L));
//        System.out.println(jobService.sortByExperience("asc"));
//        jobService.deleteDescriptionColumn();
    }
}
