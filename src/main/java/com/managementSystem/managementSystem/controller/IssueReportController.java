package com.managementSystem.managementSystem.controller;

import com.managementSystem.managementSystem.model.IssueReport;
import com.managementSystem.managementSystem.servive.IssueReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/issues")
public class IssueReportController {
    @Autowired
    private IssueReportService issueReportService;

    @PostMapping("/report-issues")
    public IssueReport reportIssues(@RequestBody IssueReport issueReport) {
        return issueReportService.reportIssue(issueReport);
    }

    @RequestMapping("/allIssues")
    public List<IssueReport> getAllIssues() {
        return  issueReportService.getAllIssues();
    }

    @GetMapping("/by-reporter")
    public List<IssueReport> getIssuesByReportedBy(@RequestParam String reportedBy) {
        return issueReportService.getIssuesByReportedBy(reportedBy);
    }
}
