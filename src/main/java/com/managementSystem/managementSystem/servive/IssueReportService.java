package com.managementSystem.managementSystem.servive;

import com.managementSystem.managementSystem.exception.NotFoundException;
import com.managementSystem.managementSystem.model.IssueReport;
import com.managementSystem.managementSystem.repository.IssueReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class IssueReportService {
    @Autowired
    private IssueReportRepository issueReportRepository;
    @Autowired
    private MailService mailService;
    @Value("${app.admin.email}")
    private String adminEmail;

    public IssueReport reportIssue(IssueReport issueReport) {
        issueReport.setReportedAt(LocalDateTime.now());
        IssueReport saved = issueReportRepository.save(issueReport);
        // Notify admin
        mailService.sendEmail(
                adminEmail,
                "New Issue Reported",
                "A new issue has been reported by: " + issueReport.getReportedBy() +
                        "\n\nDescription: " + issueReport.getDescription()
        );
        return saved;
    }

    public List<IssueReport> getAllIssues() {
        return issueReportRepository.findAll();
    }

    public List<IssueReport> getIssuesByReportedBy(String reportedBy) {
        List<IssueReport> issues = issueReportRepository.findByReportedBy(reportedBy);
        if (issues.isEmpty()) {
            throw new NotFoundException("No issues found for reporter: " + reportedBy);
        }
        return issues;
       }
}