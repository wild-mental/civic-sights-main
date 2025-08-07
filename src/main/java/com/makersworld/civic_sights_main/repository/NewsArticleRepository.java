package com.makersworld.civic_sights_main.repository;

import com.makersworld.civic_sights_main.model.Category;
import com.makersworld.civic_sights_main.model.NewsArticle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NewsArticleRepository extends JpaRepository<NewsArticle, Long> {
    
    // Find all articles ordered by creation date descending (페이지네이션)
    Page<NewsArticle> findAllByOrderByCreateDateDesc(Pageable pageable);
    
    // Find premium articles (페이지네이션)
    Page<NewsArticle> findByIsPremiumTrueOrderByCreateDateDesc(Pageable pageable);
    
    // Find free articles (페이지네이션)
    Page<NewsArticle> findByIsPremiumFalseOrderByCreateDateDesc(Pageable pageable);
    
    // Find articles by category (페이지네이션)
    Page<NewsArticle> findByCategoryOrderByCreateDateDesc(Category category, Pageable pageable);
    
    // Find articles by category and premium status (페이지네이션)
    Page<NewsArticle> findByCategoryAndIsPremiumOrderByCreateDateDesc(
            Category category, Boolean isPremium, Pageable pageable);
    
    // Find articles by premium status and ID
    Optional<NewsArticle> findByIdAndIsPremiumFalse(Long id);
    Optional<NewsArticle> findByIdAndIsPremiumTrue(Long id);
    
    // Search articles by title or content (페이지네이션)
    @Query("SELECT n FROM NewsArticle n WHERE " +
           "LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(n.content) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "ORDER BY n.createDate DESC")
    Page<NewsArticle> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    // Find articles by author (페이지네이션)
    Page<NewsArticle> findByAuthorContainingIgnoreCaseOrderByCreateDateDesc(String author, Pageable pageable);
    
    // Count by premium status
    long countByIsPremium(Boolean isPremium);
    
    // Count by category
    long countByCategory(Category category);
} 