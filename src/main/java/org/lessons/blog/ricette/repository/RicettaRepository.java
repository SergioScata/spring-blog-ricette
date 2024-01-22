package org.lessons.blog.ricette.repository;

import org.lessons.blog.ricette.model.Ricetta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RicettaRepository extends JpaRepository<Ricetta, Integer> {

    List<Ricetta> findByTitleContainingOrIngredientsContaining(String searchTitle, String searchIngredients);
}
