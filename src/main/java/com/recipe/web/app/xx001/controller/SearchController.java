package com.recipe.web.app.xx001.controller;

import com.recipe.base.BaseController;
import com.recipe.web.app.common.session.UserSession;
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
        System.out.println("★★★searchPage : ");
        UserSession user = (UserSession) httpSession.getAttribute("USER_SESSION");
        model.addAttribute("user", user);

        // Set<String> → カンマ区切り文字列に変換して渡す
        if (user != null && user.getRole() != null) {
            String rolesStr = String.join(", ", user.getRole());
            model.addAttribute("rolesStr", rolesStr);
        } else {
            model.addAttribute("rolesStr", "");
        }

        model.addAttribute("searchForm", new Object());
        return "search";
    }

    @PostMapping("/search")
    public String doSearch(@ModelAttribute SearchForm form, Model model) {
        List<String> results = searchService.search(form.getKeyword());
        model.addAttribute("results", results);
        return "search";
    }
}