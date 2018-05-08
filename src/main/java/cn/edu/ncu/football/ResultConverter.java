package cn.edu.ncu.football;

import cn.edu.ncu.football.model.RaceResults.RaceResult;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;


@Converter
public class ResultConverter implements AttributeConverter<RaceResult, Integer> {
    @Override
    public Integer convertToDatabaseColumn(RaceResult attribute) {
        return attribute.getValue();
    }

    @Override
    public RaceResult convertToEntityAttribute(Integer dbData) {
        for (RaceResult status : RaceResult.values()) {
            if (status.getValue().equals(dbData)) {
                return status;
            }
        }

        throw new RuntimeException("Unknown database value: ".concat(dbData.toString()));
    }
}
