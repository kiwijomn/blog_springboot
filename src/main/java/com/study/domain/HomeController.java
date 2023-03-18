package com.study.domain;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.study.domain.post.PostResponse;
import com.study.domain.post.PostService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Controller
@RequiredArgsConstructor
public class HomeController {

	   private final PostService postService;


    /** 메인 화면
     *  공지사항 포함 최신 게시글  5개 리스트
     * @param model
     * @return
     */
    @GetMapping(value = {"","/"})
    public String openPostList(Model model) {        	
    	List<PostResponse> lastestList = postService.lastestPost();
        model.addAttribute("lastestList", lastestList);
        return "index";
    }
       
    
    
}
