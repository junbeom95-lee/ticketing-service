package com.chil.ticketingservice.domain.like.service;

import static com.chil.ticketingservice.common.enums.SuccessMessage.LIKE_CREATE_SUCCESS;
import static com.chil.ticketingservice.common.enums.SuccessMessage.LIKE_DELETE_SUCCESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class LikeServiceTest {

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ShowRepository showRepository;

    @InjectMocks
    private LikeService likeService;

    User user;
    Show show;

    @BeforeEach
    void setUp() {

        user = new User("test@test.com", "test", LocalDate.now(), "1234", UserRole.USER);
        ReflectionTestUtils.setField(user, "id", 1L);

        ShowCreateRequest request = new ShowCreateRequest("test title", "DDP", LocalDateTime.now().plusDays(3), 15L, "descriptions", "image");
        show = new Show(request);
        ReflectionTestUtils.setField(show, "id", 1L);
    }

    @Test
    @DisplayName("좋아요 생성 성공")
    void postLike_create() {

        // Give
        Like like = new Like(show, user);
        ReflectionTestUtils.setField(like, "id", 1L);

        given(userRepository.findUserById(anyLong())).willReturn(user);
        given(showRepository.findShowById(anyLong())).willReturn(show);
        given(likeRepository.existsByUserIdAndShowId(anyLong(), anyLong())).willReturn(false);
        given(likeRepository.save(any(Like.class))).willReturn(like);

        // When
        SuccessMessage response = likeService.postLike(1L, 1L);

        // Then
        assertThat(response).isNotNull();
        assertThat(response).isEqualTo(LIKE_CREATE_SUCCESS);
    }

    @Test
    @DisplayName("좋아요 삭제 성공")
    void postLike_delete() {

        // Given
        given(userRepository.findUserById(anyLong())).willReturn(user);
        given(showRepository.findShowById(anyLong())).willReturn(show);
        given(likeRepository.existsByUserIdAndShowId(anyLong(), anyLong())).willReturn(true);
        willDoNothing().given(likeRepository).deleteByUserIdAndShowId(anyLong(), anyLong());

        // When
        SuccessMessage response = likeService.postLike(1L, 1L);

        // Then
        assertThat(response).isNotNull();
        assertThat(response).isEqualTo(LIKE_DELETE_SUCCESS);
    }

    @Test
    @DisplayName("공연별 좋아요 수 조회")
    void countLikes() {

        // Given
        Like like = new Like(show, user);
        ReflectionTestUtils.setField(like, "id", 1L);

        given(showRepository.findShowById(anyLong())).willReturn(show);
        given(likeRepository.countByShow(any(Show.class))).willReturn(1L);

        // When
        LikeCountResponse response = likeService.countLikes(1L);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.likes()).isEqualTo(1L);
    }
}