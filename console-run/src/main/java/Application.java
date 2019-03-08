
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.pepper.core.BaseDaoFactoryBean;
import com.pepper.core.dubbo.DubboDynamicVersion;

@DubboComponentScan(basePackages = { "com.pepper.controller.**", "com.pepper.service.**", "com.pepper.util.**",
		"com.pepper.core.**", "com.pepper.init.data.**" })
@SpringBootApplication(scanBasePackages = { "com.pepper.controller.**", "com.pepper.service.**", "com.pepper.util.**",
		"com.pepper.core.**", "com.pepper.model.**", "com.pepper.init.data.**","com.pepper.register.**"})
@EnableJpaRepositories(basePackages = "com.pepper.dao.**", repositoryFactoryBeanClass = BaseDaoFactoryBean.class)
@EntityScan("com.pepper.model.**")
@PropertySource(value = { "classpath:console-run.properties" }, ignoreResourceNotFound = true, encoding = "UTF-8")
@Import(value={DubboDynamicVersion.class})
public class Application extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}

	public static void main(String args[]) throws Exception {
		/*
		 * new SpringApplicationBuilder(Application.class)
		 * .web(WebApplicationType.NONE) .run(args);
		 */
		SpringApplication.run(Application.class, args);
	}
}