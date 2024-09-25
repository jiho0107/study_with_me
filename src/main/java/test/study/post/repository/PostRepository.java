package test.study.post.repository;

import test.study.post.domain.Post;

import java.util.List;

public interface PostRepository {
    public Post save(Post post); // 게시글 저장(등록)
    public List<Post> findAllPosts(); // 게시글 전체 조회
    public List<Post> findMyPosts(Long userId); //내가 쓴 게시글 목록 조회
    public Post findPost(Long postId); // 게시글 찾기
    public Post editPost(Long postId, Post update); // 게시글 수정
    public void removePost(Long postId); // 게시글 삭제
}
