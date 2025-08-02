package com.managementSystem.managementSystem.servive;

import com.managementSystem.managementSystem.exception.NotFoundException;
import com.managementSystem.managementSystem.model.Program;
import com.managementSystem.managementSystem.repository.MemberRepository;
import com.managementSystem.managementSystem.repository.ProgramRepository;
import com.managementSystem.managementSystem.util.ProgramUtil;
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
    @Autowired
    private ProgramUtil programUtil;

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
                                programUtil.buildProgramNotification(program)
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

            public Program updateProgram(Long id, Program updatedProgram) {
                Program program = programRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Program not found with id: " + id));

                // Update fields
                program.setName(updatedProgram.getName());
                program.setVenue(updatedProgram.getVenue());
                program.setDescription(updatedProgram.getDescription());
                program.setProgramDate(updatedProgram.getProgramDate());
                program.setProgramTime(updatedProgram.getProgramTime());

                Program savedProgram = programRepository.save(program);
                // Notify all members after update
                memberRepository.findAll().forEach(member -> {
                    String email = member.getEmail();
                    if (isValidEmail(email)) {
                        try {
                            mailService.sendEmail(
                                    email,
                                    "Program Updated: " + (program.getName() != null ? program.getName() : "No Name"),
                                    programUtil.buildProgramUpdateNotification(program)
                            );
                        } catch (Exception e) {
                            System.err.println("Failed to notify " + email + ": " + e.getMessage());
                        }
                    }
                });

                return savedProgram;
            }
        }

