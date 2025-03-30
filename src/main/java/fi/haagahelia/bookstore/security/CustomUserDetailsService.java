package fi.haagahelia.bookstore.security;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import fi.haagahelia.bookstore.domain.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository käyttäjäTietovarasto;

    public CustomUserDetailsService(UserRepository käyttäjäTietovarasto) {
        this.käyttäjäTietovarasto = käyttäjäTietovarasto;
    }

    @Override
    public UserDetails loadUserByUsername(String käyttäjätunnus) throws UsernameNotFoundException {
        fi.haagahelia.bookstore.domain.User käyttäjä = käyttäjäTietovarasto.findByKäyttäjätunnus(käyttäjätunnus)
                .orElseThrow(() -> new UsernameNotFoundException("Käyttäjää ei löydy: " + käyttäjätunnus));

        List<SimpleGrantedAuthority> roolit = List.of(new SimpleGrantedAuthority("ROLE_" + käyttäjä.getRooli().toUpperCase()));
        
        return new org.springframework.security.core.userdetails.User(
                käyttäjä.getKäyttäjätunnus(),
                käyttäjä.getSalasana(),
                roolit
        );
    }
}
