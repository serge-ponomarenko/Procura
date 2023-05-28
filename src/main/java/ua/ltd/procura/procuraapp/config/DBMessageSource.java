package ua.ltd.procura.procuraapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.stereotype.Component;
import ua.ltd.procura.procuraapp.constants.GlobalConstants;
import ua.ltd.procura.procuraapp.entity.MessageHolder;
import ua.ltd.procura.procuraapp.repository.AppLocaleRepository;
import ua.ltd.procura.procuraapp.repository.MessageHolderRepository;

import java.text.MessageFormat;
import java.util.Locale;

@Component("messageSource")
public class DBMessageSource extends AbstractMessageSource {

    @Autowired
    private MessageHolderRepository messageHolderRepository;
    @Autowired
    private AppLocaleRepository appLocaleRepository;

    @Override
    protected MessageFormat resolveCode(String key, Locale locale) {
        MessageHolder message = messageHolderRepository.findByKeyAndLocale(key, appLocaleRepository.findByName(locale.getLanguage()));

        if (message == null) {
            message = messageHolderRepository.findByKeyAndLocale(key,
                    appLocaleRepository.findByName(GlobalConstants.DEFAULT_LOCALE_CODE));
        }
        if (message == null) {
            return new MessageFormat("??" + locale + ":" + key + "??", locale);
        }
        return new MessageFormat(message.getMessage(), locale);
    }
}
