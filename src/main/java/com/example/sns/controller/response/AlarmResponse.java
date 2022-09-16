package com.example.sns.controller.response;

import com.example.sns.model.Alarm;
import com.example.sns.model.AlarmArgs;
import com.example.sns.model.AlarmType;
import com.example.sns.model.User;
import com.example.sns.model.entity.AlarmEntity;
import lombok.AllArgsConstructor;

import java.sql.Timestamp;

@AllArgsConstructor
public class AlarmResponse {

    private Integer id;
    private AlarmType alarmType;
    private AlarmArgs alarmArgs;
    private String text;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static AlarmResponse fromAlarm(Alarm alarm) {
        return new AlarmResponse(
                alarm.getId(),
                alarm.getAlarmType(),
                alarm.getArgs(),
                alarm.getAlarmType().getAlarmText(),
                alarm.getRegisteredAt(),
                alarm.getUpdatedAt(),
                alarm.getDeletedAt()
        );
    }
}
