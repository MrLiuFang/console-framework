
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import com.pepper.core.BaseDaoFactoryBean;

//@DubboComponentScan(basePackages = { "com.pepper.controller.**", "com.pepper.service.**", "com.pepper.util.**","com.pepper.core.**" })
@SpringBootApplication(scanBasePackages = { "com.pepper.controller.**", "com.pepper.service.**", "com.pepper.util.**","com.pepper.core.**", "com.pepper.model.**" })
//@EnableJpaRepositories(basePackages = "com.pepper.dao.**", repositoryFactoryBeanClass = BaseDaoFactoryBean.class)
//@EntityScan("com.pepper.model.**")
//@EnableAutoConfiguration()
@PropertySource(value = { "classpath:console-run.properties" }, ignoreResourceNotFound = true, encoding = "UTF-8")
public class Application extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}

//	public static void main(String args[]) {
//		SpringApplication.run(Application.class, args);
//	}
}