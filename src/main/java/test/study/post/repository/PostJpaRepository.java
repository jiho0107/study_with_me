package test.study.post.repository;

import org.springframework.stereotype.Repository;
import test.study.member.domain.Member;
import test.study.post.domain.Post;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class PostJpaRepository implements PostRepository {
    @PersistenceContext
    EntityManager em;

    @Override // 게시글 저장(등록)
    public Post save(Post post) {
        em.persist(post);
        return post;
    }

    @Override // 게시글 전체 조회
    public List<Post> findAllPosts() {
        return em.createQuery("select p from Post p", Post.class)
                .getResultList();
    }

    @Override //내가 쓴 게시글 목록 조회
    public List<Post> findMyPosts(Long userId) {
        return em.createQuery("select p from Post p where p.member.id = :userId", Post.class)
                .setParameter("userId",userId)
                .getResultList();
    }

    @Override // 게시글 찾기
    public Post findPost(Long postId) {
        return em.find(Post.class, postId);
    }

    @Override // 게시글 수정
    public Post editPost(Long postId, Post update) {
        Post findPost = findPost(postId);
        findPost.setTitle(update.getTitle());
        findPost.setContent(update.getContent());
        return findPost;
    }

    @Override // 게시글 삭제
    public void removePost(Long postId) {
        Post post = em.find(Post.class,postId);
        if(post != null){
            em.remove(em.find(Post.class,postId));
        }
        else{
            throw new EntityNotFoundException("게시글을 찾을 수 없습니다.");
        }
    }
}
