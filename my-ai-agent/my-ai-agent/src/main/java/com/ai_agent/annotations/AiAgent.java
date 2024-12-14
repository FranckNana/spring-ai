package com.ai_agent.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface AiAgent {
	@AliasFor(annotation = Component.class)
	public String value() default "";
}
