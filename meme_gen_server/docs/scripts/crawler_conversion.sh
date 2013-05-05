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

		join (select @curRow := 1) r
