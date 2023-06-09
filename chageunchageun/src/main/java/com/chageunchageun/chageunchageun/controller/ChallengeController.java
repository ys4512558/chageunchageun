package com.chageunchageun.chageunchageun.controller;


import com.chageunchageun.chageunchageun.data.dto.Challenge.ChallengeDTO;
import com.chageunchageun.chageunchageun.service.ChallengeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/challenge/")
public class ChallengeController {

    @Autowired
    ChallengeService challengeService;
    @PostMapping (value = "save")
    public ResponseEntity<HttpStatus> saveChallenge(@RequestParam String email,
                                                    @RequestParam String title){

        challengeService.saveChallenge(email, title);
        return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK);
    }

    @GetMapping(value = "incomplete")
    public ResponseEntity<List<ChallengeDTO>> incompleteChallenge(@RequestParam String email){
        List<ChallengeDTO> challengeDTOList = challengeService.incompleteChallenge(email);

        return ResponseEntity.status(HttpStatus.OK).body(challengeDTOList);
    }

    @PatchMapping(value = "update/{email}/{title}")
    public ResponseEntity<HttpStatus> updateCompleteDate(@PathVariable("email") String email,
                                                         @PathVariable("title") String title){

        challengeService.ChallengeUpdate(email, title);

        return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK);
    }

    @GetMapping(value = "complete")
    public ResponseEntity<List<ChallengeDTO>> completeChallenge(@RequestParam String email){
        List<ChallengeDTO> challengeDTOList = challengeService.completeChallenge(email);

        return ResponseEntity.status(HttpStatus.OK).body(challengeDTOList);
    }

    @DeleteMapping(value = "delete/{email}/{title}")
    public ResponseEntity<HttpStatus> deleteChallenge(@PathVariable String email,
                                                      @PathVariable String title){

        challengeService.DeleteChallenge(email, title);

        return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK);
    }
}
