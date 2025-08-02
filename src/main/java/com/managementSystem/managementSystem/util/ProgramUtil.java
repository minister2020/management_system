package com.managementSystem.managementSystem.util;

import com.managementSystem.managementSystem.model.Program;
import org.springframework.stereotype.Component;

@Component
public class ProgramUtil {

    public String buildProgramNotification(Program program) {
        StringBuilder message = new StringBuilder("A new program has been added:\n\n");

        message.append("Name: ")
                .append(program.getName() != null ? program.getName() : "No Name")
                .append("\n");

        message.append("Description: ")
                .append(program.getDescription() != null ? program.getDescription() : "No Description")
                .append("\n");

        message.append("Date: ")
                .append(program.getProgramDate() != null ? program.getProgramDate().toString() : "No Date")
                .append("\n");

        message.append("Time: ")
                .append(program.getProgramTime() != null ? program.getProgramTime().toString() : "No Time")
                .append("\n");

        message.append("Venue: ")
                .append(program.getVenue() != null ? program.getVenue() : "No Venue");

        return message.toString();
    }

    public String buildProgramUpdateNotification(Program program) {
        StringBuilder message = new StringBuilder("A new program has been updated:\n\n");

        message.append("Name: ")
                .append(program.getName() != null ? program.getName() : "No Name")
                .append("\n");

        message.append("Description: ")
                .append(program.getDescription() != null ? program.getDescription() : "No Description")
                .append("\n");

        message.append("Date: ")
                .append(program.getProgramDate() != null ? program.getProgramDate().toString() : "No Date")
                .append("\n");

        message.append("Time: ")
                .append(program.getProgramTime() != null ? program.getProgramTime().toString() : "No Time")
                .append("\n");

        message.append("Venue: ")
                .append(program.getVenue() != null ? program.getVenue() : "No Venue");

        return message.toString();
    }


}
