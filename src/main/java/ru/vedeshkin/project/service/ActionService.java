package ru.vedeshkin.project.service;

import ru.vedeshkin.project.entity.Action;

import java.util.List;

public interface ActionService {

    List<Action> findAll();

    void create(String description);

}
