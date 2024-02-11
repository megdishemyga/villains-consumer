package com.gotham.villains.infra.database.crime.entity;

import lombok.experimental.FieldNameConstants;

@FieldNameConstants
public record CrimeId(String location, String villainNickName) {
}