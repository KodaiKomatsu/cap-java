// package customer.cap_java_handson9999.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.core.annotation.Order;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.oauth2.jwt.JwtDecoder;
// import org.springframework.security.oauth2.jwt.JwtDecoders;
// @Configuration
// @EnableWebSecurity
// @Order(1)
// public class SecurityConfig {
//     // private static final String ALL_PATH = "/**";
//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception  {
//         // @formatter:off
//         http
//             .sessionManagement((session) -> session
//                     .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                 )
//             .securityMatcher(AntPathRequestMatcher.antMatcher("/**"))
//             .csrf((csrf) -> csrf.disable())
//             .authorizeHttpRequests((authorize) -> authorize.anyRequest().permitAll());
//         // @formatter:on
//         return http.build();
//     }

//     @Bean
//     public JwtDecoder jwtDecoder() {
//         return JwtDecoders.fromIssuerLocation("https://0bde5657trial.authentication.us10.hana.ondemand.com/oauth/token");
//     }
// }
