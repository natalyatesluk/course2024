package org.example.course2024.service;

import org.example.course2024.dto.PagedDataDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.example.course2024.dto.MasterDto;
import org.example.course2024.entity.Master;
import org.example.course2024.exception.NotFoundException;
import org.example.course2024.mapper.MasterMapper;
import org.example.course2024.repository.MasterRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class MasterService {
    private final MasterMapper masterMapper;
    private final MasterRepository masterRepository;

    @Transactional(readOnly = true)
    public MasterDto getById(Long id) {
        Master master = masterRepository.findById(id).
                orElseThrow(() -> new NotFoundException("Master not found"));
        return masterMapper.toDto(master);
    }

    public MasterDto create(MasterDto master) {

        return masterMapper.toDto(masterRepository.save(masterMapper.toEntity(master)));
    }

    @Transactional(readOnly = true)
    public PagedDataDto<MasterDto> getAll(PageRequest pageRequest) {

        Page<Master> masters = masterRepository.findAll(pageRequest);
        List<Master> masterList = masters.getContent();

        List<MasterDto> data = masterList.stream().map(master -> masterMapper.toDto(master)).collect(Collectors.toList());
        PagedDataDto<MasterDto> pageMasterAll = new PagedDataDto<>();
        pageMasterAll.setData(data);
        pageMasterAll.setPage(masters.getNumber());
        pageMasterAll.setPageSize(masters.getSize());
        pageMasterAll.setTotal(masters.getTotalElements());
        pageMasterAll.setTotalPages(masters.getTotalPages());

        return pageMasterAll;
    }

    public MasterDto update(Long id, MasterDto masterDto) {
        Master master = masterRepository.findById(id).
                orElseThrow(() -> new NotFoundException("Master not found"));
        master.setName(masterDto.name());
        master.setSurname(masterDto.surname());
        master.setMiddleName(masterDto.middleName());
        master.setPhone(masterDto.phone());
        master.setDoneWork(masterDto.doneWork());
        return masterMapper.toDto(masterRepository.save(master));
    }

    public void delete(Long id) {
        masterRepository.deleteById(id);
    }

    public PagedDataDto<MasterDto> search(String keyword, PageRequest pageRequest) {
        Pageable pageable = pageRequest;
        List<Master> filteredMasters = masterRepository.findAllByPage(pageRequest).stream()
                .filter(customer ->
                        (customer.getPhone() != null && customer.getPhone().contains(keyword)) ||
                                (customer.getName() != null && customer.getName().toLowerCase().contains(keyword.toLowerCase())) ||
                                (customer.getSurname() != null && customer.getSurname().toLowerCase().contains(keyword.toLowerCase()))
                ).collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredMasters.size());
        List<Master> pageContent = filteredMasters.subList(start, end);
        Page<Master> masters = new PageImpl<>(pageContent, pageable, filteredMasters.size());
        List<MasterDto> content =  filteredMasters.stream().map(master -> masterMapper.toDto(master)).collect(Collectors.toList());

        PagedDataDto<MasterDto> pageMasterFilter = new PagedDataDto<>();
        pageMasterFilter.setData(content);
        pageMasterFilter.setPage(masters.getNumber());
        pageMasterFilter.setPageSize(masters.getSize());
        pageMasterFilter.setTotal(masters.getTotalElements());
        pageMasterFilter.setTotalPages(masters.getTotalPages());

        return pageMasterFilter;

    }

    public List<MasterDto> sorted(String keyword, boolean reverse) {
        List<Master> masters = masterRepository.findAll();

        List<Master> sortedMasters;
        if (keyword.equalsIgnoreCase("name")) {
            sortedMasters = masters.stream()
                    .sorted(reverse ? Comparator.comparing(Master::getName).reversed()
                            : Comparator.comparing(Master::getName))
                    .collect(Collectors.toList());
        } else if (keyword.equalsIgnoreCase("surname")) {
            sortedMasters = masters.stream()
                    .sorted(reverse ? Comparator.comparing(Master::getSurname).reversed()
                            : Comparator.comparing(Master::getSurname))
                    .collect(Collectors.toList());
        } else if (keyword.equalsIgnoreCase("phone")) {
            sortedMasters = masters.stream()
                    .sorted(reverse ? Comparator.comparing(Master::getPhone).reversed()
                            : Comparator.comparing(Master::getPhone))
                    .collect(Collectors.toList());
        } else {
            sortedMasters = masters;
        }

        return sortedMasters.stream()
                .map(master -> masterMapper.toDto(master))
                .collect(Collectors.toList());
    }
}
