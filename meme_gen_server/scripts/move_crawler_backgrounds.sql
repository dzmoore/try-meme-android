DROP PROCEDURE IF EXISTS move_crawler_backgrounds();
DELIMITER //
CREATE PROCEDURE `move_crawler_backgrounds`();
BEGIN
insert into meme_background (id, active, path, last_mod)
    select
        (
            select
                auto_increment + ((@curRow := @curRow + 1) * 10)
            from information_schema.tables
            where
                table_name = 'meme_background'
                and table_schema = 'heroku_b630f8d967bd52a'
        )                       as id,

        1                       as active,

        path.path               as path,

        current_timestamp       as last_mod

    from
        (
            select distinct
                cb.crawler_img_filename as path
            from
                crawler_meme cm join crawler_background cb
                on (upper(cm.name) = upper(cb.crawler_img_desc))
        ) as path

        join (select @curRow := 1) r;
END//
