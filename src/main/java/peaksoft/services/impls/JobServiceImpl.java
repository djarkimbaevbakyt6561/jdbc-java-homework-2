package peaksoft.services.impls;

import peaksoft.dao.JobDao;
import peaksoft.dao.impls.JobDaoImpl;
import peaksoft.models.Job;
import peaksoft.services.JobService;

import java.util.List;

public class JobServiceImpl implements JobService {
    JobDao jobDao = new JobDaoImpl();

    @Override
    public void createJobTable() {
        jobDao.createJobTable();
    }

    @Override
    public void addJob(Job job) {
        jobDao.addJob(job);
    }

    @Override
    public Job getJobById(Long jobId) {
        return jobDao.getJobById(jobId);
    }

    @Override
    public List<Job> sortByExperience(String ascOrDesc) {
        if (ascOrDesc.equals("asc") || ascOrDesc.equals("desc")) {
            return jobDao.sortByExperience(ascOrDesc);
        }
        return null;
    }

    @Override
    public Job getJobByEmployeeId(Long employeeId) {
        return jobDao.getJobByEmployeeId(employeeId);
    }

    @Override
    public void deleteDescriptionColumn() {
        jobDao.deleteDescriptionColumn();
    }
}
