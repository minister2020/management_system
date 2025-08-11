package com.managementSystem.managementSystem.controller;

import com.managementSystem.managementSystem.model.IssueReport;
import com.managementSystem.managementSystem.servive.IssueReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

    @RestController
    @RequestMapping("/api/issues")
    @RequiredArgsConstructor
    public class IssueReportController {

        private final IssueReportService issueReportService;

        @PostMapping("/report-issues")
        public ResponseEntity<IssueReport> reportIssue(@RequestBody IssueReport issueReport) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.isAuthenticated()
                    && !"anonymousUser".equals(authentication.getPrincipal())) {

                String username = authentication.getName();
                issueReport.setReportedBy(username);
            } else {
                issueReport.setReportedBy("anonymous");
            }

            IssueReport saved = issueReportService.reportIssue(issueReport);
            return ResponseEntity.ok(saved);
        }

        //Only admins can access
        @PreAuthorize("hasRole('ADMIN')")
        @GetMapping("/alllssues")
        public ResponseEntity<List<IssueReport>> getAllIssues() {
            return ResponseEntity.ok(issueReportService.getAllIssues());
        }

        //Any authenticated user can access their own reports
        @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
        @GetMapping("/by-reporter")
        public ResponseEntity<List<IssueReport>> getIssuesByReportedBy(@RequestParam String reportedBy,
                                                                       Authentication authentication) {
            String currentUsername = authentication.getName();
            if (!currentUsername.equals(reportedBy) && !authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
                throw new AccessDeniedException("You can only view your own reports.");
            }
            return ResponseEntity.ok(issueReportService.getIssuesByReportedBy(reportedBy));
        }
}
