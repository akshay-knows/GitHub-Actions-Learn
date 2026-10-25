package com.swiftyquote.workflow.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration public class KafkaTopicConfig {
 @Bean public NewTopic decisionsTopic(){return new NewTopic("quotation-decisions",3,(short)1);}
}
