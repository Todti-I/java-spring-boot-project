package ru.vedeshkin.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.vedeshkin.project.entity.Action;
import ru.vedeshkin.project.repository.ActionRepository;

import java.util.List;

@Service
public class ActionServiceImpl implements ActionService {

    private final ActionRepository actionRepository;

    @Autowired
    public ActionServiceImpl(ActionRepository actionRepository) {
        this.actionRepository = actionRepository;
    }

    @Override
    public List<Action> findAll() {
        return actionRepository.findAll(Sort.by(Sort.Direction.DESC, "date"));
    }

    @Override
    public void create(String description) {
        Action action = new Action();
        action.setDescription(description);
        actionRepository.save(action);
    }

}
