package org.example.course2024.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagedDataDto<T> {
    private List<T> data;
    private int page;
    private int pageSize;
    private long total;
    private int totalPages;
}
