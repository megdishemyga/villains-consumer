package com.gotham.villains.domain.crime.model;

public record Crime(
        String location,
        String villainNickName,
        CrimeStatus criminalActivityStatus
) {
}
