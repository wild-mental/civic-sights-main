package com.makersworld.civic_sights_main.controller;

import com.makersworld.civic_sights_main.model.Category;
import com.makersworld.civic_sights_main.model.NewsArticle;
import com.makersworld.civic_sights_main.service.NewsArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class NewsArticleController {
    
    private final NewsArticleService newsArticleService;
    
    /**
     * 전체 뉴스 리스트 조회
     * GET /api/articles
     */
    @GetMapping
    public ResponseEntity<List<NewsArticle>> getAllArticles() {
        List<NewsArticle> articles = newsArticleService.getAllArticles();
        return ResponseEntity.ok(articles);
    }
    
    /**
     * 유료 뉴스 리스트 조회
     * GET /api/articles/premium
     */
    @GetMapping("/premium")
    public ResponseEntity<List<NewsArticle>> getPremiumArticles() {
        List<NewsArticle> articles = newsArticleService.getPremiumArticles();
        return ResponseEntity.ok(articles);
    }
    
    /**
     * 무료 뉴스 리스트 조회
     * GET /api/articles/free
     */
    @GetMapping("/free")
    public ResponseEntity<List<NewsArticle>> getFreeArticles() {
        List<NewsArticle> articles = newsArticleService.getFreeArticles();
        return ResponseEntity.ok(articles);
    }
    
    /**
     * 카테고리별 뉴스 리스트 조회
     * GET /api/articles/category/{category}
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<NewsArticle>> getArticlesByCategory(
            @PathVariable Category category) {
        List<NewsArticle> articles = newsArticleService.getArticlesByCategory(category);
        return ResponseEntity.ok(articles);
    }
    
    /**
     * 개별 뉴스 상세 조회
     * GET /api/articles/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<NewsArticle> getArticleById(@PathVariable Long id) {
        Optional<NewsArticle> article = newsArticleService.getArticleById(id);
        return article.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * 뉴스 생성
     * POST /api/articles
     */
    @PostMapping
    public ResponseEntity<NewsArticle> createArticle(@RequestBody NewsArticle article) {
        NewsArticle createdArticle = newsArticleService.createArticle(article);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdArticle);
    }
    
    /**
     * 뉴스 수정
     * PUT /api/articles/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<NewsArticle> updateArticle(
            @PathVariable Long id, 
            @RequestBody NewsArticle article) {
        Optional<NewsArticle> updatedArticle = newsArticleService.updateArticle(id, article);
        return updatedArticle.map(ResponseEntity::ok)
                           .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * 뉴스 삭제
     * DELETE /api/articles/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        boolean deleted = newsArticleService.deleteArticle(id);
        return deleted ? ResponseEntity.noContent().build() 
                      : ResponseEntity.notFound().build();
    }
    
    /**
     * 건강 상태 확인 (헬스 체크)
     * GET /api/articles/health
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("News Article API is running!");
    }
} 