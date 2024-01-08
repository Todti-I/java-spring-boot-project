package ru.vedeshkin.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vedeshkin.project.entity.Action;

@Repository
public interface ActionRepository extends JpaRepository<Action, Long> {

}
