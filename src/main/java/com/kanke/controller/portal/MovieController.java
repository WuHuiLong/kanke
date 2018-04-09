package com.kanke.controller.portal;

import com.kanke.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/movie/")
public class MovieController {
    @Autowired
    private IUserService iUserService;



}
