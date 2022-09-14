package com.example.sns.service;

import com.example.sns.exception.ErrorCode;
import com.example.sns.exception.SnsApplicationException;
import com.example.sns.model.Post;
import com.example.sns.model.entity.LikeEntity;
import com.example.sns.model.entity.PostEntity;
import com.example.sns.repository.LikeEntityRepository;
import com.example.sns.repository.PostEntityRepository;
import com.example.sns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostEntityRepository postEntityRepository;

    private final UserEntityRepository userEntityRepository;

    private final LikeEntityRepository likeEntityRepository;

    @Transactional
    public void create(String title , String body , String userName) {
        //user find
        var userEntity =userEntityRepository.findByUserName(userName).orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND,String.format("%s not founded",userName)));
        //post save
         postEntityRepository.save(PostEntity.of(title,body,userEntity));
    }

    @Transactional
    public Post modify(String title , String body , String userName, Integer postId) {

        var userEntity =userEntityRepository.findByUserName(userName).orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND,String.format("%s not founded",userName)));

        // post exist
        var postEntity = postEntityRepository.findById(postId).orElseThrow(() -> new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not founded", postId)));

        //post permission
        if(postEntity.getUser() != userEntity) {
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSON, String.format("%s has no permission with %s", userName, postId));
        }

        postEntity.setTitle(title);
        postEntity.setBody(body);


        return Post.fromEntity( postEntityRepository.saveAndFlush(postEntity));
    }

    @Transactional
    public void delete(String userName,Integer postId) {
        var userEntity =userEntityRepository.findByUserName(userName).orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND,String.format("%s not founded",userName)));
        var postEntity = postEntityRepository.findById(postId).orElseThrow(() -> new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not founded", postId)));

        //post permission
        if(postEntity.getUser() != userEntity) {
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSON, String.format("%s has no permission with %s", userName, postId));
        }
        postEntityRepository.delete(postEntity);
    }

    public Page<Post> list(Pageable pageable) {
        return postEntityRepository.findAll(pageable).map(Post::fromEntity);
    }

    public Page<Post> my(String userName , Pageable pageable) {
        var userEntity =userEntityRepository.findByUserName(userName).orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND,String.format("%s not founded",userName)));
        return postEntityRepository.findAllByUser(userEntity,pageable).map(Post::fromEntity);
    }


    @Transactional
    public void like(Integer postId , String userName) {
        var postEntity = postEntityRepository.findById(postId).orElseThrow(() -> new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not founded", postId)));
        var userEntity =userEntityRepository.findByUserName(userName).orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND,String.format("%s not founded",userName)));

        likeEntityRepository.findByUserAndPost(userEntity, postEntity).ifPresent(it -> {
            throw new SnsApplicationException(ErrorCode.ALREADY_LIKED , String.format("userName %s already like post %d", userName , postId));
        });

        likeEntityRepository.save(LikeEntity.of(userEntity, postEntity));
    }

    @Transactional
    public int likeCount(Integer postId) {
        var postEntity = postEntityRepository.findById(postId).orElseThrow(() -> new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not founded", postId)));

//        List<LikeEntity> likeEntities = likeEntityRepository.findAllByPost(postEntity);
        return likeEntityRepository.countByPost(postEntity);

    }

}
