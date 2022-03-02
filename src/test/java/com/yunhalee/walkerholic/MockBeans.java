package com.yunhalee.walkerholic;

import com.yunhalee.walkerholic.activity.domain.ActivityRepository;
import com.yunhalee.walkerholic.activity.service.ActivityService;
import com.yunhalee.walkerholic.common.service.S3ImageUploader;
import com.yunhalee.walkerholic.follow.domain.FollowRepository;
import com.yunhalee.walkerholic.follow.service.FollowService;
import com.yunhalee.walkerholic.likepost.domain.LikePostRepository;
import com.yunhalee.walkerholic.post.domain.PostImageRepository;
import com.yunhalee.walkerholic.post.domain.PostRepository;
import com.yunhalee.walkerholic.post.service.PostService;
import com.yunhalee.walkerholic.productImage.domain.ProductImageRepository;
import com.yunhalee.walkerholic.product.domain.ProductRepository;
import com.yunhalee.walkerholic.product.service.ProductService;
import com.yunhalee.walkerholic.review.domain.ReviewRepository;
import com.yunhalee.walkerholic.user.domain.UserRepository;
import com.yunhalee.walkerholic.user.service.UserService;
import com.yunhalee.walkerholic.useractivity.domain.UserActivityRepository;
import com.yunhalee.walkerholic.useractivity.service.UserActivityService;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MockBeans {

    @MockBean
    protected UserActivityRepository userActivityRepository;

    @MockBean
    protected ActivityRepository activityRepository;

    @MockBean
    protected UserRepository userRepository;

    @MockBean
    protected ProductRepository productRepository;

    @MockBean
    protected ReviewRepository reviewRepository;

    @MockBean
    protected S3ImageUploader s3ImageUploader;

    @MockBean
    protected FollowRepository followRepository;

    @MockBean
    protected PostRepository postRepository;

    @MockBean
    protected LikePostRepository likePostRepository;

    @MockBean
    protected PostImageRepository postImageRepository;

    @MockBean
    protected UserActivityService userActivityService;

    @MockBean
    protected ActivityService activityService;

    @MockBean
    protected UserService userService;

    @MockBean
    protected PostService postService;

    @MockBean
    protected FollowService followService;

    @MockBean
    protected ProductService productService;

    @MockBean
    protected ProductImageRepository productImageRepository;
}
