package com.oreilly.test1.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.mule.api.MuleMessage;
import org.mule.api.client.MuleClient;
import org.mule.tck.junit4.FunctionalTestCase;

public class ScriptingTest extends FunctionalTestCase {
	@Test
	public void testNoVarProvided() throws Exception {
		MuleClient client = muleContext.getClient();
		MuleMessage result = client.request("http://127.0.0.1:8083", 1000);
		assertThat(result, is(notNullValue()));
		int parsedResponse = Integer.parseInt(result.getPayloadAsString());
		assertEquals(-1 * 3 * 5 * 7 * 11, parsedResponse);
	}

	@Test
	public void testIntsProvided() throws Exception {
		for (int i = 0; i < 10; i++) {
			MuleClient client = muleContext.getClient();
			MuleMessage result = client.request(
					String.format("http://127.0.0.1:8083?a=%d", i), 1000);
			assertThat(result, is(notNullValue()));
			int parsedResponse = Integer.parseInt(result.getPayloadAsString());
			assertEquals(i * 3 * 5 * 7 * 11, parsedResponse);
		}
	}

	@Test
	public void testNoNumericProvided() throws Exception {
		MuleClient client = muleContext.getClient();
		MuleMessage result = client.request(
				String.format("http://127.0.0.1:8083?a=blah"), 1000);
		assertThat(result, is(notNullValue()));
		int parsedResponse = Integer.parseInt(result.getPayloadAsString());
		assertEquals(0, parsedResponse);
	}

	@Override
	protected String getConfigResources() {
		return "scripting.xml";
	}
}