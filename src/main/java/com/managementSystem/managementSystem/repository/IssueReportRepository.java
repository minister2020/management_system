package com.managementSystem.managementSystem.repository;

import com.managementSystem.managementSystem.model.IssueReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IssueReportRepository extends JpaRepository<IssueReport, Long> {
        List<IssueReport> findByReportedBy(String reportedBy);

}
