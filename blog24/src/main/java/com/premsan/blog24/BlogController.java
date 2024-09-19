package com.premsan.blog24;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class BlogController {

    private final BlogRepository blogRepository;

    @GetMapping("/blogs/{id}")
    public ModelAndView view(final @PathVariable String id)
    {
        ModelAndView model = new ModelAndView("blog-view");
        model.addObject("blog", blogRepository.findById(id).get());
        return model;
    }

    @GetMapping("/blogs/create")
    public ModelAndView getCreate()
    {
        ModelAndView model = new ModelAndView("blog-create");
        model.addObject("blog", new Blog());
        return model;
    }

    @PostMapping("/blogs/create")
    public ModelAndView postCreate(@ModelAttribute Blog blog)
    {
        final Blog savedBlog = blogRepository.save(blog);
        ModelAndView model = new ModelAndView("blog-view");
        model.addObject("blog", savedBlog);
        return model;
    }
}
