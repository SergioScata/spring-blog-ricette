package org.lessons.blog.ricette.controller;

import jakarta.validation.Valid;
import org.lessons.blog.ricette.model.Ricetta;
import org.lessons.blog.ricette.repository.RicettaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/ricette")
public class ricettaController {

    @Autowired
    private RicettaRepository ricettaRepository;

    @GetMapping
    public String index(@RequestParam(name = "keyword", required = false) String searchKeyword, Model model) {
        List<Ricetta> ricettaList;
        if (searchKeyword != null){
            ricettaList = ricettaRepository.findByTitleContainingOrIngredientsContaining(searchKeyword,searchKeyword );
        }else{
            ricettaList = ricettaRepository.findAll();
        }
        model.addAttribute("ricettaList", ricettaList);

        model.addAttribute("preloadSearch", searchKeyword);
        return "ricette/list";
    }

    @GetMapping("/show/{id}")
    public String show(@PathVariable Integer id, Model model) {
        Optional<Ricetta> result = ricettaRepository.findById(id);
        if (result.isPresent()) {
            Ricetta ricetta = result.get();
            model.addAttribute("ricetta", ricetta);
            return "ricette/show";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe with id " + id + " not found");
        }
    }
    @GetMapping("/create")
    public String create(Model model) {
        Ricetta ricetta = new Ricetta();
        model.addAttribute("ricetta", ricetta);
        return "ricette/create";
    }

    @PostMapping("/create")
    public String store(@Valid @ModelAttribute("ricetta") Ricetta formRicetta, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "ricette/create";
        }
        Ricetta savedRicetta = ricettaRepository.save(formRicetta);
        return "redirect:/ricette/show/" + savedRicetta.getId();
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model){
        Optional<Ricetta> result = ricettaRepository.findById(id);
        if(result.isPresent()){
            model.addAttribute("ricetta", result.get());
            return "ricette/edit";
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe with id" + id + "not found");
        }
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Integer id,@Valid @ModelAttribute("ricetta") Ricetta formRicetta, BindingResult bindingResult){
        Optional<Ricetta> result = ricettaRepository.findById(id);
        if (result.isPresent()){
            Ricetta ricettaToEdit = result.get();
            if (bindingResult.hasErrors()){
                return "ricette/edit";
            }
            formRicetta.setPhoto(ricettaToEdit.getPhoto());
            Ricetta savedRicetta = ricettaRepository.save(formRicetta);
            return "redirect:/ricette/show/" + id;
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe with id" + id + "not found");
        }
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes){
        Optional<Ricetta> result = ricettaRepository.findById(id);
        if (result.isPresent()){
            ricettaRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("redirectMessage", result.get().getTitle() + " è stato cancellato!");
            return "redirect:/ricette";
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe with id" + id + "not found");
        }
    }






}
