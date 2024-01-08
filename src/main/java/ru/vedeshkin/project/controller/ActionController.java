package ru.vedeshkin.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.vedeshkin.project.entity.Action;
import ru.vedeshkin.project.model.CustomUserDetails;
import ru.vedeshkin.project.repository.ActionRepository;
import ru.vedeshkin.project.service.ActionService;

import java.util.List;

@Controller
@RequestMapping("/actions")
public class ActionController {

    private final ActionService actionService;

    @Autowired
    public ActionController(ActionService actionService) {
        this.actionService = actionService;
    }

    @GetMapping()
    public ModelAndView getAllActions(@AuthenticationPrincipal CustomUserDetails user) {
        List<Action> actions = actionService.findAll();

        ModelAndView mav = new ModelAndView("list-actions");
        mav.addObject("user", user);
        mav.addObject("actions", actions);

        return mav;
    }

}
