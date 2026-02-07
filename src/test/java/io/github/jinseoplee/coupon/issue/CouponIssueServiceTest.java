package io.github.jinseoplee.coupon.issue;

import io.github.jinseoplee.coupon.campaign.CouponCampaign;
import io.github.jinseoplee.coupon.campaign.CouponCampaignRepository;
import io.github.jinseoplee.coupon.issue.exception.CouponAlreadyIssuedException;
import io.github.jinseoplee.coupon.issue.exception.CouponSoldOutException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CouponIssueServiceTest {

    @Autowired
    CouponIssueService couponIssueService;

    @Autowired
    CouponCampaignRepository couponCampaignRepository;

    @Autowired
    CouponIssueRepository couponIssueRepository;

    @DisplayName("쿠폰 발급 성공 시 발급 수량이 증가한다")
    @Test
    void issue_whenFirstRequest() {
        // given
        CouponCampaign campaign = couponCampaignRepository.save(
                CouponCampaign.of(10)
        );

        // when
        couponIssueService.issue(campaign.getId(), 1L);

        // then
        assertThat(couponIssueRepository.count()).isEqualTo(1);

        CouponCampaign updated = couponCampaignRepository.findById(campaign.getId()).orElseThrow();
        assertThat(updated.getIssuedCount()).isEqualTo(1);
    }

    @DisplayName("동일 사용자는 쿠폰을 중복 발급받을 수 없다")
    @Test
    void issue_whenDuplicateRequest() {
        // given
        CouponCampaign campaign = couponCampaignRepository.save(
                CouponCampaign.of(10)
        );

        // when
        couponIssueService.issue(campaign.getId(), 1L);

        // then
        assertThatThrownBy(() ->
                couponIssueService.issue(campaign.getId(), 1L)
        ).isInstanceOf(CouponAlreadyIssuedException.class);

        assertThat(couponIssueRepository.count()).isEqualTo(1);

        CouponCampaign updated = couponCampaignRepository.findById(campaign.getId()).orElseThrow();
        assertThat(updated.getIssuedCount()).isEqualTo(1);
    }

    @DisplayName("발급 한도에 도달하면 추가 발급은 실패한다")
    @Test
    void issue_whenIssueLimitReached() {
        // given
        CouponCampaign campaign = couponCampaignRepository.save(
                CouponCampaign.of(1)
        );

        // when
        couponIssueService.issue(campaign.getId(), 1L);

        // then
        assertThatThrownBy(() ->
                couponIssueService.issue(campaign.getId(), 2L)
        ).isInstanceOf(CouponSoldOutException.class);

        assertThat(couponIssueRepository.count()).isEqualTo(1);

        CouponCampaign updated = couponCampaignRepository.findById(campaign.getId()).orElseThrow();
        assertThat(updated.getIssuedCount()).isEqualTo(1);
    }
}
