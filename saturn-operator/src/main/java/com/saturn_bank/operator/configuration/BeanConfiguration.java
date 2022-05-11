package com.saturn_bank.operator.configuration;

import org.passay.CharacterCharacteristicsRule;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.EnglishSequenceData;
import org.passay.IllegalSequenceRule;
import org.passay.LengthRule;
import org.passay.MessageResolver;
import org.passay.PasswordValidator;
import org.passay.RepeatCharactersRule;
import org.passay.spring.SpringMessageResolver;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import java.util.List;
import java.util.Locale;

@Configuration
public class BeanConfiguration {

    private static final int NUM_OF_CHARACTERISTICS = 3;
    private static final int SEQUENCE_LENGTH = 5;
    private static final int MIN_PASS_LEN = 8;
    private static final int MAX_PASS_LEN = 16;

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PasswordValidator passayPasswordValidator() {
        MessageResolver resolver = new SpringMessageResolver(messageSource());

        return new PasswordValidator(resolver, List.of(
                new LengthRule(MIN_PASS_LEN, MAX_PASS_LEN),
                new CharacterCharacteristicsRule(NUM_OF_CHARACTERISTICS,
                        new CharacterRule(EnglishCharacterData.UpperCase),
                        new CharacterRule(EnglishCharacterData.LowerCase),
                        new CharacterRule(EnglishCharacterData.Digit),
                        new CharacterRule(EnglishCharacterData.Special)
                ),
                new IllegalSequenceRule(EnglishSequenceData.Alphabetical, SEQUENCE_LENGTH, false),
                new IllegalSequenceRule(EnglishSequenceData.Numerical, SEQUENCE_LENGTH, false),
                new IllegalSequenceRule(EnglishSequenceData.USQwerty, SEQUENCE_LENGTH, false),
                new RepeatCharactersRule(SEQUENCE_LENGTH)
        ));
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource res = new ReloadableResourceBundleMessageSource();
        res.setDefaultEncoding("UTF-8");
        res.setFallbackToSystemLocale(false);
        res.setBasename("classpath:messages");

        return res;
    }

    @Bean
    public LocaleResolver localeResolver(){
        CookieLocaleResolver resolver = new CookieLocaleResolver();
        resolver.setDefaultLocale(new Locale("en"));

        return resolver;
    }

    @Bean
    public LocalValidatorFactoryBean localValidatorFactoryBean() {
        LocalValidatorFactoryBean validatorFactory = new LocalValidatorFactoryBean();
        validatorFactory.setValidationMessageSource(messageSource());

        return validatorFactory;
    }

}
