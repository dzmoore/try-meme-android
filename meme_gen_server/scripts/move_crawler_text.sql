DROP PROCEDURE IF EXISTS move_crawler_text;
DELIMITER //
CREATE PROCEDURE `move_crawler_text`()
MAIN: BEGIN
    
    BG_PATH_LOOP: BEGIN

    -- CRALWER_BACKGROUND --> MEME_BACKGROUND
    DECLARE eaBgPath VARCHAR(200) DEFAULT '';
    DECLARE isBgPathLoopFinished INT DEFAULT 0;
    DECLARE memeBackgroundId INT DEFAULT 0;
    DECLARE memeBgExistingCount INT DEFAULT 0;

    DECLARE bgPathCursor CURSOR FOR
        SELECT DISTINCT
            cb.crawler_img_filename as path
        FROM
            crawler_meme cm join crawler_background cb
            on (upper(cm.name) = upper(cb.crawler_img_desc));
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET isBgPathLoopFinished = 1;

    OPEN bgPathCursor;

    -- for each distinct background, insert 
    -- into meme_background if it isnt already 
    -- in the table
    WHILE isBgPathLoopFinished = 0 DO
        FETCH bgPathCursor into eaBgPath;

        SELECT count(*)
        INTO memeBgExistingCount
        FROM meme_background
        WHERE
            upper(path) = upper(eaBgPath);
        
        IF memeBgExistingCount <= 0 THEN
            INSERT into meme_background
                (active, path)
            values
                (1, eaBgPath);
        END IF;
    END WHILE;

    CLOSE bgPathCursor;
    END BG_PATH_LOOP;
    
    MEME_TYPE_LOOP: BEGIN   
    -- CRAWLER_MEME --> LV_MEME_TYPE
    DECLARE isTypeLoopFinished INT DEFAULT 0;
    DECLARE eaMemeTypeDescr VARCHAR(255) DEFAULT '';
    DECLARE eaMemeBgFilename VARCHAR(255) DEFAULT '';
    DECLARE existingDescrCount INT DEFAULT 0;
    DECLARE MAX_SAMPLE_ROWS INT DEFAULT 4;
    DECLARE DEFAULT_FONT_SIZE INT DEFAULT 26;
    DECLARE DEFAULT_INSERT_USER_ID INT DEFAULT 1;
    DECLARE memeTypeFk INT DEFAULT 0;
    DECLARE imgDescrCursor CURSOR FOR
        SELECT
            cb.crawler_img_desc as descr,
            cb.crawler_img_filename as filename
        FROM (
            SELECT cm.name as name
            from
                crawler_meme cm
            group by cm.name
            having count(*) >= MAX_SAMPLE_ROWS
        ) as cm_sampleable

        join crawler_background cb
        on (upper(cb.crawler_img_desc) = upper(cm_sampleable.name));
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET isTypeLoopFinished = 1;

    OPEN imgDescrCursor;

    -- insert types into lv_meme_type table
    -- if they dont already exist
    set eaMemeTypeDescr = '';
    set isTypeLoopFinished = 0;
    WHILE isTypeLoopFinished = 0 DO
        FETCH imgDescrCursor into eaMemeTypeDescr, eaMemeBgFilename;

        SELECT count(*) INTO existingDescrCount
        FROM crawler_meme_type
        WHERE upper(crawler_meme_type_descr) = upper(eaMemeTypeDescr);


        IF existingDescrCount <= 0 THEN
            SAMPLE_LOOP_BLOCK: BEGIN
            -- for each type, insert four samples from the crawler_meme table
            -- into the meme and meme_text table
            DECLARE sampleLoopBypass INT DEFAULT 0;
            DECLARE sampleRowCounter INT DEFAULT 0;
            DECLARE eaTopText VARCHAR(255);
            DECLARE eaBottomText VARCHAR(255);
            DECLARE memeBgFk INT DEFAULT 0;
            DECLARE memeTypeFk INT DEFAULT 0;
            DECLARE memeId INT DEFAULT 0;

            -- CRAWLER_MEME --> MEME_TEXT
            -- insert MAX_SAMPLE_ROWS sets of top/bottom
            -- text for the meme we just inserted
            DECLARE crawlerMemeSamplesCursor CURSOR FOR
                SELECT 
                    top_text,
                    bottom_text
                FROM crawler_meme
                WHERE upper(name) = upper(eaMemeTypeDescr);
            DECLARE CONTINUE HANDLER FOR NOT FOUND SET sampleLoopBypass = 1;

            
            INSERT into lv_meme_type
                (active, descr) values (TRUE, eaMemeTypeDescr);
            
            SELECT last_insert_id() INTO memeTypeFk;

            -- meme table needs:
            --    meme_background_fk
            --    lv_meme_type_fk
            --    created_by_user_fk 
            --    is_sample_meme
            SELECT id
            INTO memeBgFk
            FROM meme_background
            WHERE upper(path) = upper(eaMemeBgFilename)
            limit 1;

            -- create meme entry
            INSERT INTO meme
            (
                meme_background_fk, 
                lv_meme_type_fk, 
                created_by_user_fk, 
                is_sample_meme
            )   
            values
            (
                memeBgFk,   
                memeTypeFk,
                DEFAULT_INSERT_USER_ID,
                true
            );
        
            SELECT last_insert_id() INTO memeId;


            OPEN crawlerMemeSamplesCursor;
    
            WHILE sampleLoopBypass = 0 AND sampleRowCounter < MAX_SAMPLE_ROWS DO
                FETCH crawlerMemeSamplesCursor into eaTopText, eaBottomText;
            
                INSERT INTO meme_text
                (text, text_type, font_size, meme_fk)
                VALUES
                (eaTopText, 'TOP', DEFAULT_FONT_SIZE, memeId);

                INSERT INTO meme_text
                (text, text_type, font_size, meme_fk)
                VALUES
                (eaBottomText, 'BOTTOM', DEFAULT_FONT_SIZE, memeId);
        
                SET sampleRowCounter = sampleRowCounter + 1;
        
            END WHILE;

            CLOSE crawlerMemeSamplesCursor;
            end SAMPLE_LOOP_BLOCK;
        END IF;
    END WHILE;

    close imgDescrCursor;
    end MEME_TYPE_LOOP;
    


END MAIN//
