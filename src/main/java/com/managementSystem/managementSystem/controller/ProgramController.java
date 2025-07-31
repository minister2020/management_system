package com.managementSystem.managementSystem.controller;

import com.managementSystem.managementSystem.model.Program;
import com.managementSystem.managementSystem.servive.ProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/program")
public class ProgramController {
   @Autowired
    private ProgramService programService;

   @PostMapping("/addProgram")
   public Program addProgram(@RequestBody Program program) {
       return  programService.addProgram(program);
   }

    @GetMapping("/allProgram")
   public List<Program> getAllPrograms() {
       return programService.getAllPrograms();
   }

    @GetMapping("/by-name")
    public List<Program> getProgramsByName(@RequestParam String name) {
        return programService.getProgramsByName(name);
    }

    @PutMapping("/{id}")
    public Program updateProgram(@PathVariable Long id, @RequestBody Program updatedProgram) {
        return programService.updateProgram(id, updatedProgram);
    }
}
