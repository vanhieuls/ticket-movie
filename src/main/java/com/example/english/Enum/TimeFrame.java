package com.example.english.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;

@AllArgsConstructor
@Getter
public enum TimeFrame {
    MORNING("MORNING", LocalTime.of(8,0), LocalTime.of(11,59), 1.0),
    NOON("NOON", LocalTime.of(12,0), LocalTime.of(16,59), 1.05),
    AFTERNOON("AFTERNOON", LocalTime.of(17,0), LocalTime.of(22,59), 1.1),
    EVENING("EVENING", LocalTime.of(23,0), LocalTime.of(2,59), 1.15);

    private final String timeFrame;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final double priceFactor;

    public boolean isInSlot (LocalTime time) {
        if(startTime.isBefore(endTime)){
            return !time.isBefore(startTime) && !time.isAfter(endTime);
        }
        // dành cho khung giờ qua đêm
        else return !time.isBefore(startTime) || !time.isAfter(endTime);
    }

    public static TimeFrame from (LocalTime time){
        for(TimeFrame s : values()){
            if(s.isInSlot(time)) return s;
        }
        return MORNING;
    }
    public static TimeFrame fromLabel (String label){
        for (TimeFrame tf : values()){
            if(tf.getTimeFrame().equalsIgnoreCase(label)){
                return tf;
            }
        }
        throw new IllegalArgumentException("Unknown time frame: " + label);
    }

}
