/*
 * Copyright 2012-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.owner;
import org.springframework.samples.petclinic.vet.Vet;
import org.springframework.samples.petclinic.vet.VetRepository;
import org.springframework.samples.petclinic.visit.Visit;
import org.springframework.samples.petclinic.visit.VisitRepository;
import org.springframework.samples.petclinic.visit.VisitValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Collection;
import java.util.Map;
/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 * @author Dave Syer
 */
@Controller
@RequestMapping("/owners/{ownerId}")
class VisitController {
    private static final String VIEWS_VISITS_CREATE_OR_UPDATE_FORM = "pets/createOrUpdateVisitForm";
    private final VisitRepository visits;
    private final VetRepository vets;
    private final PetRepository pets;
    private final OwnerRepository owners;

    public VisitController(VisitRepository visits, PetRepository pets, OwnerRepository owners, VetRepository vets) {
        this.visits = visits;
        this.vets = vets;
        this.pets = pets;
        this.owners = owners;
    }

    @InitBinder("visit")
    public void initPetBinder(WebDataBinder dataBinder) {
        dataBinder.setValidator(new VisitValidator());
    }

    @InitBinder("visit")
    public void initOwnerBinder(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    /**
     * Called before each and every @RequestMapping annotated method.
     * 2 goals:
     * - Make sure we always have fresh data
     * - Since we do not use the session scope, make sure that Pet object always has an id
     * (Even though id is not part of the form fields)
     *
     * @return Pet
     */

    @ModelAttribute("vets")
    public Collection<Vet> allVets() {
        return vets.findAll();
    }

    @ModelAttribute("visit")
    public Visit loadPetWithVisit(@PathVariable("petId") int petId, @PathVariable(name = "visitId", required = false) Integer visitId, Map<String, Object> model) {
        Pet pet = this.pets.findById(petId);
        model.put("pet", pet);
        Visit visit = visitId == null ? new Visit() : visits.findById(visitId);
        pet.addVisit(visit);
        return visit;
    }





    @GetMapping("/pets/{petId}/visits/new")
    public String initNewVisitForm(@PathVariable("petId") int petId, Visit visit, ModelMap model) {
        Pet pet = this.pets.findById(petId);
        pet.addVisit(visit);
        model.put("pet", pet);
        return "pets/createOrUpdateVisitForm";
    }

    @PostMapping("/pets/{petId}/visits/new")
    public String processNewVisitForm(@Valid Visit visit, BindingResult result) {
        if (result.hasErrors()) {
            return "pets/createOrUpdateVisitForm";
        } else {
            visit.setStatus("active");
            this.visits.save(visit);
            return "redirect:/owners/{ownerId}";
        }
    }


    @GetMapping("/pets/{petId}/visits/{visitId}/edit")
    public String initUpdateForm(@PathVariable("ownerId") int ownerId, @PathVariable("petId") int petId, ModelMap model) {
        Owner owner=owners.findById(ownerId);
        model.put("ownersPets",owner.getPets());
        return VIEWS_VISITS_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping("/pets/{petId}/visits/{visitId}/edit")
    public String editVisit(@Valid Visit visit, BindingResult result) {
        if (result.hasErrors()) {
            return "pets/createOrUpdateVisitForm";
        } else {
            this.visits.save(visit);
            return "redirect:/owners/{ownerId}";
        }
    }



    @GetMapping("/pets/{petId}/visits/{visitId}/cancel")
    public String cancelVisit(@PathVariable("visitId") int visitId) {
        Visit visit = visits.findById(visitId);
        visit.toCancel();
        this.visits.save(visit);
        return "redirect:/owners/{ownerId}";

    }


}
