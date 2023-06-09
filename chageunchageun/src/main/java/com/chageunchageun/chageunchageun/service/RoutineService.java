package com.chageunchageun.chageunchageun.service;

import com.chageunchageun.chageunchageun.data.dto.Routine.DeleteRoutineDTO;
import com.chageunchageun.chageunchageun.data.dto.Routine.UpdateRoutineDTO;
import com.chageunchageun.chageunchageun.data.entity.Routine;
import com.chageunchageun.chageunchageun.data.entity.User;
import com.chageunchageun.chageunchageun.data.dto.Routine.RoutineDTO;
import com.chageunchageun.chageunchageun.data.dto.Routine.RoutinesDTO;
import com.chageunchageun.chageunchageun.data.repository.RoutineRepository;
import com.chageunchageun.chageunchageun.data.repository.UserRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoutineService {

    private final UserRepository userRepository;

    private final RoutineRepository routineRepository;

    @Autowired
    public RoutineService(UserRepository userRepository, RoutineRepository routineRepository) {
        this.userRepository = userRepository;
        this.routineRepository = routineRepository;
    }


    /**
     * 루틴 저장
     * @param emailParam
     * @param routinesDTO
     */
    public void saveRoutineDTO(String emailParam, RoutinesDTO routinesDTO){
        String email = emailParam;
        String day = routinesDTO.getDay();
        User user = userRepository.getReferenceById(email);

        List<RoutineDTO> routineDTOS = routinesDTO.getRoutines();

        List<Routine> routines = new ArrayList<>();

        for(RoutineDTO routineDTO : routineDTOS){
            Routine routine = new Routine(user, day,
                    routineDTO.getItemName(),
                    routineDTO.getItemDisc(),
                    routineDTO.getStart(),
                    routineDTO.getEnd());

            routines.add(routine);
            routineRepository.save(routine);
        }
    }

    /**
     * DB에서 이메일, 요일로 검색
     * 해당 데이터 RoutineDTO에 저장
     * ArrayList<RoutineDTO>에 저장
     * RoutinesDTO에 이메일, 요일, List<RoutineDTO>저장
     * RoutinesDTO 리턴
     * @param emailParam
     * @param dayParam
     * @return
     */
    public RoutinesDTO selectRoutine(String emailParam, String dayParam){

        List<Routine> routines = routineRepository.findByUserEmailAndDay(emailParam, dayParam);

        List<RoutineDTO> routineDTOS = new ArrayList<RoutineDTO>();

        for (Routine routine : routines) {

            RoutineDTO routineDTO = new RoutineDTO();
            routineDTO.setItemDisc(routine.getItemDisc());
            routineDTO.setItemName(routine.getItemName());
            routineDTO.setEnd(routine.getEnd());
            routineDTO.setStart(routine.getStart());

            routineDTOS.add(routineDTO);
        }

        RoutinesDTO routinesDTO = new RoutinesDTO();
        routinesDTO.setRoutines(routineDTOS);
        routinesDTO.setDay(dayParam);

        return routinesDTO;
    }

    /**
     * 이메일, 수정전 이름, 설명, 요일을 받아서
     * 이름, 설명, 시작시간, 종료시간을 업데이트
     * @param updateRoutine
     */
    public void updateRoutine(String email, UpdateRoutineDTO updateRoutine) {

        User user = userRepository.getReferenceById(email);

        String item_name = updateRoutine.getItem_name();
        String item_disc = updateRoutine.getItem_disc();
        String day = updateRoutine.getDay();

        Optional<Routine> routineOptionaline = routineRepository.findTopByUserEmailAndItemNameAndItemDiscAndDay(user.getEmail(), item_name, item_disc, day);
        Routine routine = new Routine();

        if(routineOptionaline.isPresent()){
            routine = routineOptionaline.get();

            routine.setItemName(updateRoutine.getItem_nameRp());
            routine.setItemDisc(updateRoutine.getItem_discRp());
            routine.setStart(updateRoutine.getStartRp());
            routine.setEnd(updateRoutine.getEndRp());

            routineRepository.save(routine);
        }
    }

    /**
     * 루틴 삭제
     * @param email
     * @param deleteRoutineDTO
     */
    public void deleteRoutine(String email, DeleteRoutineDTO deleteRoutineDTO){

        User user = userRepository.getReferenceById(email);

        String item_name = deleteRoutineDTO.getItem_name();
        String item_disc = deleteRoutineDTO.getItem_disc();
        String day = deleteRoutineDTO.getDay();

        Optional<Routine> routineOptional = routineRepository.findTopByUserEmailAndItemNameAndItemDiscAndDay(user.getEmail(),
                item_name, item_disc, day);

        if (routineOptional.isPresent()) {
            Routine routine = routineOptional.get();

            routineRepository.delete(routine);
        }
    }


    /**
     * DTO를 사용하지 않을 경우
     * 문자열 routines를 @RequestBody에서 받음
     * Json으로 파싱 후 DB에 저장
     * @param routines
     */
    public void saveJsonRoutine(String routines){
        System.out.println(routines);
        JSONParser parser = new JSONParser();

        String email = null;
        String day = null;
        JSONArray routineArray = null;

        try {
            JSONObject jsonObject = (JSONObject) parser.parse(routines);
            email = (String) jsonObject.get("email");
            day = (String) jsonObject.get("day");
            routineArray = (JSONArray) jsonObject.get("Routine");

        } catch (ParseException e) {
            e.printStackTrace();
        }

        User user = userRepository.getReferenceById(email);

        user.setRoutines(parseJsonArray(routineArray));

        for(Routine routine : user.getRoutines()){
            routine.setDay(day);
            routine.setUser(user);
            routineRepository.save(routine);
        }

    }

    /**
     * JSONArray를 엔티티 List로 변환(파싱) 후 반환
     * @param routineArray
     * @return
     */
    public List<Routine> parseJsonArray(JSONArray routineArray){

        //JSONArray에 들어갈 내용들
        String itemName;
        String itemDisc;
        String start;
        String end;

        List<Routine> list = new ArrayList<Routine>();
        for(Object object : routineArray){
            JSONObject routineObject = (JSONObject) object;

            itemName = (String) routineObject.get("itemName");
            itemDisc = (String) routineObject.get("itemDisc");
            start = (String) routineObject.get("start");
            end = (String) routineObject.get("end");

            Routine routine = new Routine();
            routine.setItemName(itemName);
            routine.setItemDisc(itemDisc);
            routine.setStart(start);
            routine.setEnd(end);

            list.add(routine);
        }
        return list;
    }

    /**
     * DTO사용하지 않을 경우
     * 이메일, 수정전 이름, 설명, 요일을 받아서
     * 이름, 설명, 시작시간, 종료시간을 업데이트
     * @param updateRoutine
     */
    public void updateJsonRoutine(String email, String updateRoutine) {

        JSONParser parser = new JSONParser();

        //String email;
        String item_name;
        String item_disc;
        String day;

        String item_nameRp;
        String item_discRp;
        String startRp;
        String endRp;

        try {
            JSONObject jsonObject = (JSONObject) parser.parse(updateRoutine);

            //email = (String) jsonObject.get("email");
            item_name = (String) jsonObject.get("item_name");
            item_disc = (String) jsonObject.get("item_disc");
            day = (String) jsonObject.get("day");
            item_nameRp = (String) jsonObject.get("item_nameRp");
            item_discRp = (String) jsonObject.get("item_discRp");
            startRp = (String) jsonObject.get("startRp");
            endRp = (String) jsonObject.get("endRp");


        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        Optional<Routine> routineOptionaline = routineRepository.findTopByUserEmailAndItemNameAndItemDiscAndDay(email, item_name, item_disc, day);

        Routine routine = new Routine();

        if(routineOptionaline.isPresent()){
            routine = routineOptionaline.get();

            routine.setItemName(item_nameRp);
            routine.setItemDisc(item_discRp);
            routine.setStart(startRp);
            routine.setEnd(endRp);

            routineRepository.save(routine);
        }
    }
}
