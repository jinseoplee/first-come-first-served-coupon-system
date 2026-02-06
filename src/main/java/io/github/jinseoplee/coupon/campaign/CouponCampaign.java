package io.github.jinseoplee.coupon.campaign;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "coupon_campaign")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponCampaign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int issueLimit;

    @Column(nullable = false)
    private int issuedCount;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public static CouponCampaign of(int issueLimit) {
        CouponCampaign campaign = new CouponCampaign();
        campaign.issueLimit = issueLimit;
        campaign.issuedCount = 0;
        campaign.createdAt = LocalDateTime.now();
        campaign.updatedAt = LocalDateTime.now();
        return campaign;
    }
}
