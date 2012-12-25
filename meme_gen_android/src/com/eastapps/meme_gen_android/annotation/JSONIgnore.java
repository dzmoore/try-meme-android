package com.eastapps.meme_gen_android.annotation;

import com.eastapps.meme_gen_android.R;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) 
@Target({ElementType.METHOD})       
public @interface JSONIgnore {

}
