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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Haytham Mohamed
 **/
@Service
public class JdbcWorker {

	private final JdbcTemplate template;

	public JdbcWorker(JdbcTemplate jdbcTemplate) {
		this.template = jdbcTemplate;
	}

	public String query(String sql, Object[] params) throws DataAccessException {
		Map<String, Object> map = template.queryForMap(sql, params);
		return toXml(map);
	}

	public String update(String sql, Object[] params) throws DataAccessException {
		int count = template.update(sql, params);
		Map<String, Object> map = new HashMap<>();
		map.put("count", new Integer(count));
		return toXml(map);
	}

	//TODO: change this to whatever format you would like the output to be
	public String toXml(Map<String, Object> map) {
		String xml = "";

		XmlMapper xmlMapper = new XmlMapper();
		try {
			xml = xmlMapper.writer().withRootName("result").writeValueAsString(map);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return xml;
	}
}
