package jp.co.axa.apidemo.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
@Slf4j
@Getter
public class ApplicationStateService {

	@Value("${service.ready.waittime:5000}")
	private Integer readyWaitTime;

	@Value("${service.terminate.waittime:5000}")
	private Integer terminateWaitTime;

	private boolean ready = false;

	public void prepareReadyState() throws InterruptedException {
		// Simulate startup tasks...
		if (!ready) {
			log.info("Starting up...");
			Thread.sleep(readyWaitTime);
			ready = true;
		}
		log.info("READY");
	}

}


