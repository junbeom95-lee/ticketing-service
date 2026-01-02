package com.chil.ticketingservice.domain.like.service;

import static com.chil.ticketingservice.common.enums.SuccessMessage.LIKE_CREATE_SUCCESS;
import static com.chil.ticketingservice.common.enums.SuccessMessage.LIKE_DELETE_SUCCESS;
import static org.assertj.core.api.Assertions.assertThat;

import com.chil.ticketingservice.common.enums.SuccessMessage;
import com.chil.ticketingservice.domain.like.dto.response.LikeCountResponse;
import com.chil.ticketingservice.domain.like.entity.Like;
import com.chil.ticketingservice.domain.like.repository.LikeRepository;
import com.chil.ticketingservice.domain.show.dto.request.ShowCreateRequest;
import com.chil.ticketingservice.domain.show.entity.Show;
import com.chil.ticketingservice.domain.show.repository.ShowRepository;
import com.chil.ticketingservice.domain.user.entity.User;
import com.chil.ticketingservice.domain.user.enums.UserRole;
import com.chil.ticketingservice.domain.user.repository.UserRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class LikeServiceIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private LikeService likeService;

    User user;
    Show show;

    @BeforeEach
    void setUp() {

        user = new User("fortest@test.com", "test", LocalDate.now(), "1234", UserRole.USER);
        userRepository.save(user);

        ShowCreateRequest request = new ShowCreateRequest("test title", "DDP", LocalDateTime.now().plusDays(3), 15L, "descriptions", "image");
        show = new Show(request);
        showRepository.save(show);
    }

    @Test
    @DisplayName("좋아요 생성 성공")
    void createLike_success() {

        // Given

        // When
        SuccessMessage response = likeService.postLike(user.getId(), show.getId());

        // Then
        assertThat(response).isNotNull();
        assertThat(response).isEqualTo(LIKE_CREATE_SUCCESS);
    }

    @Test
    @DisplayName("좋아요 삭제 성공")
    void deleteLike_success() {

        // Given
        Like like = new Like(show, user);
        likeRepository.save(like);

        // When
        SuccessMessage response = likeService.postLike(user.getId(), show.getId());

        // Then
        assertThat(response).isNotNull();
        assertThat(response).isEqualTo(LIKE_DELETE_SUCCESS);
    }

    @Test
    @DisplayName("좋아요 개수 조회 성공")
    void countLike_success() {

        // Given
        Like like1 = new Like(show, user);
        likeRepository.save(like1);

        // When
        LikeCountResponse response = likeService.countLikes(show.getId());

        // Then
        assertThat(response).isNotNull();
        assertThat(response.likes()).isEqualTo(1);
    }
}