package com.gotham.villains.config;


import com.gotham.villains.domain.crime.port.CrimeRepository;
import com.gotham.villains.domain.usecase.VillainDetectedUseCase;
import com.gotham.villains.domain.vilain.port.VillainRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfig {

    @Bean
    public VillainDetectedUseCase villainDetectedUseCase(final VillainRepository villainRepository, final CrimeRepository crimeRepository) {
        return new VillainDetectedUseCase(villainRepository, crimeRepository);
    }
}
