package io.github.jinseoplee.coupon.issue;

import io.github.jinseoplee.coupon.campaign.CouponCampaign;
import io.github.jinseoplee.coupon.campaign.CouponCampaignRepository;
import io.github.jinseoplee.coupon.issue.exception.CouponAlreadyIssuedException;
import io.github.jinseoplee.coupon.issue.exception.CouponCampaignNotFoundException;
import io.github.jinseoplee.coupon.issue.exception.CouponSoldOutException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponIssueService {

    private final CouponCampaignRepository couponCampaignRepository;
    private final CouponIssueRepository couponIssueRepository;

    @Transactional
    public void issue(Long campaignId, Long userId) {
        // 캠페인 존재 확인
        CouponCampaign campaign = couponCampaignRepository.findById(campaignId)
                .orElseThrow(() -> new CouponCampaignNotFoundException(campaignId));

        // 이미 발급된 요청은 즉시 종료
        if (couponIssueRepository.existsByCouponCampaign_IdAndUserId(campaignId, userId)) {
            throw new CouponAlreadyIssuedException();
        }

        // 조건부 UPDATE로 선착순 판정 및 수량 차감
        int updated = couponCampaignRepository.tryIncreaseIssuedCount(campaignId);
        if (updated == 0) {
            throw new CouponSoldOutException();
        }

        // UNIQUE 제약으로 중복 발급 방어
        try {
            couponIssueRepository.save(CouponIssue.of(campaign, userId));
        } catch (DataIntegrityViolationException e) {
            throw new CouponAlreadyIssuedException();
        }
    }
}
