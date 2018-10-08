package com.ritto.srm.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.sql.DataSource;

/**
 * @author : Eiden J.P Zhou
 * @Date: 2018/4/11
 * @Description:
 * @Modified By:
 */

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //关闭 CSRF 保护
        http.csrf().disable();
        //HttpSecurity 注释
        http.authorizeRequests()
                //..assets目录下的所有资源能够被所有人访问
                .antMatchers("/assets/**").permitAll()
                .antMatchers("/temp/**").permitAll()
                // ..admin目录下只有ADMIN权限的能访问 所有请求都需要进行验证
                .antMatchers("/admin").hasRole("ADMIN").anyRequest().fullyAuthenticated()
                .and()
                //允许用户进行基于表单的认证 设置登陆页面 登陆失败的页面 所有人都能访问
                .formLogin().loginPage("/login").failureUrl("/login?type=error").defaultSuccessUrl("/index").permitAll()
                .and()
                //触发登出页面的url
                .logout().logoutUrl("/logout").permitAll();
    }
//    @Autowired
//    DataSource dataSource;
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
//        auth
//                .jdbcAuthentication()
//                .dataSource(dataSource)
//                .usersByUsernameQuery("")
//                .authoritiesByUsernameQuery("");
//    }

    @Bean
    public JdbcUserDetailsManager jdbcUserDetailsManager(@Qualifier("primaryDataSource") DataSource dataSource) {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager();
        jdbcUserDetailsManager.setDataSource(dataSource);
        jdbcUserDetailsManager.setUsersByUsernameQuery("SELECT user_name,password,true from h_user where user_name = ?");
//        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("select username,'ROLE_ADMIN' from user where username = ?");
        return jdbcUserDetailsManager;
    }
//    @Autowired
////    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
////        auth
////                .inMemoryAuthentication()
////                .withUser("user").password("password").roles("USER");
////    }

}