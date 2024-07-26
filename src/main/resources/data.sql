INSERT INTO tier (tier_id, tier_name, character_url, required_exp)
VALUES (UNHEX(REPLACE(UUID(), '-', '')), '바보 감자', 'https://d1ds22ndweg1nu.cloudfront.net/potato/stupidPotato.png', 0),
       (UNHEX(REPLACE(UUID(), '-', '')), '말하는 감자', 'https://d1ds22ndweg1nu.cloudfront.net/potato/speakingPotato.png', 1000),
       (UNHEX(REPLACE(UUID(), '-', '')), '개발자 감자', 'https://d1ds22ndweg1nu.cloudfront.net/potato/developerPotato.png', 15000),
       (UNHEX(REPLACE(UUID(), '-', '')), 'CEO 감자', 'https://d1ds22ndweg1nu.cloudfront.net/potato/CEOPotato.png', 33000);
