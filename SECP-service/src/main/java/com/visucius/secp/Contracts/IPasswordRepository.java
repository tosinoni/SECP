package com.visucius.secp.Contracts;

public interface IPasswordRepository {

    String getPassword(long id);
    void save(long id, String password);
}
