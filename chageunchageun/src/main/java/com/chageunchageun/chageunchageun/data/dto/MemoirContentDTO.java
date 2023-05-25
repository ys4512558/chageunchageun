package com.chageunchageun.chageunchageun.data.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemoirContentDTO {
    String email;

    LocalDate date;

    String title;

    String mood;

    String comment;
}