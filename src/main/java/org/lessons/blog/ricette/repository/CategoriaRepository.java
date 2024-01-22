package org.lessons.blog.ricette.repository;

import org.lessons.blog.ricette.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
}
