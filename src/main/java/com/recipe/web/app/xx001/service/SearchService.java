package com.recipe.web.app.xx001.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {
    public List<String> search(String keyword) {
        // サンプル: キーワードを含む固定リストを返す
        return List.of("Apple", "Banana", "Cherry").stream()
                .filter(s -> keyword == null || s.contains(keyword))
                .toList();
    }
}

