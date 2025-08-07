package com.makersworld.civic_sights_main.service;

import com.makersworld.civic_sights_main.model.Category;
import com.makersworld.civic_sights_main.model.NewsArticle;
import com.makersworld.civic_sights_main.repository.NewsArticleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsArticleService {
    
    private final NewsArticleRepository newsArticleRepository;
    
    // 페이지네이션 상수
    private static final int DEFAULT_PAGE_SIZE = 25;
    
    // 임시 데이터 (JPA 연결이 성공하면 제거될 예정)
    private List<NewsArticle> sampleArticles;   
    
    @PostConstruct
    private void initializeSampleData() {
        sampleArticles = new ArrayList<>();
        
        // Sample data
        sampleArticles.add(NewsArticle.builder()
                .id(1L)
                .title("기본소득 정책의 현황과 전망")
                .mainImg("https://example.com/image1.jpg")
                .author("김정책")
                .createDate(LocalDateTime.now().minusDays(1))
                .updateDate(LocalDateTime.now().minusDays(1))
                .content("기본소득 정책에 대한 자세한 분석 내용...")
                .category(Category.BASIC_INCOME)
                .isPremium(false)
                .build());
                
        sampleArticles.add(NewsArticle.builder()
                .id(2L)
                .title("시민 참여와 민주주의의 미래")
                .mainImg("https://example.com/image2.jpg")
                .author("이시민")
                .createDate(LocalDateTime.now().minusDays(2))
                .updateDate(LocalDateTime.now().minusDays(2))
                .content("시민 참여의 중요성과 새로운 방향성에 대한 논의...")
                .category(Category.CIVIC_ENGAGEMENT)
                .isPremium(true)
                .build());
                
        sampleArticles.add(NewsArticle.builder()
                .id(3L)
                .title("2024년 메가트렌드 분석")
                .mainImg("https://example.com/image3.jpg")
                .author("박트렌드")
                .createDate(LocalDateTime.now().minusDays(3))
                .updateDate(LocalDateTime.now().minusDays(3))
                .content("올해 주목해야 할 주요 메가트렌드들...")
                .category(Category.MEGATRENDS)
                .isPremium(true)
                .build());
    }
    
    /**
     * 샘플 데이터를 사용한 페이지네이션 헬퍼 메서드
     */
    private Page<NewsArticle> createPageFromList(List<NewsArticle> articles, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), articles.size());
        
        if (start > articles.size()) {
            return new PageImpl<>(new ArrayList<>(), pageable, articles.size());
        }
        
        List<NewsArticle> pageContent = articles.subList(start, end);
        return new PageImpl<>(pageContent, pageable, articles.size());
    }
    
    /**
     * 기본 Pageable 생성 (페이지당 25건)
     */
    private Pageable createDefaultPageable(int page) {
        return PageRequest.of(page, DEFAULT_PAGE_SIZE);
    }
    
    /**
     * 전체 뉴스 리스트 조회 (페이지네이션)
     */
    public Page<NewsArticle> getAllArticles(Pageable pageable) {
        try {
            // JPA Repository 사용 시도
            return newsArticleRepository.findAllByOrderByCreateDateDesc(pageable);
        } catch (Exception e) {
            // JPA 연결 실패 시 샘플 데이터 반환
            return createPageFromList(new ArrayList<>(sampleArticles), pageable);
        }
    }
    
    /**
     * 전체 뉴스 리스트 조회 (기본 페이지네이션 - 페이지당 25건)
     */
    public Page<NewsArticle> getAllArticles(int page) {
        return getAllArticles(createDefaultPageable(page));
    }
    
    /**
     * 유료 뉴스 리스트 조회 (페이지네이션)
     */
    public Page<NewsArticle> getPremiumArticles(Pageable pageable) {
        try {
            return newsArticleRepository.findByIsPremiumTrueOrderByCreateDateDesc(pageable);
        } catch (Exception e) {
            List<NewsArticle> premiumArticles = sampleArticles.stream()
                    .filter(NewsArticle::getIsPremium)
                    .collect(Collectors.toList());
            return createPageFromList(premiumArticles, pageable);
        }
    }
    
    /**
     * 유료 뉴스 리스트 조회 (기본 페이지네이션)
     */
    public Page<NewsArticle> getPremiumArticles(int page) {
        return getPremiumArticles(createDefaultPageable(page));
    }
    
    /**
     * 무료 뉴스 리스트 조회 (페이지네이션)
     */
    public Page<NewsArticle> getFreeArticles(Pageable pageable) {
        try {
            return newsArticleRepository.findByIsPremiumFalseOrderByCreateDateDesc(pageable);
        } catch (Exception e) {
            List<NewsArticle> freeArticles = sampleArticles.stream()
                    .filter(article -> !article.getIsPremium())
                    .collect(Collectors.toList());
            return createPageFromList(freeArticles, pageable);
        }
    }
    
    /**
     * 무료 뉴스 리스트 조회 (기본 페이지네이션)
     */
    public Page<NewsArticle> getFreeArticles(int page) {
        return getFreeArticles(createDefaultPageable(page));
    }
    
    /**
     * 카테고리별 뉴스 리스트 조회 (페이지네이션)
     */
    public Page<NewsArticle> getArticlesByCategory(Category category, Pageable pageable) {
        try {
            return newsArticleRepository.findByCategoryOrderByCreateDateDesc(category, pageable);
        } catch (Exception e) {
            List<NewsArticle> categoryArticles = sampleArticles.stream()
                    .filter(article -> article.getCategory() == category)
                    .collect(Collectors.toList());
            return createPageFromList(categoryArticles, pageable);
        }
    }
    
    /**
     * 카테고리별 뉴스 리스트 조회 (기본 페이지네이션)
     */
    public Page<NewsArticle> getArticlesByCategory(Category category, int page) {
        return getArticlesByCategory(category, createDefaultPageable(page));
    }
    
    /**
     * 개별 뉴스 상세 조회
     */
    public Optional<NewsArticle> getFreeArticleById(Long id) {
        try {
            return newsArticleRepository.findByIdAndIsPremiumFalse(id);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    /**
     * 유료 뉴스 상세 조회
     */
    public Optional<NewsArticle> getPremiumArticleById(Long id) {
        try {
            return newsArticleRepository.findByIdAndIsPremiumTrue(id);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    /**
     * 개별 뉴스 상세 조회 (모든 뉴스 - 무료/유료 구분 없음)
     */
    public Optional<NewsArticle> getArticleById(Long id) {
        try {
            return newsArticleRepository.findById(id);
        } catch (Exception e) {
            // JPA 연결 실패 시 샘플 데이터에서 검색
            return sampleArticles.stream()
                    .filter(article -> article.getId().equals(id))
                    .findFirst();
        }
    }
    
    /**
     * 뉴스 생성
     */
    public NewsArticle createArticle(NewsArticle article) {
        try {
            // JPA Repository 사용
            return newsArticleRepository.save(article);
        } catch (Exception e) {
            // JPA 연결 실패 시 메모리에 추가
            article.setId((long) (sampleArticles.size() + 1));
            article.setCreateDate(LocalDateTime.now());
            article.setUpdateDate(LocalDateTime.now());
            sampleArticles.add(article);
            return article;
        }
    }
    
    /**
     * 뉴스 수정
     */
    public Optional<NewsArticle> updateArticle(Long id, NewsArticle updatedArticle) {
        try {
            Optional<NewsArticle> existingArticle = newsArticleRepository.findById(id);
            if (existingArticle.isPresent()) {
                NewsArticle article = existingArticle.get();
                article.setTitle(updatedArticle.getTitle());
                article.setMainImg(updatedArticle.getMainImg());
                article.setAuthor(updatedArticle.getAuthor());
                article.setContent(updatedArticle.getContent());
                article.setCategory(updatedArticle.getCategory());
                article.setIsPremium(updatedArticle.getIsPremium());
                return Optional.of(newsArticleRepository.save(article));
            }
        } catch (Exception e) {
            // JPA 연결 실패 시 메모리에서 수정
            Optional<NewsArticle> existingArticle = sampleArticles.stream()
                    .filter(article -> article.getId().equals(id))
                    .findFirst();
            if (existingArticle.isPresent()) {
                NewsArticle article = existingArticle.get();
                article.setTitle(updatedArticle.getTitle());
                article.setMainImg(updatedArticle.getMainImg());
                article.setAuthor(updatedArticle.getAuthor());
                article.setContent(updatedArticle.getContent());
                article.setCategory(updatedArticle.getCategory());
                article.setIsPremium(updatedArticle.getIsPremium());
                article.setUpdateDate(LocalDateTime.now());
                return Optional.of(article);
            }
        }
        return Optional.empty();
    }
    
    /**
     * 뉴스 삭제
     */
    public boolean deleteArticle(Long id) {
        try {
            if (newsArticleRepository.existsById(id)) {
                newsArticleRepository.deleteById(id);
                return true;
            }
            return false;
        } catch (Exception e) {
            return sampleArticles.removeIf(article -> article.getId().equals(id));
        }
    }
} 