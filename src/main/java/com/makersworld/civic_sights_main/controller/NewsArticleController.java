package com.makersworld.civic_sights_main.controller;

import com.makersworld.civic_sights_main.model.Category;
import com.makersworld.civic_sights_main.model.NewsArticle;
import com.makersworld.civic_sights_main.service.NewsArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class NewsArticleController {
    
    private final NewsArticleService newsArticleService;
    
    /**
     * 전체 뉴스 리스트 조회 (페이지네이션)
     * GET /api/articles?page=0&size=25
     */
    @GetMapping
    public ResponseEntity<Page<NewsArticle>> getAllArticles(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "25") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<NewsArticle> articles = newsArticleService.getAllArticles(pageable);
        return ResponseEntity.ok(articles);
    }
    
    /**
     * 유료 뉴스 리스트 조회 (페이지네이션)
     * GET /api/articles/premium?page=0&size=25
     */
    @GetMapping("/premium")
    public ResponseEntity<Page<NewsArticle>> getPremiumArticles(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "25") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<NewsArticle> articles = newsArticleService.getPremiumArticles(pageable);
        return ResponseEntity.ok(articles);
    }
    
    /**
     * 무료 뉴스 리스트 조회 (페이지네이션)
     * GET /api/articles/free?page=0&size=25
     */
    @GetMapping("/free")
    public ResponseEntity<Page<NewsArticle>> getFreeArticles(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "25") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<NewsArticle> articles = newsArticleService.getFreeArticles(pageable);
        return ResponseEntity.ok(articles);
    }
    
    /**
     * 카테고리별 뉴스 리스트 조회 (페이지네이션)
     * GET /api/articles/category/{category}?page=0&size=25
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<Page<NewsArticle>> getArticlesByCategory(
            @PathVariable("category") Category category,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "25") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<NewsArticle> articles = newsArticleService.getArticlesByCategory(category, pageable);
        return ResponseEntity.ok(articles);
    }
    
    /**
     * 무료 뉴스 상세 조회
     * GET /api/articles/free/{id}
     */
    @GetMapping("/free/{id}")
    public ResponseEntity<NewsArticle> getFreeArticleById(@PathVariable("id") Long id) {
        Optional<NewsArticle> article = newsArticleService.getFreeArticleById(id);
        return article.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 유료 뉴스 상세 조회
     * GET /api/articles/premium/{id}
     */
    @GetMapping("/premium/{id}")
    public ResponseEntity<NewsArticle> getPremiumArticleById(@PathVariable("id") Long id,
                                                             @RequestHeader(value = "X-User-Roles", required = false) String rolesHeader) {
        // 게이트웨이가 부여한 역할 헤더를 검사하여 PAID_USER 인지 확인
        boolean hasPaidRole = rolesHeader != null &&
                java.util.Arrays.stream(rolesHeader.split(","))
                        .map(String::trim)
                        .anyMatch(r -> r.equalsIgnoreCase("PAID_USER") || r.equalsIgnoreCase("ROLE_PAID_USER"));

        if (!hasPaidRole) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Optional<NewsArticle> article = newsArticleService.getPremiumArticleById(id);
        return article.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * 개별 뉴스 상세 조회 (모든 뉴스 - 무료/유료 구분 없음)
     * GET /api/articles/{id}
     */
    @GetMapping("/{id:[0-9]+}")
    public ResponseEntity<NewsArticle> getArticleById(@PathVariable("id") Long id) {
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
            @PathVariable("id") Long id, 
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
    public ResponseEntity<Void> deleteArticle(@PathVariable("id") Long id) {
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