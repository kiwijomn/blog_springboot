package com.study.domain.contact;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContactController {
  
	/**
	 * 문의화면 이동
	 * @return
	 */
	@GetMapping("/contact/view.do")
    public String openPostView() {
        return "contact/contact_view";
    }
}
