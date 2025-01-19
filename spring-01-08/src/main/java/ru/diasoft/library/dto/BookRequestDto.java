package ru.diasoft.library.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookRequestDto {
    private long id;
    private String title;
    private String authorFullName;
    private String genre;
}
