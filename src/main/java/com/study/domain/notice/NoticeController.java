package com.study.domain.notice;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.study.domain.post.PostResponse;
import com.study.domain.post.PostService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class NoticeController {

	private final PostService postService;

	/**
	 * 최근 공지사항 1개만 가져오기
	 * @param model
	 * @return
	 */
	@GetMapping("/notice/view.do")
	public String openNoticiew(Model model) {
		PostResponse post = postService.lastestNotice();
		model.addAttribute("post", post);
		return "notice/notice_view";
	}

}
