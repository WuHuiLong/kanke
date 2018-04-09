package com.kanke.controller.backend;

import com.kanke.service.IMovieService;
import com.kanke.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/manage/movie/")
public class MovieManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IMovieService iMovieService;
}
