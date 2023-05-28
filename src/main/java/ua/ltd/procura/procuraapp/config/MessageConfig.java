package ua.ltd.procura.procuraapp.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import ua.ltd.procura.procuraapp.service.AppLocaleService;

import java.util.Locale;

@Configuration
@RequiredArgsConstructor
public class MessageConfig implements WebMvcConfigurer {

    private final AppLocaleService appLocaleService;

    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver clr = new CookieLocaleResolver();
        Locale defaultLocale = new Locale(appLocaleService.findAll().keySet().stream().findFirst().orElseThrow());
        clr.setDefaultLocale(defaultLocale);
        return clr;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException {
                String newLocale = request.getParameter(getParamName());
                if (!appLocaleService.findAll().containsKey(newLocale)) {
                    return true;
                }
                super.preHandle(request, response, handler);
                return true;
            }
        };
        localeChangeInterceptor.setParamName("lang");
        return localeChangeInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

}