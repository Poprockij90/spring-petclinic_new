package org.springframework.samples.petclinic.visit;

import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class VisitValidator implements Validator {

    private static final String REQUIRED = "required";

    @Override
    public boolean supports(Class<?> aClass) {

        return Visit.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        Visit visit = (Visit) obj;


        // date validation
        if (visit.getDate()== null) {
            errors.rejectValue("date", REQUIRED, REQUIRED);
        }

        // petid validation
        if (visit.getPetId() == null) {
            errors.rejectValue("petid", REQUIRED, REQUIRED);
        }

        // Vet validation
        if (visit.getVet() == null) {
            errors.rejectValue("vet", REQUIRED, REQUIRED);
        }



    }
}
