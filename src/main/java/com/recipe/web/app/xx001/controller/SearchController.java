package com.recipe.web.app.xx001.controller;

import com.recipe.base.BaseController;
import com.recipe.web.app.xx001.form.SearchForm;
import com.recipe.web.app.xx001.service.SearchService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SearchController extends BaseController {

    private final HttpSession httpSession;

    private final SearchService searchService;

    @GetMapping("/search")
    public String searchPage(Model model) {

        model.addAttribute("searchForm", new SearchForm());
        return "search";
    }

    @PostMapping("/search")
    public String doSearch(@ModelAttribute SearchForm form, Model model) {
        List<String> results = searchService.search(form.getKeyword());
        model.addAttribute("results", results);
        return "search";
    }
}