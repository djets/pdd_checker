package ru.djets.pdd_checker.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.djets.pdd_checker.domain.Answer;
import ru.djets.pdd_checker.rest.dto.AnswerDto;
import ru.djets.pdd_checker.rest.dto.mappers.AnswerDtoMapper;
import ru.djets.pdd_checker.repositories.AnswerJpaRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Transactional
public class AnswerServiceImpl implements AnswerService {
    AnswerDtoMapper answerDtoMapper;
    AnswerJpaRepository repository;
    @Override
    @Transactional
    public AnswerDto save(Answer answer) {
        return answerDtoMapper.toDto(repository.save(answer));
    }

    @Override
    @Transactional
    public AnswerDto getById(Long id) {
        return answerDtoMapper.toDto(repository.findById(id).orElseThrow(RuntimeException::new));
    }

    @Override
    @Transactional
    public List<AnswerDto> getAll() {
        return repository.findAll().stream()
                .map(answerDtoMapper::toDto)
                .collect(Collectors.toList());
    }
}
