package com.visucius.secp.Contracts;


import com.visucius.secp.Entity.User;

public interface IUserRepository {

    User getById(long id);
    User getByEmail(String email);
    void save(User user);

}
