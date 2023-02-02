package ru.djets.pdd_checker.services;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.djets.pdd_checker.domain.Ticket;
import ru.djets.pdd_checker.rest.dto.TicketDto;
import ru.djets.pdd_checker.rest.dto.mappers.TicketDtoMapper;
import ru.djets.pdd_checker.repositories.TicketJpaRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    TicketDtoMapper ticketDtoMapper;
    TicketJpaRepository repository;
    @Override
    @Transactional
    public void save(Ticket ticket) {
        repository.save(ticket);
    }

    @Override
    @Transactional
    public TicketDto getById(@NotNull long id) {
        return ticketDtoMapper.toDto(repository.findById(id).orElseThrow(RuntimeException::new));
    }

    @Override
    @Transactional
    public List<TicketDto> getAll() {
        return repository.findAll()
                .stream()
                .map(ticketDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TicketDto getByTicketNumber(@NotNull int ticketNumber) {
        return ticketDtoMapper.toDto(repository
                .findByTicketNumber(ticketNumber).orElseThrow(RuntimeException::new));
    }
}
