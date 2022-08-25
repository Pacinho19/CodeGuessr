package pl.pacinho.codeguessr.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum RsaRepository {

    IMPORTER_SMD("SMDImport/branches/20220825-refactor")
    ,DOCTOR_NO("DoctorNo/trunk")
    //,MONGUS("Mongus/trunk")
    //,OCTOPUS("OCTOPUS2CC/trunk");
    ;

    @Getter
    private String url;

}
