package ua.ltd.procura.procuraapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.ltd.procura.procuraapp.entity.AppLocale;
import ua.ltd.procura.procuraapp.entity.MessageHolder;

@Repository
public interface AppLocaleRepository extends JpaRepository<AppLocale, Long> {
    AppLocale findByName(String name);

}
