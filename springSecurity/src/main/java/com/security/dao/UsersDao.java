package com.security.dao;

import com.security.entity.Users;

public interface UsersDao {
    Users getUsers(String userName);
}
