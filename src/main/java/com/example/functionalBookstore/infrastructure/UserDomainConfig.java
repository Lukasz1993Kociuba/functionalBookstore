package com.example.functionalBookstore.infrastructure;

import com.example.functionalBookstore.domain.user.core.UserService;
import com.example.functionalBookstore.domain.user.core.ports.incoming.AddNewUser;
import com.example.functionalBookstore.domain.user.core.ports.incoming.GetLoggedUser;
import com.example.functionalBookstore.domain.user.core.ports.outgoing.UserDatabase;
import com.example.functionalBookstore.domain.user.infrastructure.UserDatabaseAdapter;
import com.example.functionalBookstore.domain.user.infrastructure.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class UserDomainConfig {

    @Bean
    public UserDatabase userDatabase(UserRepository userRepository) {
        return new UserDatabaseAdapter(userRepository);
    }

    @Bean
    @Qualifier("AddNewUser")
    public AddNewUser addNewUser(UserDatabase userDatabase, BCryptPasswordEncoder passwordEncoder) {
        return new UserService(userDatabase, passwordEncoder);
    }

    @Bean
    @Qualifier("GetLoggedUser")
    public GetLoggedUser getLoggedUser(UserDatabase userDatabase, BCryptPasswordEncoder passwordEncoder) {
        return new UserService(userDatabase, passwordEncoder);
    }
}
