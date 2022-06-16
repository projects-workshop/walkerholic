package com.yunhalee.walkerholic;

import static org.mockito.Mockito.mockStatic;

import com.yunhalee.walkerholic.activity.domain.ActivityRepository;
import com.yunhalee.walkerholic.activity.service.ActivityService;
import com.yunhalee.walkerholic.cart.domain.CartRepository;
import com.yunhalee.walkerholic.cart.service.CartService;
import com.yunhalee.walkerholic.cartItem.domain.CartItemRepository;
import com.yunhalee.walkerholic.cartItem.service.CartItemService;
import com.yunhalee.walkerholic.common.notification.mapper.NotificationMapper;
import com.yunhalee.walkerholic.common.notification.sender.DefaultNotificationSender;
import com.yunhalee.walkerholic.common.service.S3ImageUploader;
import com.yunhalee.walkerholic.common.notification.sender.MailNotificationSender;
import com.yunhalee.walkerholic.common.notification.sender.SMSNotificationSender;
import com.yunhalee.walkerholic.follow.domain.FollowRepository;
import com.yunhalee.walkerholic.follow.service.FollowService;
import com.yunhalee.walkerholic.likepost.domain.LikePostRepository;
import com.yunhalee.walkerholic.order.domain.OrderRepository;
import com.yunhalee.walkerholic.orderitem.domain.OrderItemRepository;
import com.yunhalee.walkerholic.orderitem.service.OrderItemService;
import com.yunhalee.walkerholic.postImage.domain.PostImageRepository;
import com.yunhalee.walkerholic.post.domain.PostRepository;
import com.yunhalee.walkerholic.post.service.PostService;
import com.yunhalee.walkerholic.postImage.service.PostImageService;
import com.yunhalee.walkerholic.productImage.domain.ProductImageRepository;
import com.yunhalee.walkerholic.product.domain.ProductRepository;
import com.yunhalee.walkerholic.product.service.ProductService;
import com.yunhalee.walkerholic.productImage.service.ProductImageService;
import com.yunhalee.walkerholic.review.domain.ReviewRepository;
import com.yunhalee.walkerholic.user.domain.UserRepository;
import com.yunhalee.walkerholic.user.service.UserService;
import com.yunhalee.walkerholic.useractivity.domain.UserActivityRepository;
import com.yunhalee.walkerholic.useractivity.service.UserActivityService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
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

    @MockBean
    protected ProductImageService productImageService;

    @MockBean
    protected PostImageService postImageService;

    @MockBean
    protected OrderItemService orderItemService;

    @MockBean
    protected MailNotificationSender mailNotificationSender;

    @MockBean
    protected SMSNotificationSender SMSNotificationSender;

    @MockBean
    protected OrderRepository orderRepository;

    @MockBean
    protected OrderItemRepository orderItemRepository;

    @MockBean
    protected CartService cartService;

    @MockBean
    protected CartRepository cartRepository;

    @MockBean
    protected CartItemRepository cartItemRepository;

    @MockBean
    protected CartItemService cartItemService;

    @MockBean
    protected PasswordEncoder passwordEncoder;





}
