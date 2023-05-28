package ua.ltd.procura.procuraapp.service;

import ua.ltd.procura.procuraapp.entity.AppLocale;

import java.util.Map;

public interface AppLocaleService {

    AppLocale findByName(String name);

    Map<String, AppLocale> findAll();

}
