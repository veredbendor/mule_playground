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

public class ProductListTest  extends FunctionalTestCase {

	@Test
	public void testFindSingleItemById() throws Exception {
		MuleClient client = muleContext.getClient();
		MuleMessage result = client
				.request("http://127.0.0.1:8087/productList?ids=a1", 1000);
		assertThat(result, is(notNullValue()));
		ObjectMapper mapper = new ObjectMapper();
		Product[] products=mapper.readValue(result.getPayloadAsString(),Product[].class);
		assertEquals(1,products.length);
		assertEquals("a1",products[0].getId());
		assertEquals("Title For Book With ID a1",products[0].getTitle());
		assertEquals("Publisher1",products[0].getPublisher());
		assertEquals("format1",products[0].getFormat());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date myDate= formatter.parse("2011-01-01 01:20:13");
		assertEquals(myDate,products[0].getCreated());
		assertEquals(153,products[0].getNumPages());
	}
	@Test
	public void testFindAnotherItemById() throws Exception {
		MuleClient client = muleContext.getClient();
		MuleMessage result = client
				.request("http://127.0.0.1:8087/productList?ids=b1", 1000);
		assertThat(result, is(notNullValue()));
		ObjectMapper mapper = new ObjectMapper();
		Product[] products=mapper.readValue(result.getPayloadAsString(),Product[].class);
		assertEquals(1,products.length);
		assertEquals("b1",products[0].getId());
	}
	@Test
	public void testFindTwoItemsByIds() throws Exception {
		MuleClient client = muleContext.getClient();
		MuleMessage result = client
				.request("http://127.0.0.1:8087/productList?ids=a1%2Cb1", 1000);
		assertThat(result, is(notNullValue()));
		ObjectMapper mapper = new ObjectMapper();
		Product[] products=mapper.readValue(result.getPayloadAsString(),Product[].class);
		assertEquals(2,products.length);
		assertEquals("a1",products[0].getId());
		assertEquals("b1",products[1].getId());
	}
	@Test
	public void testFindTwoItemsByIdsSortedByIds() throws Exception {
		MuleClient client = muleContext.getClient();
		MuleMessage result = client
				.request("http://127.0.0.1:8087/productList?ids=b1%2Ca1", 1000);
		assertThat(result, is(notNullValue()));
		ObjectMapper mapper = new ObjectMapper();
		Product[] products=mapper.readValue(result.getPayloadAsString(),Product[].class);
		assertEquals(2,products.length);
		assertEquals("a1",products[0].getId());
		assertEquals("b1",products[1].getId());
	}
	

	@Override
	protected String getConfigResources() {
		return "product.xml,productlist-testflow.xml";
	}

}
