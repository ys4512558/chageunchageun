package com.chageunchageun.chageunchageun.controller;

import com.chageunchageun.chageunchageun.data.dto.Memoir.MemoirDTO;
import com.chageunchageun.chageunchageun.data.dto.Memoir.MemoirSaveDTO;
import com.chageunchageun.chageunchageun.data.dto.User.UserMemoirDTO;
import com.chageunchageun.chageunchageun.service.MemoirService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/memoir/")
public class MemoirController {
    @Autowired
    MemoirService memoirService;

    private final Logger LOGGER = LoggerFactory.getLogger(MemoirController.class);

    /**
     *루틴 완료 사진 저장 컨트롤러
     */
    @PostMapping(value = "save")
    public ResponseEntity<HttpStatus> saveMemoir(@RequestParam String email,
                                       @RequestParam String date,
                                       @RequestParam String itemName,
                                       @RequestParam MultipartFile image) throws IOException {

        MemoirSaveDTO memoirSaveDTO = new MemoirSaveDTO();

        memoirSaveDTO.setEmail(email);
        memoirSaveDTO.setDate(LocalDate.parse(date));
        memoirSaveDTO.setItemName(itemName);
        memoirSaveDTO.setImg(image);

        memoirService.saveMemoir(memoirSaveDTO);

        return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK);
    }

    /**
     * 회고록 코멘트 작성 컨트롤러
     */
    @PostMapping(value = "saveContent")
    public ResponseEntity<HttpStatus> saveMemoirComment(@RequestPart("memoirContent") String memoirContent,
                                                        @RequestPart("image") MultipartFile image){
        memoirService.saveContent(memoirContent, image);

        return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK);
    }


    /**
     *회고록 조회 컨트롤러
     */

    @GetMapping(value = "select")
    public ResponseEntity<MemoirDTO> selectMemoir(@RequestParam String email,
                                               @RequestParam LocalDate date){

        MemoirDTO memoirDTO = memoirService.selectMemoir(email, date);


        return ResponseEntity.status(HttpStatus.OK).body(memoirDTO);

    }

    /**
     * 회고록의 이미지를 보여주기 위한 url
     * @param response
     * @param email
     * @param memoirDate
     * @param img
     * @throws IOException
     */

    @GetMapping("img/{email}/{memoirDate}/{img}")
    public void get(HttpServletResponse response,
                    @PathVariable("email") String email,
                    @PathVariable("memoirDate") LocalDate memoirDate,
                    @PathVariable("img") String img) throws IOException {

        ServletOutputStream outputStream = response.getOutputStream();

        File file = memoirService.imgView(email, memoirDate, img);

        if (!file.exists()) {
            //해당 파일이 존재하지 않을 때 처리
        }
        FileInputStream inputStream = new FileInputStream(file);

        int length;
        byte[] buffer = new byte[1024];

        while ((length = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, length);
        }
    }


    @GetMapping(value = "Preview")
    public ResponseEntity<UserMemoirDTO> preview(@RequestParam String email){
        UserMemoirDTO userMemoirDTO = memoirService.previewMemoir(email);

        return ResponseEntity.status(HttpStatus.OK).body(userMemoirDTO);

    }
}
