package com.oreilly.test1.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;
import org.mortbay.util.ajax.JSON;
import org.mule.api.MuleMessage;
import org.mule.api.client.MuleClient;
import org.mule.api.transport.ReceiveException;
import org.mule.tck.junit4.FunctionalTestCase;

public class RestEndpointTest extends FunctionalTestCase {
	@Test
	public void testGetTest1() throws Exception {
		MuleClient client = muleContext.getClient();
		MuleMessage result = client
				.request("http://127.0.0.1:8081/test1", 1000);
		assertThat(result, is(notNullValue()));
		Object parsedJson = JSON.parse(result.getPayloadAsString());
		assertTrue(parsedJson instanceof Object[]);
		Object[] array = (Object[]) parsedJson;
		assertEquals(1, array.length);
		assertTrue(array[0] instanceof Map);
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>) array[0];
		assertEquals("hello1", map.get("var1"));
		assertEquals("goodbye1", map.get("var2"));

	}

	@Test
	public void testGetTest2() throws Exception {
		MuleClient client = muleContext.getClient();
		MuleMessage result = client
				.request("http://127.0.0.1:8081/test2", 1000);
		assertThat(result, is(notNullValue()));
		Object parsedJson = JSON.parse(result.getPayloadAsString());
		assertTrue(parsedJson instanceof Object[]);
		Object[] array = (Object[]) parsedJson;
		assertEquals(1, array.length);
		assertTrue(array[0] instanceof Map);
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>) array[0];
		assertEquals("hello2", map.get("var1"));
		assertEquals("goodbye2", map.get("var2"));
	}

	@Test
	public void testGetOther() throws Exception {
		MuleClient client = muleContext.getClient();
		boolean hadError = false;
		try {
			client.request("http://127.0.0.1:8081/blah", 1000);
		} catch (ReceiveException e) {
			hadError = true;
		}
		assertTrue(hadError);
	}

	@Override
	protected String getConfigResources() {
		return "restendpoint.xml";
	}
}