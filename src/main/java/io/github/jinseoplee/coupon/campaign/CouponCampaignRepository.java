package io.github.jinseoplee.coupon.campaign;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CouponCampaignRepository extends JpaRepository<CouponCampaign, Long> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
                    update CouponCampaign c
                       set c.issuedCount = c.issuedCount + 1
                     where c.id = :campaignId
                       and c.issuedCount < c.issueLimit
            """)
    int tryIncreaseIssuedCount(@Param("campaignId") Long campaignId);
}
