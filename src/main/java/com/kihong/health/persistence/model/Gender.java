package com.kihong.health.persistence.model;

public enum Gender {
  Male("Male"),
  Female("Female");

  private String genderName;

  Gender(String genderName) {this.genderName = genderName;}
  public String getGenderName() {return this.genderName;}
}
