BEGIN;
SET @max_venue_seq = (SELECT MAX(seq) FROM venue);
SET @max_artist_seq = (SELECT MAX(seq) FROM artist);
SET @max_concert_seq = (SELECT MAX(seq) FROM concert);
SET @max_member_seq = (SELECT MAX(seq) FROM member);

DELETE FROM venue where seq = @max_venue_seq;
DELETE FROM artist where seq = @max_artist_seq;
DELETE FROM concert where seq = @max_concert_seq;
DELETE FROM ticket where concert_seq = @max_concert_seq;
DELETE FROM member where seq = @max_member_seq;
COMMIT;

