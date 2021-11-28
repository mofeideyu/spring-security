package com.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Resource
    private UserDetailsService userDetailsService;
    @Resource
    private DataSource dataSource;

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
//        jdbcTokenRepository.setCreateTableOnStartup(true);//自动创建表
        return jdbcTokenRepository;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //配置没有访问权限而跳转到自定义页面
        http.exceptionHandling().accessDeniedPage("/unauth.html");
        //退出配置
        http.logout().logoutUrl("/logout").logoutSuccessUrl("/login.html").permitAll();
        http.formLogin()//自定义编写的登录页面
            .loginPage("/login.html") //登录页面设置
            .loginProcessingUrl("/user/login") //登录访问路径
            .defaultSuccessUrl("/success.html") //登录成功之后,跳转路径
            .permitAll()
                .and().authorizeRequests()
                .antMatchers("/","/test/hello").permitAll()//设置哪些路径可以直接访问,不需要认证
//                .antMatchers("/test/index").hasAnyAuthority("admin")//设置权限
                .antMatchers("/test/index").hasRole("sale")
                .anyRequest().authenticated()
                .and().rememberMe().tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(120)//设置有效时间
                .userDetailsService(userDetailsService)
                .and().csrf().disable();//关闭csrf防护
    }
}
