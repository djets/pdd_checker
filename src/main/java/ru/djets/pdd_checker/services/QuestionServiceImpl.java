package ru.djets.pdd_checker.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.djets.pdd_checker.domain.Question;
import ru.djets.pdd_checker.rest.dto.QuestionDto;
import ru.djets.pdd_checker.rest.dto.mappers.QuestionDtoMapper;
import ru.djets.pdd_checker.repositories.QuestionJpaRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Transactional
public class QuestionServiceImpl implements QuestionService {
    QuestionDtoMapper questionDtoMapper;
    QuestionJpaRepository repository;
    @Override
    @Transactional
    public QuestionDto save(Question question) {
        return questionDtoMapper.toDto(repository.save(question));
    }

    @Override
    @Transactional
    public QuestionDto getById(long id) {
        return questionDtoMapper.toDto(repository.findById(id).orElseThrow(RuntimeException::new));
    }

    @Override
    @Transactional
    public List<QuestionDto> getAll() {
        return repository.findAll().stream()
                .map(questionDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<QuestionDto> getAllByTicketNumber(int ticket) {
        return repository.findAllByTicket_TicketNumber(ticket).stream()
                .map(questionDtoMapper::toDto)
                .collect(Collectors.toList());
    }
}