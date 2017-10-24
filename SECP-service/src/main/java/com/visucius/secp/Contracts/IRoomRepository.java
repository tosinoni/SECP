package com.visucius.secp.Contracts;


import com.visucius.secp.Entity.Room;

import java.util.List;

public interface IRoomRepository {

    Room getById(long id);
    List<Room> getAll();
}
