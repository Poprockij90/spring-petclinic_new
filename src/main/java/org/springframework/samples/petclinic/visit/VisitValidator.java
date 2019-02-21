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
        String descrition=visit.getDescription();


        // date validation
        if (visit.getDate()== null) {
            errors.rejectValue("date", REQUIRED, REQUIRED);
        }


        // Vet validation
        if (visit.getVet() == null) {
            errors.rejectValue("vet", REQUIRED, REQUIRED);
        }

        // Description validation
        if (!StringUtils.hasLength(descrition)) {
            errors.rejectValue("description", REQUIRED, REQUIRED);
        }



    }
}
