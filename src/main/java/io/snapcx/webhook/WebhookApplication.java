package io.snapcx.webhook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class WebhookApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebhookApplication.class, args);
	}
	
  @Bean(name="defaultRestTemplate")
  public RestTemplate getRestTemplate() {
  	RestTemplate restTemplate = new RestTemplate();
  	return restTemplate;
  }
}
