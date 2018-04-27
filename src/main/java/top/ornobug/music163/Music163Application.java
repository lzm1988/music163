package top.ornobug.music163;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
// mapper 接口类扫描包配置
@MapperScan("top.ornobug.music163.dao")
@ComponentScan(basePackages = {
		"top.ornobug.music163.util"
		, "top.ornobug.music163.scheduled"
		, "top.ornobug.music163.service"
		, "top.ornobug.music163.service.impl"
		, "top.ornobug.music163.spider"
})
@EnableScheduling
public class Music163Application {

	public static void main(String[] args) {
		SpringApplication.run(Music163Application.class, args);
	}
}
