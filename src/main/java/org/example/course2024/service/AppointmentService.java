package org.example.course2024.service;

import lombok.AllArgsConstructor;
import org.example.course2024.dto.*;
import org.example.course2024.entity.*;
import org.example.course2024.enums.StatusAppoint;
import org.example.course2024.enums.StatusTime;
import org.example.course2024.exception.MasterNotSchedule;
import org.example.course2024.exception.MasterNotThisPrice;
import org.example.course2024.exception.NotFoundException;
import org.example.course2024.exception.ScheduleAlreadyBusyException;
import org.example.course2024.mapper.AppointmentMapper;
import org.example.course2024.repository.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final CustomerRepository customerRepository;
    private final MasterRepository masterRepository;
    private final ScheduleRepository scheduleRepository;
    private final PriceRepository priceRepository;
    private final EmailService emailService;

    @Transactional(readOnly = true)
    public PagedDataDto<AppointmentDto> getAll(PageRequest pageRequest) {

        Page<Appointment> appointments = appointmentRepository.findAll(pageRequest);
        List<Appointment> appointmentList = appointments.getContent();

        List<AppointmentDto> appointmentDtoList = appointmentList.stream().map(appointment -> appointmentMapper.toDto(appointment)).collect(Collectors.toList());
        return new PagedDataDto<>(appointmentDtoList, appointments.getNumber(), appointments.getSize(), appointments.getTotalElements(), appointments.getTotalPages());
    }

    @Transactional(readOnly = true)
    public AppointmentDto getById(Long id) {
        return appointmentMapper.
                toDto(appointmentRepository.findById(id).orElseThrow(() -> new NotFoundException("Appointment not found")));
    }

    public AppointmentDto create(AppointmentCreationDto appointmentDto) {
        Customer customer = customerRepository.findById(appointmentDto.customerId()).orElseThrow(() -> new NotFoundException("Customer not found"));
        Master master = masterRepository.findById(appointmentDto.masterId()).orElseThrow(() -> new NotFoundException("Master not found"));
        Schedule schedule = scheduleRepository.findById(appointmentDto.scheduleId()).orElseThrow(() -> new NotFoundException("Schedule not found"));
        Price price = priceRepository.findById(appointmentDto.priceId()).orElseThrow(() -> new NotFoundException("Price not found"));
        Appointment appointment = appointmentMapper.toEntity(appointmentDto);

        if (schedule.getStatus() == StatusTime.BUSY) {
            throw new ScheduleAlreadyBusyException("The selected schedule is already marked as busy.");
        }
        if (!Objects.equals(schedule.getMaster().getId(), appointment.getMaster().getId())) {
            throw new MasterNotSchedule("This master doesn't match this record");
        }
        if (!Objects.equals(price.getMaster().getId(), appointment.getMaster().getId())) {
            throw new MasterNotThisPrice("This master doesn't match this price");
        }

        appointment.setCustomer(customer);
        appointment.setMaster(master);
        schedule.setStatus(StatusTime.BUSY);
        scheduleRepository.save(schedule);
        appointment.setSchedule(schedule);
        appointment.setPrice(price);
        appointmentRepository.save(appointment);

        String subject = "Your tattoo inscription";
        String body = String.format(
                "Hello %s,\n\n" +
                        "Your appointment with Master %s is scheduled for %s at %s.\n" +
                        "Price: %s " + "(This is a base price, not a full.)\n The full price will depend on the complexity of the work. "+
                        "Thank you for choosing our service!",
                customer.getName(), master.getName(),
                appointment.getSchedule().getDate().toString(),
                appointment.getSchedule().getTime().toString(),
                price.getPrice());

        String customerEmail = customer.getEmail();

        if (customerEmail == null || customerEmail.isEmpty()) {
            throw new NotFoundException("Customer email not found");
        }

        emailService.sendEmail(customerEmail, subject, body);
        return appointmentMapper.toDto(appointment);
    }

    public AppointmentDto update(AppointmentUpdatingDto appointmentDto, Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Appointment not found"));

        if (appointmentDto.id() != null) {
            appointment.setId(appointmentDto.id());
        }

        if (appointmentDto.masterId() != null) {
            Master master = masterRepository.findById(appointmentDto.masterId())
                    .orElseThrow(() -> new NotFoundException("Master not found"));
            appointment.setMaster(master);
        }

        if (appointmentDto.customerId() != null) {
            Customer customer = customerRepository.findById(appointmentDto.customerId())
                    .orElseThrow(() -> new NotFoundException("Customer not found"));
            appointment.setCustomer(customer);
        }

        if (appointmentDto.status() != null) {
            appointment.setStatus(appointmentDto.status());
            if (appointmentDto.status() == StatusAppoint.CANCELLED) {
                Schedule schedule = scheduleRepository.findById(appointment.getSchedule().getId())
                        .orElseThrow(() -> new NotFoundException("Schedule not found"));
                schedule.setStatus(StatusTime.FREE);
            }
        }

        if (appointmentDto.scheduleId() != null) {
            Schedule scheduleOld = scheduleRepository.findById(appointment.getSchedule().getId())
                    .orElseThrow(() -> new NotFoundException("Schedule not found"));
            Schedule scheduleNew = scheduleRepository.findById(appointmentDto.scheduleId())
                    .orElseThrow(() -> new NotFoundException("Schedule not found"));

            if (scheduleNew.getStatus() == StatusTime.BUSY) {
                throw new ScheduleAlreadyBusyException("The selected schedule is already marked as busy.");
            }
            if (!Objects.equals(scheduleNew.getMaster().getId(), appointment.getMaster().getId())) {
                throw new MasterNotSchedule("This master doesn't match this record");
            }
            scheduleOld.setStatus(StatusTime.FREE);
            scheduleRepository.save(scheduleOld);

            scheduleNew.setStatus(StatusTime.BUSY);
            scheduleRepository.save(scheduleNew);

            appointment.setSchedule(scheduleNew);
        }
        if (appointmentDto.localDate() != null) {
            Schedule schedule = scheduleRepository.findById(appointment.getSchedule().getId()).orElseThrow(() -> new NotFoundException("Schedule not found"));
            schedule.setDate(appointmentDto.localDate());
            scheduleRepository.save(schedule);
            appointment.setSchedule(schedule);
        }
        if (appointmentDto.localTime() != null) {
            Schedule schedule = scheduleRepository.findById(appointment.getSchedule().getId()).orElseThrow(() -> new NotFoundException("Schedule not found"));
            schedule.setTime(appointmentDto.localTime());
            scheduleRepository.save(schedule);
            appointment.setSchedule(schedule);
        }
        if (appointmentDto.priceId() != null) {
            Price newPrice = priceRepository.findById(appointmentDto.priceId()).
                    orElseThrow(() -> new NotFoundException("Price not found"));

            if (!Objects.equals(newPrice.getMaster().getId(), appointment.getMaster().getId())) {
                throw new MasterNotThisPrice("This master doesn't match this price");
            }

            appointment.setPrice(newPrice);
        }
        if (appointmentDto.pricePrice() != null) {
            Price price = priceRepository.findById(appointment.getPrice().getId())
                    .orElseThrow(() -> new NotFoundException("Price not found"));
            price.setPrice(appointmentDto.pricePrice());
            appointment.setPrice(price);
        }
        appointmentRepository.save(appointment);
        return appointmentMapper.toDto(appointment);
    }

    public void delete(Long id) {
        appointmentRepository.deleteById(id);
    }

    public Map<StatusAppoint, Long> getStatusCounts() {
        List<Appointment> appointments = appointmentRepository.findAll();
        return appointments.stream()
                .collect(Collectors.groupingBy(Appointment::getStatus, Collectors.counting()));
    }

    public PagedDataDto<AppointmentDto> search(String keyword, PageRequest pageRequest) {
        Pageable pageable = pageRequest;
        List<Appointment> filteredPrice = appointmentRepository.findAllByPage(pageRequest).stream()
                .filter(appointment ->
                        (appointment.getCustomer().getSurname() != null && appointment.getCustomer().getSurname().toLowerCase().contains(keyword.toLowerCase())) ||
                                (appointment.getMaster().getSurname() != null && appointment.getMaster().getSurname().toLowerCase().contains(keyword.toLowerCase())) ||
                                (appointment.getCustomer().getName() != null && appointment.getCustomer().getName().toLowerCase().contains(keyword.toLowerCase())) ||
                                (appointment.getMaster().getName() != null && appointment.getMaster().getName().toLowerCase().contains(keyword.toLowerCase())) ||
                                (appointment.getCustomer().getEmail() != null && appointment.getCustomer().getEmail().toLowerCase().contains(keyword.toLowerCase())) ||
                                (appointment.getCustomer().getPhone() != null && appointment.getCustomer().getPhone().contains(keyword)) ||
                                (appointment.getMaster().getEmail() != null && appointment.getMaster().getEmail().toLowerCase().contains(keyword.toLowerCase())) ||
                                (appointment.getMaster().getPhone() != null && appointment.getMaster().getPhone().contains(keyword)) ||
                                (String.valueOf(appointment.getCustomer().getId()).equals(keyword)) ||
                                (String.valueOf(appointment.getMaster().getId()).equals(keyword)) ||
                                (String.valueOf(appointment.getSchedule().getId()).equals(keyword)))
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredPrice.size());
        List<Appointment> pageContent = filteredPrice.subList(start, end);
        Page<Appointment> masters = new PageImpl<>(pageContent, pageable, filteredPrice.size());
        List<AppointmentDto> content = filteredPrice.stream().map(master -> appointmentMapper.toDto(master)).collect(Collectors.toList());

        PagedDataDto<AppointmentDto> appointmentDtoPagedDataDto = new PagedDataDto<>();
        appointmentDtoPagedDataDto.setData(content);
        appointmentDtoPagedDataDto.setPage(masters.getNumber());
        appointmentDtoPagedDataDto.setPageSize(masters.getSize());
        appointmentDtoPagedDataDto.setTotal(masters.getTotalElements());
        appointmentDtoPagedDataDto.setTotalPages(masters.getTotalPages());

        return appointmentDtoPagedDataDto;

    }

    @Transactional(readOnly = true)
    public PagedDataDto<AppointmentDto> getStatusList(String status, int page, int size, boolean asc) {
        Sort sort = Sort.by(asc ? Sort.Direction.ASC : Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);

        StatusAppoint statusAppoint = StatusAppoint.valueOf(status.toUpperCase());
        Page<Appointment> schedulesPage = appointmentRepository.findByStatus(statusAppoint, pageable);
        List<Appointment> appointmentList = schedulesPage.getContent();
        List<AppointmentDto> appointmentDtoList = appointmentList.stream().map(appointment -> appointmentMapper.toDto(appointment)).collect(Collectors.toUnmodifiableList());

        PagedDataDto<AppointmentDto> appointmentDtoPagedDataDto = new PagedDataDto<>();
        appointmentDtoPagedDataDto.setData(appointmentDtoList);
        appointmentDtoPagedDataDto.setPage(schedulesPage.getNumber());
        appointmentDtoPagedDataDto.setPageSize(schedulesPage.getSize());
        appointmentDtoPagedDataDto.setTotal(schedulesPage.getTotalElements());
        appointmentDtoPagedDataDto.setTotalPages(schedulesPage.getTotalPages());
        return appointmentDtoPagedDataDto;
    }
    public List<AppointmentDto> getByMaster(Long idMaster){

        List <Appointment> prices= appointmentRepository.findByMaster(idMaster);
        return prices.stream().map(appointmentMapper::toDto).collect(Collectors.toList());
    }
    public List<AppointmentDto> getByCustomer(Long idCustomer){

        List <Appointment> prices= appointmentRepository.findByCustomer(idCustomer);
        return prices.stream().map(appointmentMapper::toDto).collect(Collectors.toList());
    }
}

