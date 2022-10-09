package recipes.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import recipes.repositories.UserRepository;

@Service
public record UserDetailsServiceImpl(UserRepository repository) implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return repository()
                .findByEmail(username)
                .stream()
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("Not found: " + username));
    }
}