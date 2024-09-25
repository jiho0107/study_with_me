package test.study.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import test.study.post.domain.Post;
import test.study.post.repository.PostRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;

    @Transactional
    public Post save(Post post){
        return postRepository.save(post);
    }

    @Transactional
    public Post edit(Long postId, Post update){
        return postRepository.editPost(postId, update);
    }

    @Transactional
    public void remove(Long postId) {
        postRepository.removePost(postId);
    }

    public List<Post> findAllPosts(){
        return postRepository.findAllPosts();
    }

    public List<Post> findMyPosts(Long userId) {
        return postRepository.findMyPosts(userId);
    }

    public Post findPost(Long postId) {
        return postRepository.findPost(postId);
    }
}
