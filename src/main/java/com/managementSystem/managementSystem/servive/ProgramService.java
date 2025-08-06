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
    @Autowired
    private SmsService smsService;

        public Program addProgram(Program program) {
            Program saved = programRepository.save(program);

            // Prepare email content
            String emailMessage = programUtil.buildProgramNotification(program);
            String subject = "New Program Added: " + (program.getName() != null ? program.getName() : "No Name");

            // Notify all members
            memberRepository.findAll().forEach(member -> {
                String email = member.getEmail();
                String phone = member.getPhoneNumber();

                // Send Email if valid
                if (isValidEmail(email)) {
                    try {
                        mailService.sendEmail(email, subject, emailMessage);
                    } catch (Exception ex) {
                        System.err.println("❌ Failed to send email to: " + email + ". Error: " + ex.getMessage());
                    }
                } else {
                    System.err.println("⚠️ Invalid email skipped: " + email);
                }

                // Send SMS if phone number is provided
                if (phone != null && !phone.isBlank()) {
                    String formattedPhone = formatPhoneNumberToInternational(phone);
                    try {
                        smsService.sendSms(formattedPhone, program);
                    } catch (Exception ex) {
                        System.err.println("❌ Failed to send SMS to: " + phone + ". Error: " + ex.getMessage());
                    }
                } else {
                    System.err.println("⚠️ No phone number for member: " + member.getFamily());
                }
            });

            return saved;
        }
        private String formatPhoneNumberToInternational(String phone) {
            if (phone.startsWith("0") && phone.length() == 11) {
                return "234" + phone.substring(1);
            }
            return phone;
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
                String emailMessage = programUtil.buildProgramUpdateNotification(program);
                String subject = " Program Updated: " + (program.getName() != null ? program.getName() : "No Name");

                // Notify all members
                memberRepository.findAll().forEach(member -> {
                    String email = member.getEmail();
                    String phone = member.getPhoneNumber();

                    // Send Email if valid
                    if (isValidEmail(email)) {
                        try {
                            mailService.sendEmail(email, subject, emailMessage);
                        } catch (Exception ex) {
                            System.err.println("❌ Failed to send email to: " + email + ". Error: " + ex.getMessage());
                        }
                    } else {
                        System.err.println("⚠️ Invalid email skipped: " + email);
                    }

                    // Send SMS if phone number is provided
                    if (phone != null && !phone.isBlank()) {
                        String formattedPhone = formatPhoneNumberToInternational(phone);
                        try {
                            smsService.sendSmsUpdate(formattedPhone, program);
                        } catch (Exception ex) {
                            System.err.println("❌ Failed to send SMS to: " + phone + ". Error: " + ex.getMessage());
                        }
                    } else {
                        System.err.println("⚠️ No phone number for member: " + member.getFamily());
                    }
                });

                return savedProgram;
            }
        }

