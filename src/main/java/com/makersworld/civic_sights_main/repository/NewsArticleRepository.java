package com.makersworld.civic_sights_main.repository;

import com.makersworld.civic_sights_main.model.Category;
import com.makersworld.civic_sights_main.model.NewsArticle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsArticleRepository extends JpaRepository<NewsArticle, Long> {
    
    // Find all articles ordered by creation date descending
    List<NewsArticle> findAllByOrderByCreateDateDesc();
    
    // Find premium articles
    List<NewsArticle> findByIsPremiumTrueOrderByCreateDateDesc();
    
    // Find free articles
    List<NewsArticle> findByIsPremiumFalseOrderByCreateDateDesc();
    
    // Find articles by category
    List<NewsArticle> findByCategoryOrderByCreateDateDesc(Category category);
    
    // Find articles by category and premium status
    List<NewsArticle> findByCategoryAndIsPremiumOrderByCreateDateDesc(
            Category category, Boolean isPremium);
    
    // Search articles by title or content
    @Query("SELECT n FROM NewsArticle n WHERE " +
           "LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(n.content) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "ORDER BY n.createDate DESC")
    List<NewsArticle> searchByKeyword(@Param("keyword") String keyword);
    
    // Find articles by author
    List<NewsArticle> findByAuthorContainingIgnoreCaseOrderByCreateDateDesc(String author);
    
    // Count by premium status
    long countByIsPremium(Boolean isPremium);
    
    // Count by category
    long countByCategory(Category category);
} 