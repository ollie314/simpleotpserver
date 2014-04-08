package org.sisdirect.otpserver.generator;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class OtpTest {
	
	@Configuration
	static class Config {
		
		@Bean
		public OtpGenerator otp() {
			return new DefaultOtpGenerator();
		}
	}
	
	/**
	 * Default generator
	 */
	@Autowired
	private OtpGenerator generator;
	
	/**
	 * 
	 */
	private final Logger logger = Logger.getLogger(getClass()); 
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void autowiringOk() {
		logger.info("autowiringOk() starting");
		logger.trace("Checking generator correct instanciation");
		assertNotNull("The generator should be correctly injected", generator);
		logger.info("autowiringOk() complete");
	}

	@Test
	@Ignore
	public void testCalcChecksum() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testHmac_sha1() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testGenerateOTP() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testGetUnixTimestamp() {
		fail("Not yet implemented");
	}

}
