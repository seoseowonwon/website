package com.example.firstproject.controller;


import com.example.firstproject.dto.ArticleForm;
import com.example.firstproject.entity.Article;
import com.example.firstproject.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
public class ArticleController {
    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/articles/new")
    public String newArticleForm(){
        return "articles/new";

    }

    // 입력 받은 데이터를 리다이렉트
    @PostMapping("/articles/create")
    public String createArticle(ArticleForm form){
        System.out.println(form.toString());
        // 1. DTO를 엔티티로 변환
        Article article = form.toEntity();
        log.info(form.toString());
        // 2. 리파지터리로 엔티티를 DB에 저장
        Article saved = articleRepository.save(article);
        //System.out.println(saved.toString());
        log.info(saved.toString());

        return "redirect:/articles/"+saved.getId();
    }

    // 해당 Id의 페이지를 보여 줌
    @GetMapping("/articles/{id}")// 데이터 조회 요청 접수
    public String show(@PathVariable Long id, Model model){ // 매개변수로 id 받아 오기
        log.info("id = " + id);

        // 1. id를 조회해 데이터 가져오기 DB에서 데이터를 가져오는 주체는 repository
        Article articleEntity = articleRepository.findById(id).orElse(null);
        log.info("articleEntity = " + articleEntity);
        // 2. 모델에 데이터 등록하기
        model.addAttribute("article", articleEntity);
        log.info("model = " + model);
        // 3. 뷰 페이지 반환하기
        return "articles/show";
    }

    //리스트 형식으로 뽑아 오기.
    @GetMapping("/articles")
    public String index(Model model){
        // 1. 모든 데이터 가져오기
        List<Article> articleEntityList = articleRepository.findAll();
        // 2. 모델에 데이터 등록하기
        model.addAttribute("articleList", articleEntityList);
        // 3. 뷰 페이지 설정하기
        return "articles/index";
    }

    @GetMapping("/articles/{id}/edit")
    public String edit(@PathVariable Long id, Model model){
        // 수정할 데이터 가져오기
        Article articleEntity = articleRepository.findById(id).orElse(null);
        // 모델에 데이터 등록하기
        model.addAttribute("article", articleEntity);
        // 뷰 페이지 설정하기
        return "articles/edit";
    }

    @PostMapping("/articles/update")
    public String update(ArticleForm form){
        log.info(form.toString());
        // 1. DTO를 엔티티로 변환하기
        Article articleEntity = form.toEntity();
        log.info(articleEntity.toString());
        // 2. 엔티티를 DB에 저장하기
        Article target = articleRepository.findById(articleEntity.getId()).orElse(null);
        // 3. 수정 결과 페이지로 리다이렉트하기
        if(target != null){
            articleRepository.save(articleEntity); // 엔티티를 DB에 저장
        }
        return "redirect:/articles/"+ articleEntity.getId();
    }

    @GetMapping("/articles/{id}/delete")
    public String delete(){
        log.info("삭제 요청이 들어왔습니다!");
        // 1. 삭제할 대상 가져오기
        // 2. 대상 엔티티 삭제하기
        // 3. 결과 페이지로 리다이렉트하기

        return null;
    }

}
