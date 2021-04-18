package com.clholdings.pass_shoot.springboot.web;

import com.clholdings.pass_shoot.springboot.config.auth.LoginUser;
import com.clholdings.pass_shoot.springboot.config.auth.dto.SessionUser;
import com.clholdings.pass_shoot.springboot.service.posts.PostsService;
import com.clholdings.pass_shoot.springboot.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;

    // @LoginUser SessionUser user
    // 기존에 (User) httpSession.getAttribute("user") 로 가져오던 세션 정보 값이 개선되었습니다
    // 이제는어느컨트롤러든지 @LoginUser 만사용하면 세션정보를 가져올수있게 되었습니다
    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user) {
        model.addAttribute("posts", postsService.findAllDesc());
        if (user != null) {
            model.addAttribute("userName", user.getName());
        }
        return "index";
    }

    @GetMapping("/posts/save")
    public String postsSave() {
        return "posts-save";
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model) {
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);

        return "posts-update";
    }
}
