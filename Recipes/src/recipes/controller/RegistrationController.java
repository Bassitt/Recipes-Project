package recipes.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import recipes.models.User;
import recipes.repositories.UserRepository;

import javax.validation.Valid;
import java.util.List;

import static java.lang.System.Logger.Level.INFO;
import static java.lang.System.Logger.Level.WARNING;

@RestController
public record RegistrationController(UserRepository repository, PasswordEncoder encoder) {
    private static final System.Logger LOG = System.getLogger(RegistrationController.class.getName());

    @PostMapping("/api/register")
    public void register(@RequestBody @Valid User user) {
        LOG.log(INFO, "Register user: {0}", user.getEmail());

        if (isExistUser(user)) {
            LOG.log(WARNING, "the user {0} already registered.", user.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "the user already registered.");
        }

        user.setPassword(encoder.encode(user.getPassword()));

        repository.save(user);
    }

    private boolean isExistUser(User user) {
        return !repository.findByEmail(user.getEmail()).isEmpty();
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return repository.findAll();
    }
}