INSERT INTO tier (tier_id, tier_name, character_url, required_exp)
VALUES (UNHEX(REPLACE(UUID(), '-', '')), '바보 감자', 'https://d1ds22ndweg1nu.cloudfront.net/potato/stupidPotato.svg', 0),
       (UNHEX(REPLACE(UUID(), '-', '')), '말하는 감자', 'https://d1ds22ndweg1nu.cloudfront.net/potato/speakingPotato.svg',
        30000),
       (UNHEX(REPLACE(UUID(), '-', '')), '개발자 감자', 'https://d1ds22ndweg1nu.cloudfront.net/potato/developerPotato.svg',
        150000),
       (UNHEX(REPLACE(UUID(), '-', '')), 'CEO 감자', 'https://d1ds22ndweg1nu.cloudfront.net/potato/CEOPotato.svg',
        330000);
