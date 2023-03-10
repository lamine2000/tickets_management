package sn.trivial.ticket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.web.filter.CorsFilter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;
import sn.trivial.ticket.security.*;
import sn.trivial.ticket.security.jwt.*;
import tech.jhipster.config.JHipsterProperties;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Import(SecurityProblemSupport.class)
public class SecurityConfiguration {

    private final JHipsterProperties jHipsterProperties;

    private final TokenProvider tokenProvider;

    private final CorsFilter corsFilter;
    private final SecurityProblemSupport problemSupport;

    public SecurityConfiguration(
        TokenProvider tokenProvider,
        CorsFilter corsFilter,
        JHipsterProperties jHipsterProperties,
        SecurityProblemSupport problemSupport
    ) {
        this.tokenProvider = tokenProvider;
        this.corsFilter = corsFilter;
        this.problemSupport = problemSupport;
        this.jHipsterProperties = jHipsterProperties;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .csrf()
            .disable()
            .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling()
                .authenticationEntryPoint(problemSupport)
                .accessDeniedHandler(problemSupport)
        .and()
            .headers()
                .contentSecurityPolicy(jHipsterProperties.getSecurity().getContentSecurityPolicy())
            .and()
                .referrerPolicy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
            .and()
                .permissionsPolicy().policy("camera=(), fullscreen=(self), geolocation=(), gyroscope=(), magnetometer=(), microphone=(), midi=(), payment=(), sync-xhr=()")
            .and()
                .frameOptions().sameOrigin()
        .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
            .authorizeRequests()
            .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .antMatchers("/app/**/*.{js,html}").permitAll()
            .antMatchers("/i18n/**").permitAll()
            .antMatchers("/content/**").permitAll()
            .antMatchers("/swagger-ui/**").permitAll()
            .antMatchers("/test/**").permitAll()

            .antMatchers("/api/authenticate").permitAll()
            .antMatchers("/api/register").permitAll()
            .antMatchers("/api/activate").permitAll()
            .antMatchers("/api/account/reset-password/init").permitAll()
            .antMatchers("/api/account/reset-password/finish").permitAll()
            .antMatchers("/api/admin/**").hasAuthority(AuthoritiesConstants.ADMIN)
        /***********BEGIN CUSTOM***********/
            .antMatchers("/api/tickets/clients/**").hasAuthority(AuthoritiesConstants.CLIENT)
            .regexMatchers("/api/tickets/\\d+/clients/*").hasAuthority(AuthoritiesConstants.CLIENT)
            .antMatchers("/api/tickets/change-status").hasAuthority(AuthoritiesConstants.CLIENT)
            .regexMatchers("/api/tickets/\\d+/send-message/clients").hasAuthority(AuthoritiesConstants.CLIENT)
            .antMatchers("/api/tickets/unassigned").hasAuthority(AuthoritiesConstants.AGENT)
            .regexMatchers("/api/tickets/\\d+/self-assign").hasAuthority(AuthoritiesConstants.AGENT)
            .antMatchers("/api/tickets/assigned").hasAuthority(AuthoritiesConstants.AGENT)
            .regexMatchers("/api/tickets/\\d+/assigned").hasAuthority(AuthoritiesConstants.AGENT)
            .regexMatchers("/api/tickets/\\d+/send-message/agents").hasAuthority(AuthoritiesConstants.AGENT)
            .regexMatchers("/api/tickets/\\d+/admin/assign-agent/\\d+").hasAuthority(AuthoritiesConstants.ADMIN)
            .regexMatchers("/api/tickets/assigned/agents/\\d+").hasAuthority(AuthoritiesConstants.ADMIN)
            .regexMatchers("/api/tickets/assigned/agents/\\d+/count").hasAuthority(AuthoritiesConstants.ADMIN)
            .regexMatchers("/api/tickets/assigned/any-agent").hasAuthority(AuthoritiesConstants.ADMIN)
            .regexMatchers("/api/tickets/status/\\w+").hasAuthority(AuthoritiesConstants.ADMIN)

            .regexMatchers("/api/messages/tickets/\\d+/clients").hasAuthority(AuthoritiesConstants.CLIENT)
            .regexMatchers("/api/messages/tickets/\\d+/agents").hasAuthority(AuthoritiesConstants.AGENT)
            .regexMatchers("/api/messages/tickets/\\d+/admin").hasAuthority(AuthoritiesConstants.ADMIN)

            .antMatchers("/api/clients/register").permitAll()

            .antMatchers("/api/agents/register").hasAuthority(AuthoritiesConstants.ADMIN)
            /***********END CUSTOM***********/
            .antMatchers("/api/**").authenticated()

            .antMatchers("/management/health").permitAll()
            .antMatchers("/management/health/**").permitAll()
            .antMatchers("/management/info").permitAll()
            .antMatchers("/management/prometheus").permitAll()
            .antMatchers("/management/**").hasAuthority(AuthoritiesConstants.ADMIN)

        .and()
            .httpBasic()
        .and()
            .apply(securityConfigurerAdapter());

        return http.build();
        // @formatter:on
    }

    private JWTConfigurer securityConfigurerAdapter() {
        return new JWTConfigurer(tokenProvider);
    }
}
