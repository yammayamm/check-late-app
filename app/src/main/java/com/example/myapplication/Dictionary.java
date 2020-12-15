package com.example.myapplication;

import java.io.Serializable;

public class Dictionary implements Serializable {
  private static final long serialVersionUID = 1L;
  private String time;
  private String Penalty;
  private String Name;

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public String getPenalty() {
    return Penalty;
  }

  public void setPenalty(String penalty) {
    Penalty = penalty;
  }

  public String getName() {
    return Name;
  }

  public void setName(String name) {
    Name = name;
  }

  public Dictionary(String time, String penalty, String name) {
    this.time = time;
    Penalty = penalty;
    Name = name;
  }
}