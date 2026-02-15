package net.prastars.messagesystem;

import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification;

@SpringBootTest(classes = MessageSystemApplication.class)
class MessageSystemApplicationSpec extends Specification{

	void contextLoads() {
		expect:
		true
	}

}
