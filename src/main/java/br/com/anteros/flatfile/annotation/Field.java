package br.com.anteros.flatfile.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.anteros.flatfile.language.EnumTypes;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Field {
	
	String name();
	int length();
	Formats format() default Formats.NONE;
	Paddings padding() default Paddings.NONE;
	EnumTypes type() default EnumTypes.STRING;
	String value() default "";
	boolean blankAccepted() default true;
	boolean truncate() default false;
}
