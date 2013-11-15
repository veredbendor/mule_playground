package com.oreilly.test1.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.mortbay.util.ajax.JSON;
import org.mule.api.MuleMessage;
import org.mule.api.client.MuleClient;
import org.mule.api.transport.ReceiveException;
import org.mule.tck.junit4.FunctionalTestCase;
import static org.hamcrest.CoreMatchers.*;

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