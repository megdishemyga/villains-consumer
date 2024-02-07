package com.gotham.villains.infra.database.criminalactivity.entity;

import lombok.experimental.FieldNameConstants;

@FieldNameConstants
public record CriminalActivityKey(String location, String villainNickName) {
}