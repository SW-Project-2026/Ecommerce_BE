ALTER TABLE campaign
    ADD COLUMN IF NOT EXISTS ad_id              BIGINT,
    ADD COLUMN IF NOT EXISTS coupon_id          BIGINT,
    ADD COLUMN IF NOT EXISTS batch_cycle        VARCHAR(20),
    ADD COLUMN IF NOT EXISTS batch_time         TIME,
    ADD COLUMN IF NOT EXISTS batch_day_of_week  VARCHAR(10),
    ADD COLUMN IF NOT EXISTS batch_day_of_month INTEGER;
