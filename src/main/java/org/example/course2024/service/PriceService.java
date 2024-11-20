package org.example.course2024.service;

import org.example.course2024.dto.*;
import org.example.course2024.entity.Master;
import org.example.course2024.repository.MasterRepository;
import org.springframework.data.domain.*;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.example.course2024.entity.Price;
import org.example.course2024.exception.NotFoundException;
import org.example.course2024.mapper.PriceMapper;
import org.example.course2024.repository.PriceRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.channels.Pipe;
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
    public PagedDataDto<PriceDto> getAll(PageRequest pageRequest) {
        Page<Price> pricePage =priceRepository.findAll(pageRequest);
        List<Price> priceList = pricePage.getContent();

        List<PriceDto> content = priceList.stream().map(priceMapper::toDto).collect(Collectors.toList());
        PagedDataDto<PriceDto> pagedDataDto =  new PagedDataDto<>();
        pagedDataDto.setData(content);
        pagedDataDto.setTotal(pricePage.getTotalElements());
        pagedDataDto.setPage(pageRequest.getPageNumber());
        pagedDataDto.setPageSize(pageRequest.getPageSize());
        pagedDataDto.setTotalPages(pricePage.getTotalPages());
        return pagedDataDto;
    }

    public PriceDto create(PriceCreationDto priceDto) {
        Master master = masterRepository.findById(priceDto.masterId())
                .orElseThrow(() -> new NotFoundException("Master not found"));

        Price price = priceMapper.toEntity(priceDto);
        price.setMaster(master);
        return priceMapper.toDto(priceRepository.save(price));
    }

    public PriceDto update(Long id, PriceUpdatingDto priceDto) {
        Price price = priceRepository.findById(id).orElseThrow(() -> new NotFoundException("Price not found"));
        if(priceDto.id()!=null)
        {
            price.setId(id);
        }
        if(priceDto.masterId()!=null)
        {
            Master master = masterRepository.findById(priceDto.masterId()).orElseThrow(() -> new NotFoundException("Master not found"));
            price.setMaster(master);
        }
        if(priceDto.price()!=null){
            price.setPrice(priceDto.price());
        }
        if(priceDto.size()!=null){
            price.setSize(priceDto.size());
        }
        return priceMapper.toDto(priceRepository.save(price));
    }

    public void delete(Long id) {
        priceRepository.deleteById(id);
    }

    public PagedDataDto<PriceDto> getPricesInRange(BigDecimal minPrice, BigDecimal maxPrice, int page, int size, boolean asc) {
        Sort sort = Sort.by(asc ? Sort.Direction.ASC : Sort.Direction.DESC, "price");
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Price> pricePage = priceRepository.findByPriceRange(minPrice, maxPrice, pageable);
        List<PriceDto> prices = pricePage.getContent()
                .stream()
                .map(priceMapper::toDto)
                .collect(Collectors.toList());
        PagedDataDto<PriceDto> allPagePrice= new PagedDataDto<>();
        allPagePrice.setData(prices);
        allPagePrice.setTotal(pricePage.getTotalElements());
        allPagePrice.setPage(pricePage.getNumber());
        allPagePrice.setPageSize(pricePage.getSize());
        allPagePrice.setTotalPages(pricePage.getTotalPages());
        return allPagePrice;
    }

    public PagedDataDto<PriceDto> search(String keyword, PageRequest pageRequest) {
        Pageable pageable = pageRequest;
        List<Price> filteredPrice = priceRepository.findAllByPage(pageRequest).stream()
                .filter(price ->
                        (price.getSize() != null && price.getSize().contains(keyword)) ||
                                (price.getMaster().getSurname() != null && price.getMaster().getSurname().toLowerCase().contains(keyword.toLowerCase())) ||
                                (price.getMaster().getName() != null && price.getMaster().getName().toLowerCase().contains(keyword.toLowerCase()))
                ).collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredPrice.size());
        List<Price> pageContent = filteredPrice.subList(start, end);
        Page<Price> masters = new PageImpl<>(pageContent, pageable, filteredPrice.size());
        List<PriceDto> content =  filteredPrice.stream().map(master -> priceMapper.toDto(master)).collect(Collectors.toList());

        PagedDataDto<PriceDto> pageMasterFilter = new PagedDataDto<>();
        pageMasterFilter.setData(content);
        pageMasterFilter.setPage(masters.getNumber());
        pageMasterFilter.setPageSize(masters.getSize());
        pageMasterFilter.setTotal(masters.getTotalElements());
        pageMasterFilter.setTotalPages(masters.getTotalPages());

        return pageMasterFilter;

    }
}