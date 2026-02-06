package io.github.jinseoplee.coupon.issue;


import io.github.jinseoplee.coupon.campaign.CouponCampaign;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "coupon_issue",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_coupon_issue_campaign_user",
                columnNames = {"campaign_id", "user_id"}
        )
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponIssue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "campaign_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_coupon_issue_campaign")
    )
    private CouponCampaign couponCampaign;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime issuedAt;
}
