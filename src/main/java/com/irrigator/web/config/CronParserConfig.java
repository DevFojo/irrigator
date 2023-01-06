package com.irrigator.web.config;

import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.parser.CronParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import static com.cronutils.model.CronType.UNIX;

@Configuration
public class CronParserConfig {

    @Bean
    @Scope("singleton")
    public CronParser cronParser() {
        CronDefinition cronDefinition = CronDefinitionBuilder.instanceDefinitionFor(UNIX);
        return new CronParser(cronDefinition);
    }
}
