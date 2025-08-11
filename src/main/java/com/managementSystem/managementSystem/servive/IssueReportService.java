package com.managementSystem.managementSystem.servive;//package com.managementSystem.managementSystem.servive;
//
//import com.managementSystem.managementSystem.exception.NotFoundException;
//import com.managementSystem.managementSystem.model.IssueReport;
//import com.managementSystem.managementSystem.repository.IssueReportRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Service
//public class IssueReportService {
//    @Autowired
//    private IssueReportRepository issueReportRepository;
//    @Autowired
//    private MailService mailService;
//    @Value("${app.admin.email}")
//    private String adminEmail;
//
//    public IssueReport reportIssue(IssueReport issueReport) {
//        issueReport.setReportedAt(LocalDateTime.now());
//        IssueReport saved = issueReportRepository.save(issueReport);
//        // Notify admin
//        mailService.sendEmail(
//                adminEmail,
//                "New Issue Reported",
//                "A new issue has been reported by: " + issueReport.getReportedBy() +
//                        "\n\nDescription: " + issueReport.getDescription()
//        );
//        return saved;
//    }
//
//    public List<IssueReport> getAllIssues(String currentUserRole) {
//        if (!"ADMIN".equalsIgnoreCase(currentUserRole)) {
//            throw new AccessDeniedException("Only admins can view reported issues.");
//        }
//        return issueReportRepository.findAll();
//    }
//
//
//    public List<IssueReport> getIssuesByReportedBy(String reportedBy) {
//        List<IssueReport> issues = issueReportRepository.findByReportedBy(reportedBy);
//        if (issues.isEmpty()) {
//            throw new NotFoundException("No issues found for reporter: " + reportedBy);
//        }
//        return issues;
//       }
//}

import com.managementSystem.managementSystem.exception.NotFoundException;
import com.managementSystem.managementSystem.model.IssueReport;
import com.managementSystem.managementSystem.repository.IssueReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IssueReportService {

    private final IssueReportRepository issueReportRepository;
    private final MailService mailService;

    private static final String ADMIN_EMAIL = "abdulrasheedidowu86@gmail.com";

    /**
     * Allow any authenticated user to report an issue.
     */
    public IssueReport reportIssue(IssueReport issueReport) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); // Extract username from auth
        issueReport.setReportedBy(username);
        issueReport.setReportedAt(LocalDateTime.now());

        IssueReport saved = issueReportRepository.save(issueReport);

        // Notify admin
        mailService.sendEmail(
                ADMIN_EMAIL,
                "New Issue Reported",
                "A new issue has been reported by: " + saved.getReportedBy() +
                        "\n\nDescription: " + saved.getDescription()
        );
        return saved;
    }

    /**
     * Only admin can view all issues
     */
    public List<IssueReport> getAllIssues() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new AccessDeniedException("Only admins can view reported issues.");
        }
        return issueReportRepository.findAll();
    }

    /**
     * A user can view only their issues. Admin can view anyone's issue.
     */
    public List<IssueReport> getIssuesByReportedBy(String reportedBy) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !currentUsername.equalsIgnoreCase(reportedBy)) {
            throw new AccessDeniedException("Access denied to view issues of another user.");
        }

        List<IssueReport> issues = issueReportRepository.findByReportedBy(reportedBy);
        if (issues.isEmpty()) {
            throw new NotFoundException("No issues found for reporter: " + reportedBy);
        }
        return issues;
    }
}
