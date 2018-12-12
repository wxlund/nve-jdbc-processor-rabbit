/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.agilehandy.processor;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.MessageBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Haytham Mohamed
 **/

@Configuration
@EnableBinding(Processor.class)
@EnableConfigurationProperties({JdbcProperties.class})
public class MessageProcessor {

	Logger logger = LoggerFactory.getLogger(MessageProcessor.class);

	private final JdbcWorker worker;

	private JdbcProperties properties;

	public MessageProcessor(JdbcWorker worker, JdbcProperties properties) {
		this.worker = worker;
		this.properties = properties;
	}

	@StreamListener(Processor.INPUT)
	@SendTo(Processor.OUTPUT)
	public Message<Map<String, Object>> process(Message<String> request) {
		String payload = request.getPayload();

		logger.info("Received: " + payload);

		// convert the payload (json) to string of params
		// observe the order they are passed in
		String[] params = this.jsonToParams(payload);

		Map<String, Object> result = new HashMap<>();

		// perform jdbc operation
		if(!properties.isUpdate()) {
			result = worker.query(properties.getQuery(), params);
		} else {
			Integer count = worker.update(properties.getQuery(), params);
			result = new HashMap<>();
			result.put("count", count);
		}

		logger.info("Sending: ");
		result.entrySet().stream().forEach(entry ->
				logger.info(entry.getKey() + ":" + (String)entry.getValue()));

		// pass the result thru
		return MessageBuilder.withPayload(result)
				.copyHeaders(request.getHeaders())
				.build();
	}

	// put the parameters in array in the right order
	private String[] jsonToParams(String json) {
		Map<String, String> map = this.convertToMap(json);
		String paramName = "param";
		Set<String> params = new HashSet<>();

		for(int i=0; i<map.size(); i++) {
			if (map.containsKey(paramName + i)) {
				params.add(map.get(paramName + i));
			}
		}

		return params.stream().toArray(String[]::new);
	}

	// converts the json formatted message to a map
	private Map<String, String> convertToMap(String json)  {
		ObjectMapper mapper = new ObjectMapper();
		// convert JSON string to Map

		Map<String, String> map = new HashMap<>();

		try {
			map = mapper.readValue(json, new TypeReference<Map<String, String>>(){});
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return map;
	}

}
