package recipes.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import recipes.models.Recipe;
import recipes.service.RecipesService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

import static java.lang.System.Logger.Level.DEBUG;
import static java.lang.System.Logger.Level.INFO;

@RestController
@RequestMapping("/api/recipe")
public class RecipesController {
    private static final System.Logger LOG = System.getLogger(RecipesController.class.getName());

    private final RecipesService recipesService;

    public RecipesController(RecipesService recipesService) {
        this.recipesService = recipesService;
    }

    @PostMapping("/new")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Long> create(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody final Recipe recipe
    ) {
        LOG.log(DEBUG, userDetails);
        recipe.setUser(userDetails.getUsername());
        recipesService.create(recipe);
        return Map.of("id", recipesService.create(recipe).getId());
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@AuthenticationPrincipal final UserDetails userDetails,
                       @RequestBody @Valid final Recipe recipe,
                       @PathVariable final Long id) {
        checkUserRights(userDetails, id);
        recipe.setId(id);
        recipesService.update(recipe);
    }

    private void checkUserRights(UserDetails userDetails, Long recipeId) {
        var recipe = recipesService.get(recipeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        LOG.log(INFO, "Check rights. User: {0} Recipe: {1}", userDetails.getUsername(), recipe.getUser());

        var hasRights = recipe.getUser() == null || userDetails.getUsername().equals(recipe.getUser());

        if (!hasRights) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("{id}")
    public Recipe get(@PathVariable Long id) {
        return recipesService.get(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id) {
        checkUserRights(userDetails, id);
        recipesService.delete(id);
    }

    @GetMapping("search")
    public List<Recipe> search(
            @RequestParam(required = false) @NotBlank String category,
            @RequestParam(required = false) @NotBlank String name
    ) {
        if (category == null ^ name == null) {
            return name == null
                    ? recipesService.findByCategory(category)
                    : recipesService.findByName(name);
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Exactly one search parameter must be specified");
    }
}