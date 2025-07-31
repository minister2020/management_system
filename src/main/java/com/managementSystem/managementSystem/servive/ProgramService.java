package com.managementSystem.managementSystem.servive;

import com.managementSystem.managementSystem.exception.NotFoundException;
import com.managementSystem.managementSystem.model.IssueReport;
import com.managementSystem.managementSystem.model.Program;
import com.managementSystem.managementSystem.repository.MemberRepository;
import com.managementSystem.managementSystem.repository.ProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProgramService {
    @Autowired
    private ProgramRepository programRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MailService mailService;

        public Program addProgram(Program program) {
            Program saved = programRepository.save(program);

            // Notify all members
            memberRepository.findAll().forEach(member -> {
                String email = member.getEmail();
                if (isValidEmail(email)) {
                    try {
                        mailService.sendEmail(
                                email,
                                "New Program Added: " + (program.getName() != null ? program.getName() : "No Name"),
                                "A new program has been added: " + (program.getDescription() != null ? program.getDescription() : "No Description") +
                                        "\nDate: " + (program.getProgramDate() != null ? program.getProgramDate().toString() : "No Date")
                        );
                    } catch (Exception ex) {
                        System.err.println("Failed to send email to: " + email + ". Error: " + ex.getMessage());
                    }
                } else {
                    System.err.println("Invalid email skipped: " + email);
                }
            });

            return saved;
        }

    private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) return false;
        // Basic structure check
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }



//    private boolean isValidEmail(String email) {
//        return email != null && !email.trim().isEmpty() && email.contains("@") && email.contains(".");
//    }

    public List<Program> getAllPrograms() {
        return programRepository.findAll();
    }

    public List<Program> getProgramsByName(String name) {
            List<Program> programs = programRepository.findByName(name);
            if (programs.isEmpty()) {
                throw new NotFoundException("No program found for: " + name);
            }
            return programs;
        }
}
