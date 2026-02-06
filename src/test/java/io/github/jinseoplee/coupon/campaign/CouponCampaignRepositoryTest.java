package io.github.jinseoplee.coupon.campaign;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CouponCampaignRepositoryTest {

    @Autowired
    CouponCampaignRepository couponCampaignRepository;

    @Test
    @DisplayName("발급 수량이 한도 미만이면 issued_count가 증가한다")
    void increaseIssuedCount_whenBelowLimit() {
        // given
        CouponCampaign couponCampaign = new CouponCampaign();
        setField(couponCampaign, "issueLimit", 10);
        setField(couponCampaign, "issuedCount", 0);
        setField(couponCampaign, "createdAt", LocalDateTime.now());
        setField(couponCampaign, "updatedAt", LocalDateTime.now());

        couponCampaignRepository.saveAndFlush(couponCampaign);

        // when
        int updated = couponCampaignRepository.tryIncreaseIssuedCount(couponCampaign.getId());

        // then
        assertThat(updated).isEqualTo(1);

        CouponCampaign reloaded = couponCampaignRepository.findById(couponCampaign.getId()).orElseThrow();
        assertThat(reloaded.getIssuedCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("발급 수량이 한도에 도달하면 issued_count가 증가하지 않는다")
    void doNotIncreaseIssuedCount_whenLimitReached() {
        // given
        CouponCampaign couponCampaign = new CouponCampaign();
        setField(couponCampaign, "issueLimit", 1);
        setField(couponCampaign, "issuedCount", 1);
        setField(couponCampaign, "createdAt", LocalDateTime.now());
        setField(couponCampaign, "updatedAt", LocalDateTime.now());


        couponCampaignRepository.saveAndFlush(couponCampaign);

        // when
        int updated = couponCampaignRepository.tryIncreaseIssuedCount(couponCampaign.getId());

        // then
        assertThat(updated).isEqualTo(0);

        CouponCampaign reloaded = couponCampaignRepository.findById(couponCampaign.getId()).orElseThrow();
        assertThat(reloaded.getIssuedCount()).isEqualTo(1);
    }

    private static void setField(Object target, String fieldName, Object value) {
        try {
            java.lang.reflect.Field f = target.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(target, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
