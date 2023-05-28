package ua.ltd.procura.procuraapp.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.ltd.procura.procuraapp.entity.AppLocale;
import ua.ltd.procura.procuraapp.repository.AppLocaleRepository;
import ua.ltd.procura.procuraapp.service.AppLocaleService;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppLocaleServiceImpl implements AppLocaleService {

    private final AppLocaleRepository appLocaleRepository;

    @Override
    public AppLocale findByName(String name) {
        return appLocaleRepository.findByName(name);
    }

    @Override
    public Map<String, AppLocale> findAll() {
        return appLocaleRepository.findAll()
                .stream()
                .collect(Collectors.toMap(AppLocale::getName, Function.identity()));
    }
}
