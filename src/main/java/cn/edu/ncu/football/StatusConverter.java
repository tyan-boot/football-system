package cn.edu.ncu.football;

import cn.edu.ncu.football.model.Race.RaceStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class StatusConverter implements AttributeConverter<RaceStatus, Integer> {
    @Override
    public Integer convertToDatabaseColumn(RaceStatus attribute) {
        return attribute.getValue();
    }

    @Override
    public RaceStatus convertToEntityAttribute(Integer dbData) {
        for (RaceStatus status : RaceStatus.values()) {
            if (status.getValue().equals(dbData)) {
                return status;
            }
        }

        throw new RuntimeException("Unknown database value: ".concat(dbData.toString()));
    }
}
