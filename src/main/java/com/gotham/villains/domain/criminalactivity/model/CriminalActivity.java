package com.gotham.villains.domain.criminalactivity.model;

public record CriminalActivity(
        String location,
        String villainNickName,
        CriminalActivityStatus criminalActivityStatus
) {
}
