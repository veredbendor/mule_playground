package com.oreilly.test1.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
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
				.request("http://127.0.0.1:8087/products?ids=a1", 1000);
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
				.request("http://127.0.0.1:8087/products?ids=b1", 1000);
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
				.request("http://127.0.0.1:8087/products?ids=a1%2Cb1", 1000);
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
				.request("http://127.0.0.1:8087/products?ids=b1%2Ca1", 1000);
		assertThat(result, is(notNullValue()));
		ObjectMapper mapper = new ObjectMapper();
		Product[] products=mapper.readValue(result.getPayloadAsString(),Product[].class);
		assertEquals(2,products.length);
		assertEquals("a1",products[0].getId());
		assertEquals("b1",products[1].getId());
	}
	@Test
	public void testNothingFound() throws Exception {
		MuleClient client = muleContext.getClient();
		MuleMessage result = client
				.request("http://127.0.0.1:8087/products?ids=fasdf%2Csdfgd", 1000);
		assertThat(result, is(notNullValue()));
		ObjectMapper mapper = new ObjectMapper();
		Product[] products=mapper.readValue(result.getPayloadAsString(),Product[].class);
		assertEquals(0,products.length);
	}
	@Test
	public void testPartialFind() throws Exception {
		MuleClient client = muleContext.getClient();
		MuleMessage result = client
				.request("http://127.0.0.1:8087/products?ids=a1%2Csdfgd%2cb1%2Cgsdfgsd", 1000);
		assertThat(result, is(notNullValue()));
		ObjectMapper mapper = new ObjectMapper();
		Product[] products=mapper.readValue(result.getPayloadAsString(),Product[].class);
		assertEquals(2,products.length);
		assertEquals("a1",products[0].getId());
		assertEquals("b1",products[1].getId());
	}
	
	@Test
	public void testCreate() throws Exception {
		Date now= new Date();
		Product product = new Product("c1","Title 3", "Publisher3", "format3", 456,now);
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String productJson = ow.writeValueAsString(product);
        Map<String, Object> props = new HashMap<String,Object>();
        props.put("http.method", "POST");
		MuleClient client = muleContext.getClient();
		MuleMessage result = client
				.send("http://127.0.0.1:8087/products", productJson,props);
		assertThat(result, is(notNullValue()));
		ObjectMapper mapper = new ObjectMapper();
		String testString=result.getPayloadAsString();
		Product returnedProduct=mapper.readValue(testString,Product.class);
		assertEquals("c1",returnedProduct.getId());
		assertEquals("Title 3",returnedProduct.getTitle());
		assertEquals("Publisher3",returnedProduct.getPublisher());
		assertEquals("format3",returnedProduct.getFormat());
		assertEquals(now,returnedProduct.getCreated());
		assertEquals(456,returnedProduct.getNumPages());
		MuleMessage result2 = client
				.request("http://127.0.0.1:8087/products?ids=c1", 1000);
		assertThat(result2, is(notNullValue()));
		ObjectMapper mapper2 = new ObjectMapper();
		Product[] products=mapper2.readValue(result2.getPayloadAsString(),Product[].class);
		assertEquals(1,products.length);
	}
	

	@Override
	protected String getConfigResources() {
		return "product.xml,productlist-testflow.xml";
	}

}
