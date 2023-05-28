package ua.ltd.procura.procuraapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.ltd.procura.procuraapp.entity.AppLocale;
import ua.ltd.procura.procuraapp.entity.MessageHolder;

@Repository
public interface MessageHolderRepository extends JpaRepository<MessageHolder, Long> {
    MessageHolder findByKeyAndLocale(String key, AppLocale locale);
}
