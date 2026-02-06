CREATE TABLE coupon_campaign
(
    id           BIGINT      NOT NULL AUTO_INCREMENT,
    issue_limit  INT         NOT NULL,
    issued_count INT         NOT NULL DEFAULT 0,
    created_at   DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at   DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6)
        ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    CHECK (issue_limit >= 0),
    CHECK (issued_count >= 0)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE coupon_issue
(
    id          BIGINT      NOT NULL AUTO_INCREMENT,
    campaign_id BIGINT      NOT NULL,
    user_id     BIGINT      NOT NULL,
    issued_at   DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    CONSTRAINT uq_coupon_issue_campaign_user
        UNIQUE (campaign_id, user_id),
    CONSTRAINT fk_coupon_issue_campaign
        FOREIGN KEY (campaign_id)
            REFERENCES coupon_campaign (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
