package recipes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import recipes.models.Recipe;

import java.util.List;

@Repository
public interface RecipesRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findAllByCategoryIgnoreCaseOrderByDateDesc(String category);

    List<Recipe> findByNameContainingIgnoreCaseOrderByDateDesc(String name);
}