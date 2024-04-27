package com.yeeiee;

import com.yeeiee.entity.DataField;
import com.yeeiee.service.impl.DataFieldServiceImpl;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MetaToolAppApplicationTests {

	@Autowired
	DataFieldServiceImpl dataFieldService;

	@Test
	void contextLoads() {
		val dataField = new DataField();
		dataField.setName("test5");
		dataField.setDescription("测试事务5");
		dataFieldService.saveOne(dataField);
	}
}
