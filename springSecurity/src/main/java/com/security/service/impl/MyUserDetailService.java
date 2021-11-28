package com.security.service.impl;

import com.security.dao.UsersDao;
import com.security.entity.Users;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MyUserDetailService implements UserDetailsService {
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private UsersDao usersDao;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Users users = usersDao.getUsers(userName);
        if (users == null) {//用戶不存在
            throw new UsernameNotFoundException("用戶名不存在");
        }
        List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList("admin,ROLE_sale");
        return new User(users.getUserName(),passwordEncoder.encode(users.getPassword()),auths);
    }
}
