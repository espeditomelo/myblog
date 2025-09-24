package com.espeditomelo.myblog.utils;

import com.espeditomelo.myblog.model.Category;
import com.espeditomelo.myblog.model.User;
import com.espeditomelo.myblog.model.repository.CategoryRepository;
import com.espeditomelo.myblog.model.repository.PostRepository;
import com.espeditomelo.myblog.model.repository.UserRepository;
import com.espeditomelo.myblog.service.serviceImpl.UserServiceImpl;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DummyData {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserServiceImpl userService;

    //@PostConstruct
    public void savePosts(){


        User user2 =  new User();
        user2.setUsername("1");
        user2.setAdmin(true);
        user2.setPassword("1");
        user2.setEmail("1@1.com");
        userService.save(user2);

        Category category1 = new Category();
        category1.setName("Database");
        categoryRepository.save(category1);

        Category category2 = new Category();
        category2.setName("Java");
        categoryRepository.save(category2);

        Category category3 = new Category();
        category3.setName("Cloud");
        categoryRepository.save(category3);

       /*
        Optional<User> userOptional = userRepository.findById(1L);
        Optional<Category> categoryOptional = categoryRepository.findById(1L);

        if(userOptional.isPresent() && categoryOptional.isPresent()){
            User user = userOptional.get();
            Category category = categoryOptional.get();

            Post post = new Post();
            post.setTitle("tittle");
            post.setBody("body");
            post.setStatus("a");
            post.setUser(user);
            post.setCategory(category);
            System.out.println(postRepository.save(post));
        } else {
            System.out.println("User or Category not found with IDs");
        } */

//        List<Post> posts = new ArrayList<>();
//
//        Post post1 = new Post();
//
//        Optional<User> userOptional = userRepository.findById(1L);
//        Optional<Category> categoryOptional = categoryRepository.findById(1L);
//
//        Optional<User> userOptional2 = userRepository.findById(2L);
//        Optional<Category> categoryOptional2 = categoryRepository.findById(2L);
//
//        //if(userOptional.isPresent() && categoryOptional.isPresent()){
//            User user = userOptional.get();
//            Category category = categoryOptional.get();
//
//            Post post = new Post();
//            post.setTitle("Initiate java");
//            post.setBody("ldkjfdjfjsdfskdfkjsdkjfksdkfjkd");
//            post.setStatus("a");
//            post.setUser(user);
//            post.setCategory(category);
//
//        posts.add(post);
//
//        Post post2 = new Post();
//        post2.setTitle("Initiate oracle");
//        post2.setBody("ldkjfdjfjsdfskdfalsdkdsldkaskdkdksdkkjsdkjfksdkfjkd");
//        post2.setStatus("a");
//        post2.setUser(user2);
//        post2.setCategory(category2);
//
//        posts.add(post2);

           // System.out.println(postRepository.save(post));
//        } else {
//            System.out.println("User or Category not found with IDs");
//        }

//        for(Post p : posts) {
//            postRepository.save(p);
//        }


    }

}
