select distinct cm.name from crawler_meme cm join crawler_background cb on (upper(cm.name) = upper(cb.crawler_img_desc)) order by cm.name;

select distinct cb.crawler_img_filename from crawler_meme cm join crawler_background cb on (upper(cm.name) = upper(cb.crawler_img_desc)) order by cm.name;

insert into meme_background (path) values ((select distinct cb.crawler_img_filename from crawler_meme cm join crawler_background cb on (upper(cm.name) = upper(cb.crawler_img_desc))));

insert into meme_background     
    (id, active, path, last_mod)    
	select
		(
			select 
				auto_increment + ((@curRow := @curRow + 1) * 10) 
			from information_schema.tables 
			where 
				table_name = 'meme_background' 
				and table_schema = 'heroku_b630f8d967bd52a'
		) 						as id,

		1 						as active,

		path.path 				as path,

		current_timestamp 		as last_mod

	from
		(
			select distinct
				cb.crawler_img_filename as path
			from
				crawler_meme cm join crawler_background cb
				on (upper(cm.name) = upper(cb.crawler_img_desc))
		) as path

		join (select @curRow := 1) r;


insert into lv_meme_type
	(id, descr, last_mod, active)
	select
		(
			select 
				auto_increment + ((@curRow := @curRow + 1) * 10) 
			from information_schema.tables 
			where 
				table_name = 'lv_meme_type' 
				and table_schema = 'heroku_b630f8d967bd52a'
		) 						as id,
		crawler_bg.descr		as descr,
		current_timestamp		as last_mod,
		1						as active
	from	
		(
			select distinct
				cb.crawler_img_desc as descr
			from
				crawler_meme cm join crawler_background cb
				on (upper(cm.name) = upper(cb.crawler_img_desc))
		) as crawler_bg

		join (select @curRow := 1) r;
		

// left off here
insert into meme
	(id, meme_background_fk, lv_meme_type_fk, created_by_user_fk, is_sample_meme, last_mod)
	select
		(
			select
				auto_increment + ((@curRow := @curRow + 1) * 10)
			from information_schema.tables
			where
				table_name = 'meme'
				and table_schema = 'heroku_b630f8d967bd52a'	
		)						as id,
		meme_bg.id				as meme_background_fk,
		meme_type.id			as lv_meme_type_fk,
		1						as created_by_user_fk,
		1						as is_sample_meme,
		current_timestamp		as last_mod
	from
		(
            select distinct
                cb.crawler_img_desc as descr
            from
                crawler_meme cm join crawler_background cb
                on (upper(cm.name) = upper(cb.crawler_img_desc))
        ) as crawler_bg_descr

        join

        (
            select distinct
                crawler_img_filename as filename,
				crawler_img_desc     as descr,
                id     		     	 as id
            from
                crawler_background
        ) as crawler_bg_filename
        on (upper(crawler_bg_descr.descr) = upper(crawler_bg_filename.descr))

		join meme_background meme_bg
		on (upper(meme_bg.path) = upper(crawler_bg_filename.filename))

		join lv_meme_type meme_type
		on (upper(meme_type.descr) = upper(crawler_bg_descr.descr))

		join (select @curRow := 1) r;

/* algorithm for inserting into meme_text
 * 1. insert all top text
 *     + join crawler_meme.name 
 *       == crawler_background.crawler_img_desc
 *     + join 
*/
insert into meme_text
	(id, text, text_type, font_size, meme_fk, last_mod)
	select
		(
			select
				auto_increment + ((@curRow := @curRow + 1) * 10)
			from information_schema.tables
			where
				table_name = 'meme_text'
				and table_schema = 'heroku_b630f8d967bd52a'	
		)						as id,
		crawler_bg_descr.text	as text,
		26						as font_size,
		meme.id					as meme_fk,
		current_timestamp		as last_mod
/* comment this out*/
select descr/*, text*/
	from
		(
            select
				count(*),
                cb.crawler_img_desc as descr
            from
                crawler_meme cm 
			group by cm.name
			having count(*) > 4
			
        ) as crawler_bg_descr

        join

        (
            select distinct
                crawler_img_filename as filename,
				crawler_img_desc     as descr,
                id     		     	 as id
            from
                crawler_background
        ) as crawler_bg_filename
        on (upper(crawler_bg_descr.descr) = upper(crawler_bg_filename.descr))

		join meme_background meme_bg
		on (upper(meme_bg.path) = upper(crawler_bg_filename.filename))

		join lv_meme_type meme_type
		on (upper(meme_type.descr) = upper(crawler_bg_descr.descr))

		join 
		(select 
			id,
			meme_background_fk,
			lv_meme_type_fk
		from
			meme
		limit 5) as meme
		on (meme.meme_background_fk = meme_bg.id 
			and meme.lv_meme_type_fk = meme_type.id)

		join (select @curRow := 1) r

		order by meme.id



DROP PROCEDURE IF EXISTS move_crawler_text;
DELIMITER //
CREATE PROCEDURE `move_crawler_text`()
BEGIN
	DECLARE isDone INT;
	DECLARE eaMemeTypeDescr varchar(255);
    DECLARE existingDescrCount integer(21) DEFAULT false;

	DECLARE img_descr_cur CURSOR FOR
		SELECT 
			cb.crawler_img_desc as descr
		FROM (
			SELECT cm.name as name
			from
				crawler_meme cm 
			group by cm.name
			having count(*) > 4
		) as cm_sampleable

		join crawler_background cb 
		on (upper(cb.crawler_img_desc) = upper(cm_sampleable.name));
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET isDone = 1;

	open img_descr_cur;
	-- insert types into crawler_meme_type table
	set eaMemeTypeDescr = '';
	set isDone = 0;
	WHILE isDone = 0 DO
		FETCH img_descr_cur into eaMemeTypeDescr;
    
        SELECT count(*) INTO existingDescrCount
        FROM crawler_meme_type
        WHERE upper(crawler_meme_type_descr) = upper(eaMemeTypeDescr);
        
        
        IF existingDescrCount <= 0 THEN
            INSERT into crawler_meme_type 
                (crawler_meme_type_descr) values (eaMemeTypeDescr);
        END IF;
	END WHILE;

    
		
	
END//

