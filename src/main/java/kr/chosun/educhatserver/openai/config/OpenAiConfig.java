package kr.chosun.educhatserver.openai.config;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OpenAiConfig {
    @Value("${openai.api.key}")
    private String openAiKey;

    @Bean
    public RestTemplate template() {
        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
//        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
//            public boolean hasError(ClientHttpResponse response) throws IOException {
//                HttpStatus statusCode = (HttpStatus) response.getStatusCode();
//                return statusCode.series() == HttpStatus.Series.SERVER_ERROR;
//            }
//        });

        restTemplate.getInterceptors().add(((request, body, execution) -> {
            request.getHeaders().setBearerAuth(openAiKey);
            return execution.execute(request, body);
        }));

        return restTemplate;
    }
}
