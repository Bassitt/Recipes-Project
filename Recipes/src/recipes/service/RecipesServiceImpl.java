package recipes.service;

import org.springframework.stereotype.Service;
import recipes.models.Recipe;
import recipes.repositories.RecipesRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class RecipesServiceImpl implements RecipesService {
    private final RecipesRepository recipesRepository;

    public RecipesServiceImpl(RecipesRepository recipesRepository) {
        this.recipesRepository = recipesRepository;
    }

    @Override
    public void delete(Long id) {
        var recipe = recipesRepository.findById(id).orElseThrow(NoSuchElementException::new);
        recipesRepository.delete(recipe);
    }

    @Override
    public Recipe create(Recipe recipe) {
        return recipesRepository.saveAndFlush(recipe);
    }

    @Override
    public Optional<Recipe> get(Long id) {
        return recipesRepository.findById(id);
    }

    @Override
    public void update(Recipe recipe) {
        recipesRepository.findById(recipe.getId()).orElseThrow(NoSuchElementException::new);
        recipesRepository.save(recipe);
    }

    @Override
    public List<Recipe> findByName(String name) {
        return recipesRepository.findByNameContainingIgnoreCaseOrderByDateDesc(name);
    }

    @Override
    public List<Recipe> findByCategory(String category) {
        return recipesRepository.findAllByCategoryIgnoreCaseOrderByDateDesc(category);
    }
}