package com.example.webhooksserver.service.impl;

import com.example.webhooksserver.domain.GitRepo;
import com.example.webhooksserver.domain.GitRepoTaskMapping;
import com.example.webhooksserver.domain.IssueType;
import com.example.webhooksserver.domain.Task;
import com.example.webhooksserver.domain.TaskToIssueMapping;
import com.example.webhooksserver.dtos.RepoTasksDto;
import com.example.webhooksserver.dtos.TaskIssueMappingDto;
import com.example.webhooksserver.repository.GitRepoRepository;
import com.example.webhooksserver.repository.GitRepoTaskMappingRepository;
import com.example.webhooksserver.repository.IssueTypeRepository;
import com.example.webhooksserver.repository.TaskRepository;
import com.example.webhooksserver.repository.TaskToIssueMappingRepository;
import com.example.webhooksserver.service.api.ClientService;
import com.example.webhooksserver.service.exceptions.NoSuchRepositoryException;
import com.example.webhooksserver.service.exceptions.NoSuchTaskException;
import com.example.webhooksserver.service.exceptions.OneToOneMappingExistsException;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ClientServiceImpl implements ClientService {

    private final GitRepoRepository gitRepoRepository;
    private final TaskRepository taskRepo;
    private final GitRepoTaskMappingRepository gitRepoTaskMappingRepository;
    private final IssueTypeRepository issueTypeRepository;
    private final TaskToIssueMappingRepository taskToIssueMappingRepository;

    ClientServiceImpl(GitRepoRepository gitRepoRepository, TaskRepository taskRepo,
            GitRepoTaskMappingRepository gitRepoTaskMappingRepository, IssueTypeRepository issueTypeRepository,
            TaskToIssueMappingRepository taskToIssueMappingRepository) {

        this.gitRepoRepository = gitRepoRepository;
        this.taskRepo = taskRepo;
        this.gitRepoTaskMappingRepository = gitRepoTaskMappingRepository;
        this.issueTypeRepository = issueTypeRepository;
        this.taskToIssueMappingRepository = taskToIssueMappingRepository;
    }

    @Override
    public void addRepo(RepoTasksDto object) {
        GitRepo repoInfo = GitRepo.builder().repoName(object.getRepoName()).build();
        gitRepoRepository.save(repoInfo);
        for (int i = 0; i < object.getTasks().size(); i++) {
            try {
                if (validateTask(object.getTasks().get(i))) {
                    gitRepoTaskMappingRepository.save(GitRepoTaskMapping.builder().repoId(repoInfo.getId())
                            .taskId(object.getTasks().get(i)).build());
                } else
                    throw new NoSuchTaskException("Task id invalid");
            } catch (Exception e) {
                log.info(e.getMessage());
            }
        }
    }

    @Override
    public Long createTask(String obj) {
        Task newTask = Task.builder().taskType(obj).build();
        taskRepo.save(newTask);
        return newTask.getId();

    }

    @Override
    public void addTaskToRepo(RepoTasksDto object) {
        try {
            if (validateRepository(object.getRepoName()) == false) {
                throw new NoSuchRepositoryException();
            }
            Long repoId = getRepoId(object.getRepoName());
            for (int i = 0; i < object.getTasks().size(); i++) {
                try {
                    if (validateTask(object.getTasks().get(i)) == false)
                        throw new NoSuchTaskException();
                    else {
                        gitRepoTaskMappingRepository.save(
                                GitRepoTaskMapping.builder().repoId(repoId).taskId(object.getTasks().get(i)).build());
                    }
                } catch (Exception e) {
                    log.info(e.getMessage());
                }
            }

        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    @Override
    public void deleteTaskFromRepo(RepoTasksDto object) {
        try {
            if (validateRepository(object.getRepoName()) == false) {
                throw new NoSuchRepositoryException();
            }
            Long repoId = getRepoId(object.getRepoName());
            for (int i = 0; i < object.getTasks().size(); i++) {
                try {
                    if (validateTask(object.getTasks().get(i)) == false)
                        throw new NoSuchTaskException();
                    else {
                        gitRepoTaskMappingRepository.delete(
                                gitRepoTaskMappingRepository.findByRepoIdAndTaskId(repoId, object.getTasks().get(i)));
                    }
                } catch (Exception e) {
                    log.info(e.getMessage());
                }
            }

        } catch (Exception e) {
            log.info(e.getMessage());
        }

    }

    @Override
    public Long addNewIssueType(String issue) {

        IssueType newIssue = IssueType.builder().issueType(issue).build();
        issueTypeRepository.save(newIssue);
        return newIssue.getId();
    }

    @Override
    public void createIssueMapping(TaskIssueMappingDto object) {
        Task storedTask = taskRepo.findByTaskType(object.getTaskType());
        IssueType storedIssue = issueTypeRepository.findByIssueType(object.getIssueType());
        if (storedTask != null && storedIssue != null) {
            try {
                if (taskToIssueMappingRepository.findByTaskIdOrIssueId(storedTask.getId(),
                        storedIssue.getId()) == null) {
                    taskToIssueMappingRepository.save(TaskToIssueMapping.builder().taskId(storedTask.getId())
                            .issueId(storedIssue.getId()).build());
                } else
                    throw new OneToOneMappingExistsException();

            } catch (Exception e) {
                log.info(e.getMessage());
            }
        } else if (storedTask != null && storedIssue == null) {
            try {
                if (taskToIssueMappingRepository.findByTaskId(storedTask.getId()) == null) {
                    taskToIssueMappingRepository.save(TaskToIssueMapping.builder()
                            .issueId(addNewIssueType(object.getIssueType())).taskId(storedTask.getId()).build());
                } else
                    throw new OneToOneMappingExistsException();
            } catch (Exception e) {
                log.info(e.getMessage());
            }
        } else if (storedTask == null && storedIssue != null) {
            try {
                if (taskToIssueMappingRepository.findByIssueId(storedIssue.getId()) == null) {
                    taskToIssueMappingRepository.save(TaskToIssueMapping.builder().issueId(storedIssue.getId())
                            .taskId(createTask(object.getTaskType())).build());
                } else
                    throw new OneToOneMappingExistsException();
            } catch (Exception e) {
                log.info(e.getMessage());
            }
        }
    }

    boolean validateTask(Long taskId) {
        return taskRepo.findById(taskId).isPresent();
    }

    boolean validateRepository(String repoName) {
        if (gitRepoRepository.findByRepoName(repoName) != null)
            return true;
        return false;
    }

    Long getRepoId(String repoName) {
        return gitRepoRepository.findByRepoName(repoName).getId();
    }

}