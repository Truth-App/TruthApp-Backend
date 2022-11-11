package com.tech.truthapp.elastic;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties("es")
@Getter
@Setter
public class ElasticSearchConfiguration {

	private String hostName;
    private int port;
    private String username;
    private String password;
    
	@Bean
    public RestClient getRestClient() {
		final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
               new UsernamePasswordCredentials(username, password));
        
        RestClient restClient = RestClient.builder(
                new HttpHost(hostName , port , "https" ))
        		.setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
                      .setDefaultCredentialsProvider(credentialsProvider))
        		.build();
        return restClient;
    }

    @Bean
    public  ElasticsearchTransport getElasticsearchTransport() {
        return new RestClientTransport(
                getRestClient(), new JacksonJsonpMapper());
    }


    @Bean
    public ElasticsearchClient getElasticsearchClient(){
        ElasticsearchClient client = new ElasticsearchClient(getElasticsearchTransport());
        return client;
    }
    
   
}
