package model.dao;

import lombok.Getter;
import lombok.Setter;


//Справочник должностей
@Getter
@Setter
public class Positions //extends Document
                        {

    @Schema(description = "Код должности")
    private Integer positionCode;

    @Schema(description = "Количество вакантных должностей")
    private Integer positionVacantCount;

    @Schema(description = "Количество временно вакантных должностей")
    private Integer positionTemporaryVacantCount;

    @Schema(description = "Признак актуальности")
    private Boolean enable;
}
