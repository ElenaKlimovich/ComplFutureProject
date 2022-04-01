package model.dao;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.List;

//Организация
@Getter
@Setter
public class Organization //extends Document
                            {

    public static final String LINK_POSITION = "commissionPositions";

    private static final Collection<LinkedField> LINKED_FIELDS =
            List.of(LinkedField.outgoingMany(LINK_POSITION, Positions.class));


    protected Collection<LinkedField> getLinkedFields() {
        return LINKED_FIELDS;
    }


    private String commissionName;
    private String phone;
    private String email;

}
