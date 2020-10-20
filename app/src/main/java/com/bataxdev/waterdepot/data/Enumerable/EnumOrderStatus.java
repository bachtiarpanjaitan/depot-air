package com.bataxdev.waterdepot.data.Enumerable;

public enum EnumOrderStatus {
    OPEN("OPEN",0),
    SENDING("SENDING",1),
    CLOSED("CLOSED",2);

    private String StatusName;
    private int StatusValue;

    EnumOrderStatus(String name, int i) {
        StatusValue = i;
        StatusName = name;
    }

    public int getValue(){ return StatusValue;}

    public String getName(){ return StatusName;}
}
