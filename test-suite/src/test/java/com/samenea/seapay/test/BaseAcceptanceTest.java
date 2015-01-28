package com.samenea.seapay.test;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

/**
 * @author: Soroosh Sarabadani
 * Date: 12/31/12
 * Time: 9:49 AM
 */

@TestExecutionListeners({AcceptanceTestExecutionListener.class})
@ContextConfiguration(locations = {"classpath:contexts.xml"})
public abstract class BaseAcceptanceTest extends AbstractTransactionalJUnit4SpringContextTests {
}
