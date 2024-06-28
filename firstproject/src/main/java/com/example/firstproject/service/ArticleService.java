package com.example.firstproject.service;

import com.example.firstproject.dto.ArticleForm;
import com.example.firstproject.entity.Article;
import com.example.firstproject.repository.ArticleRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Transactional
@Slf4j
@Service
public class ArticleService {
    @Autowired
    ArticleRepository articleRepository; // 리파지터리 객체 주입

    public List<Article> index() {
        return articleRepository.findAll();
    }

    public Article show(Long id) {
        return articleRepository.findById(id).orElse(null);
    }


    public Article create(ArticleForm dto) {
        Article article = dto.toEntity(); // DTO -> Entity로 전환 하기
        if(article.getId() != null){
            return null;
        }
        return articleRepository.save(article);
    }


    public Article update(Long id, ArticleForm dto) {
        Article article = dto.toEntity(); // DTO -> Entity 전환 수정값 DATA: repository 접근용
        log.info("id:{}, article{}", id, article.toString()); // id와 article 값들 디버깅
        // 타깃 조회하기
        Article target = articleRepository.findById(id).orElse(null); // 원래 DB에 있던 값
        // 잘못된 요청 삭제하기
        if(target == null || article.getId() != id){
            log.info("잘못된 접근: id: {}, article{}",id,article);
            return null;
        }
        //업데이트 하기
        target.patch(article);
        Article updated = articleRepository.save(target);
        return updated;
    }

    public Article delete(Long id) {
        Article target = articleRepository.findById(id).orElse(null); // 타겟 찾기
        if (target == null){
            return null;
        }
        articleRepository.delete(target);
        return target;
    }

    public List<Article> createArticles(List<ArticleForm> dtos) {
        // 1. dto 묶음을 엔티티 묶음으로 변환하기. stream()은 List와 같은 자료구조에 저장된 요소 하나씩 순회하며 처리하는 코드 패턴
        List<Article> articleList = dtos.stream().map(dto -> dto.toEntity()).collect(Collectors.toList());
        // 2. 엔티티 묶음을 DB에 저장하기
        articleList.stream().forEach(article -> articleRepository.save(article));
        // 3. 강제 예외 발생시키기
        articleRepository.findById(-1L)
                .orElseThrow(()-> new IllegalArgumentException("결제 실패!"));
        // 4. 결과 값 반환하기
        return articleList;
    }
}
