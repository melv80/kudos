package com.kudos.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("index");
		registry.addViewController("/login").setViewName("login");
		registry.addViewController("/user").setViewName("user");
		registry.addViewController("/user/update").setViewName("user");

		registry.addViewController("/user/channel").setViewName("channel");
		registry.addViewController("/user/create").setViewName("user");
		registry.addViewController("/admin").setViewName("admin");
		registry.addViewController("/admin/delete").setViewName("admin");
		registry.addViewController("/admin/import").setViewName("admin");
		registry.addViewController("/create").setViewName("create");
	}


}