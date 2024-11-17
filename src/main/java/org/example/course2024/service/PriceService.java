package org.example.course2024.service;

import org.example.course2024.dto.PriceCreationDto;
import org.example.course2024.dto.PriceDto;
import org.example.course2024.entity.Master;
import org.example.course2024.repository.MasterRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.example.course2024.entity.Price;
import org.example.course2024.exception.NotFoundException;
import org.example.course2024.mapper.PriceMapper;
import org.example.course2024.repository.PriceRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class PriceService {
    private final PriceRepository priceRepository;
    private final PriceMapper priceMapper;
    private final MasterRepository masterRepository;

    @Transactional(readOnly = true)
    public PriceDto getById(long id) {
        return priceMapper.toDto(priceRepository.findById(id).orElseThrow(() -> new NotFoundException("Price not found")));
    }

    @Transactional(readOnly = true)
    public List<PriceDto> getAll() {
        return priceRepository.findAll().stream().map(priceMapper::toDto).collect(Collectors.toList());
    }

    public PriceDto create(PriceCreationDto priceDto) {
        Master master = masterRepository.findById(priceDto.masterId())
                .orElseThrow(() -> new NotFoundException("Master not found"));

        Price price = priceMapper.toEntity(priceDto);
        price.setMaster(master);
        return priceMapper.toDto(priceRepository.save(price));
    }

    public PriceDto update(Long id, PriceDto priceDto) {
        Price price = priceRepository.findById(id).orElseThrow(() -> new NotFoundException("Price not found"));
        price.setPrice(priceDto.price());
        price.setSize(priceDto.size());
        Master master = masterRepository.findById(priceDto.masterId()).orElseThrow(() -> new NotFoundException("Master not found"));
        price.setMaster(master);
        return priceMapper.toDto(priceRepository.save(price));
    }

    public void delete(Long id) {
        priceRepository.deleteById(id);
    }

    public List<PriceDto> getPricesInRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return priceRepository.findByPriceRange(minPrice, maxPrice).stream()
                .map(priceMapper::toDto)
                .collect(Collectors.toList());
    }
}