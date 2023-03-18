package com.study.domain.post;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.study.common.dto.MessageDto;
import com.study.domain.FileUploadController;
import com.study.utils.PageMaker;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;

	@Value("${upload.path}")
	public String uploadDirectory;

    // 사용자에게 메시지를 전달하고, 페이지를 리다이렉트 한다.
    private String showMessageAndRedirect(final MessageDto params, Model model) {
        model.addAttribute("params", params);
        return "common/messageRedirect";
    }


    // 게시글 작성 페이지
    @GetMapping("/post/write.do")
    public String openPostWrite(@RequestParam(value = "id", required = false) final Long id, Model model) {
        if (id != null) {
            PostResponse post = postService.findPostById(id);
            if(post.getFileName()!=null && !post.getFileName().equals("")) {
            	model.addAttribute("encodeFile", URLEncoder.encode(post.getFileName()));	
            }
            model.addAttribute("post", post);
        }else{
            model.addAttribute("post", null);
        }
        return "post/write";
    }
    

    // 게시글 리스트 페이지
    @GetMapping("/post/list.do")
    public String openPostList(PageMaker pageMaker,	Model model) {
    	List<PostResponse> noticeList = postService.noticeList();
    	int totalCount = postService.getTotalCount(pageMaker);
    	//pageMaker.setPerPageNum(5);
    	pageMaker.setTotalCount(totalCount);
    	List<PostResponse> posts = postService.findAllPost(pageMaker);
    	String pagination =pageMaker.bootStrapPagingSearchHTML2("/post/list.do");
    	    	
        model.addAttribute("noticeList", noticeList);
        model.addAttribute("pageMaker", pageMaker);
        model.addAttribute("pagination", pagination);
        model.addAttribute("posts", posts);
        return "post/list";
    }

    
    // 게시글 상세 페이지
    @SuppressWarnings("deprecation")
	@GetMapping("/post/view.do")
    public String openPostView(@RequestParam final Long id, Model model) {
        PostResponse post = postService.findPostById(id);        
        if(post.getFileName()!=null && !post.getFileName().equals("")) {
        	model.addAttribute("encodeFile", URLEncoder.encode(post.getFileName()));	
        }
        model.addAttribute("post", post);
        return "post/view";
    }

    // 신규 게시글 생성
    @PostMapping("/post/save.do")
    public String savePost( PostRequest params,
    		@RequestParam(required = false, name="uploadfile" ) MultipartFile[] uploadfile,    		
    		Model model) throws Exception {
    	
		if(uploadfile!=null) {
			StringBuilder uploadedFileName = new StringBuilder();
			for (MultipartFile file : uploadfile) {
				try {
					log.info("*** file.getOriginalFilename() : ", file.getOriginalFilename());
					if(file.getOriginalFilename()!=null && StringUtils.hasText(file.getOriginalFilename())) {
						uploadedFileName.append(FileUploadController.uploadFile(uploadDirectory, file.getOriginalFilename(), file.getBytes(), null));
						if(uploadedFileName!=null) {
							params.setOrifileName(file.getOriginalFilename());	
							params.setFileName(uploadedFileName.toString());	
						}	
					}
								
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
    	
		 log.info("params    :{} " , params.toString());
        postService.savePost(params);
        
       
        MessageDto message = new MessageDto("게시글 생성이 완료되었습니다.", "/post/list.do", RequestMethod.GET, null);
        return showMessageAndRedirect(message, model);
    }

    // 기존 게시글 수정
    @PostMapping("/post/update.do")
    public String updatePost(final PostRequest params,
    		@RequestParam(required = false, name="uploadfile" ) MultipartFile[] uploadfile,   
    		Model model) throws Exception {    	
        
		if(uploadfile!=null) {
			StringBuilder uploadedFileName = new StringBuilder();
			for (MultipartFile file : uploadfile) {
				try {
					log.info("*** file.getOriginalFilename() : ", file.getOriginalFilename());
					if(file.getOriginalFilename()!=null && StringUtils.hasText(file.getOriginalFilename())) {
						uploadedFileName.append(FileUploadController.uploadFile(uploadDirectory, file.getOriginalFilename(), file.getBytes(), null));
						if(uploadedFileName!=null) {
							params.setOrifileName(file.getOriginalFilename());	
							params.setFileName(uploadedFileName.toString());	
						}	
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
    	
    	postService.updatePost(params);
        
        MessageDto message = new MessageDto("게시글 수정이 완료되었습니다.", "/post/list.do", RequestMethod.GET, null);
        return showMessageAndRedirect(message, model);
    }

    // 게시글 삭제
    @PostMapping("/post/delete.do")
    public String deletePost(@RequestParam final Long id, Model model) {
        postService.deletePost(id);
        MessageDto message = new MessageDto("게시글 삭제가 완료되었습니다.", "/post/list.do", RequestMethod.GET, null);
        return showMessageAndRedirect(message, model);
    }
    
    //첨부파일 삭제
    @ResponseBody
    @PostMapping("/post/deleteFile.do")
    public ResponseEntity<?> deleteFile(@RequestParam final Long id, Model model) {
    
		 PostResponse post = postService.findPostById(id);        
	     if(post.getFileName()!=null && !post.getFileName().equals("")) {	    		     	
	     	File file = new File(uploadDirectory + post.getFileName());
			if (file.exists()) {
				file.delete();
			}	 
			
			postService.deleteFile(id);
	     }
        
        return ResponseEntity.status(HttpStatus.OK).body("success");
    }
    

}
