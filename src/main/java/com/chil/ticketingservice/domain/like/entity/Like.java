package com.chil.ticketingservice.domain.like.entity;

import com.chil.ticketingservice.domain.show.entity.Show;
import com.chil.ticketingservice.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "likes",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "unique_show_user",
            columnNames = {"show_id", "user_id"}
        )
    }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "show_id", nullable = false)
    private Show show;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Like(Show show, User user) {
        this.show = show;
        this.user = user;
    }
}
