package com.easycodebox.auth.backend.config;

import com.easycodebox.common.web.springmvc.DefaultRequestMappingHandlerAdapter;
import com.easycodebox.common.web.springmvc.DefaultRequestMappingHandlerMapping;
import freemarker.ext.jsp.TaglibFactory.ClasspathMetaInfTldSource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.web.WebMvcRegistrations;
import org.springframework.boot.autoconfigure.web.WebMvcRegistrationsAdapter;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import javax.annotation.Resource;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Spring MVC 配置
 *
 * @author WangXiaoJin
 */
@Configuration
@ComponentScan(basePackages = "com.easycodebox.idgenerator.controller")
@SuppressWarnings("Duplicates")
public class SpringMvcConfig {
	
	@Resource
	private Map properties;
	
	/**
	 * 生成JS的配置文件
	 */
	/*@Bean(initMethod = "process")
	@Profile("!" + Constants.INTEGRATION_TEST_KEY)
	public FreemarkerGenerate freemarkerGenerate(freemarker.template.Configuration configuration) {
		FreemarkerGenerate generate = new FreemarkerGenerate();
		generate.setFtlPath("/config-js.ftl");
		generate.setOutputPath("/js/config.js");
		generate.setConfiguration(configuration);
		generate.setDataModel(properties);
		return generate;
	}*/
	
	@Bean
	public WebMvcRegistrations webMvcRegistrations() {
		return new WebMvcRegistrationsAdapter() {
			@Override
			public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
				DefaultRequestMappingHandlerMapping mapping = new DefaultRequestMappingHandlerMapping();
				mapping.setControllerPostfix("Controller");
				mapping.setExcludePatterns(new String[]{
						"/**/*.js",
						"/**/*.css",
						"/imgs/**",
						"/WEB-INF/common/**",
						"/errors/**"
				});
				return mapping;
			}
			
			@Override
			public RequestMappingHandlerAdapter getRequestMappingHandlerAdapter() {
				DefaultRequestMappingHandlerAdapter adapter = new DefaultRequestMappingHandlerAdapter();
				adapter.setAutoView(true);
				return adapter;
			}
		};
	}
	
	/**
	 * 修改MetaInfTldSources加载Taglib规则 - 默认加载/WEB-INF/lib下面的jar <br/>
	 * 集成Spring Boot后没有WEB-INF目录，所以找不到原有的tld文件，修改成{@link ClasspathMetaInfTldSource}
	 * 来加载所有的tld文件
	 * @return
	 */
	@Bean
	public static BeanPostProcessor freeMarkerConfigPostProcessor() {
		return new BeanPostProcessor() {
			@Override
			public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
				return bean;
			}
			
			@Override
			public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
				if (bean instanceof FreeMarkerConfig) {
					FreeMarkerConfig config = (FreeMarkerConfig) bean;
					//修改MetaInfTldSources加载Taglib规则 - 默认加载/WEB-INF/lib下面的jar
					List<ClasspathMetaInfTldSource> tldSources = Collections.singletonList(new ClasspathMetaInfTldSource(Pattern.compile(".*")));
					config.getTaglibFactory().setMetaInfTldSources(tldSources);
				}
				return bean;
			}
		};
	}
	
}
