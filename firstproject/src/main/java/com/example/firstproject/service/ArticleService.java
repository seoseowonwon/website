package com.example.firstproject.service;

import com.example.firstproject.dto.ArticleForm;
import com.example.firstproject.entity.Article;
import com.example.firstproject.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
