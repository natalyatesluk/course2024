package org.example.course2024.service;

import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.example.course2024.dto.MasterDto;
import org.example.course2024.entity.Master;
import org.example.course2024.exception.NotFoundException;
import org.example.course2024.mapper.MasterMapper;
import org.example.course2024.repository.MasterRepository;
import org.springframework.stereotype.Service;

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
                orElseThrow(()->new NotFoundException("Master not found"));
        return masterMapper.toDto(master);
    }

    public MasterDto create(MasterDto master){

        return masterMapper.toDto(masterRepository.save(masterMapper.toEntity(master)));
    }

    @Transactional(readOnly = true)
    public List<MasterDto> getAll(){
        return masterRepository.findAll().stream()
                .map(masterMapper::toDto)
                .collect(Collectors.toList());
    }

    public MasterDto update(Long id, MasterDto masterDto){
         Master master = masterRepository.findById(id).
                 orElseThrow(()->new NotFoundException("Master not found"));
        master.setName(masterDto.name());
        master.setSurname(masterDto.surname());
        master.setMiddleName(masterDto.middleName());
        master.setPhone(masterDto.phone());
        master.setDoneWork(masterDto.doneWork());
        return masterMapper.toDto(masterRepository.save(master));
    }

    public void delete(Long id){
        masterRepository.deleteById(id);
    }
}
