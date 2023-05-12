package apptive.team1.friendly.domain.post.controller;

import apptive.team1.friendly.domain.post.dto.PostDto;
import apptive.team1.friendly.domain.post.entity.Post;
import apptive.team1.friendly.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;


}
