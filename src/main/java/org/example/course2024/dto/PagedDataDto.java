package org.example.course2024.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Data
public class PagedDataDto<T> {
    private List<T> data;
    private int page;
    private int pageSize;
    private long total;
    private int totalPages;
}
