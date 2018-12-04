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

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * @author Haytham Mohamed
 **/

@ConfigurationProperties("jdbc")
@Validated
@Data
public class JdbcProperties {

	/**
	 * The query statement.
	 */
	private String query;

	/**
	 * Whether its an update SQL messages.
	 */
	private boolean update = false;

	@NotNull
	public String getQuery() {
		return query;
	}

}
