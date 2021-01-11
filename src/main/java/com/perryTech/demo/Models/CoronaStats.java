package com.perryTech.demo.Models;

import lombok.Data;

@Data
public class CoronaStats {
    private String province;
    private String country;
    private int newTotalCases;
    private int differenceFromPreviousDay;

    public CoronaStats() {
    }
}
