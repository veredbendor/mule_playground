package com.oreilly.test1.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.mule.api.MuleMessage;
import org.mule.api.client.MuleClient;
import org.mule.tck.junit4.FunctionalTestCase;

import com.oreilly.test1.model.Product;

public class ProductTest  extends FunctionalTestCase {

	@Test
	public void testLookup1() throws Exception {
		MuleClient client = muleContext.getClient();
		MuleMessage result = client
				.request("http://127.0.0.1:8087/product/a1", 1000);
		assertThat(result, is(notNullValue()));
		ObjectMapper mapper = new ObjectMapper();
		Product product=mapper.readValue(result.getPayloadAsString(),Product.class);
		assertEquals("a1",product.getId());
		assertEquals("Title For Book With ID a1",product.getTitle());
		assertEquals("Publisher1",product.getPublisher());
		assertEquals("format1",product.getFormat());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date myDate= formatter.parse("2011-01-01 01:20:13");
		assertEquals(myDate,product.getCreated());
		assertEquals(153,product.getNumPages());
	}
	
	@Test
	public void testLookup2() throws Exception {
		MuleClient client = muleContext.getClient();
		MuleMessage result = client
				.request("http://127.0.0.1:8087/product/b1", 1000);
		assertThat(result, is(notNullValue()));
		ObjectMapper mapper = new ObjectMapper();
		Product product=mapper.readValue(result.getPayloadAsString(),Product.class);
		assertEquals("b1",product.getId());
		assertEquals("Title For Book With ID b1",product.getTitle());
		assertEquals("Publisher2",product.getPublisher());
		assertEquals("format2",product.getFormat());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date myDate= formatter.parse("2010-02-03 02:25:17");
		assertEquals(myDate,product.getCreated());
		assertEquals(234,product.getNumPages());
	}
	
	@Test
	public void testLookupNotFound() throws Exception {
		MuleClient client = muleContext.getClient();
		MuleMessage result = client
				.request("http://127.0.0.1:8087/product/a1gf", 1000);
		assertThat(result, is(notNullValue()));
		ObjectMapper mapper = new ObjectMapper();
		Product product=mapper.readValue(result.getPayloadAsString(),Product.class);
		assertEquals(null,product);
	}


	@Override
	protected String getConfigResources() {
		return "product.xml,productlist-testflow.xml";
	}

}
