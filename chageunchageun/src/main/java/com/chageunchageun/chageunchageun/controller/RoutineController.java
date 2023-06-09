package com.chageunchageun.chageunchageun.controller;


import com.chageunchageun.chageunchageun.data.dto.Routine.DeleteRoutineDTO;
import com.chageunchageun.chageunchageun.data.dto.Routine.RoutinesDTO;
import com.chageunchageun.chageunchageun.data.dto.Routine.UpdateRoutineDTO;
import com.chageunchageun.chageunchageun.service.RoutineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/routine/")
public class RoutineController {
    @Autowired
    RoutineService routineService;

    /**
     * 루틴 저장
     * @param routinesDTO
     * @return
     */
    @PostMapping(value = "save/{email}")
    public ResponseEntity<HttpStatus> saveRoutinesDTO(@RequestBody RoutinesDTO routinesDTO, @PathVariable("email") String email){

        routineService.saveRoutineDTO(email, routinesDTO);

        return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK);
    }


    /**
     * 루틴 불러오기
     * @param email
     * @param day
     * @return
     */
    @GetMapping(value = "select")
    public ResponseEntity<RoutinesDTO> selectRoutines(
            @RequestParam String email,
            @RequestParam String day){

        RoutinesDTO routinesDTO = routineService.selectRoutine(email, day);

        return ResponseEntity.status(HttpStatus.OK).body(routinesDTO);
    }

    /**
     * 루틴 수정
     * @param email
     * @param updateRoutine
     */
    @PatchMapping(value = "update/{email}")
    public void updateRoutine(@PathVariable String email,
                              @RequestBody UpdateRoutineDTO updateRoutine){

        routineService.updateRoutine(email, updateRoutine);
    }

    /**
     * 루틴 삭제
     * @param email
     * @param deleteRoutineDTO
     * @return
     */
    @DeleteMapping(value = "delete/{email}")
    public ResponseEntity<HttpStatus> deleteRoutine(@PathVariable String email,
                                                    @RequestBody DeleteRoutineDTO deleteRoutineDTO){

        routineService.deleteRoutine(email, deleteRoutineDTO);

        return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK);
    }

}
