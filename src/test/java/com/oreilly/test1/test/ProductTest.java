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
	public void testUpdateAndLookup() throws Exception {
		MuleClient client = muleContext.getClient();
		Date now= new Date();
		Product product = new Product("b1","Title 2a", "Publisher2a", "format2a", 457,now);
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String productJson = ow.writeValueAsString(product);
        Map<String, Object> props = new HashMap<String,Object>();
        props.put("http.method", "PUT");
		MuleMessage result = client
				.send("http://127.0.0.1:8087/product/b1", productJson,props);
		assertThat(result, is(notNullValue()));
		ObjectMapper mapper1 = new ObjectMapper();
		Product product1=mapper1.readValue(result.getPayloadAsString(),Product.class);
		assertEquals("b1",product1.getId());
		assertEquals("Title 2a",product1.getTitle());
		assertEquals("Publisher2a",product1.getPublisher());
		assertEquals("format2a",product1.getFormat());
		assertEquals(now,product1.getCreated());
		assertEquals(457,product1.getNumPages());
		MuleMessage result2 = client
				.request("http://127.0.0.1:8087/product/b1", 1000);
		assertThat(result2, is(notNullValue()));
		ObjectMapper mapper2 = new ObjectMapper();
		Product product2=mapper2.readValue(result2.getPayloadAsString(),Product.class);
		assertEquals("b1",product2.getId());
		assertEquals("Title 2a",product2.getTitle());
		assertEquals("Publisher2a",product2.getPublisher());
		assertEquals("format2a",product2.getFormat());
		assertEquals(now,product2.getCreated());
		assertEquals(457,product2.getNumPages());
	}
	
	@Test
	public void testDeleteAndLookupNotFound() throws Exception {
		MuleClient client1 = muleContext.getClient();
        Map<String, Object> props = new HashMap<String,Object>();
        props.put("http.method", "DELETE");
		MuleMessage result1 = client1
				.send("http://127.0.0.1:8087/product/b1", null,props);
		assertThat(result1, is(notNullValue()));
		ObjectMapper mapper = new ObjectMapper();
		Product product1=mapper.readValue(result1.getPayloadAsString(),Product.class);
		assertEquals(null,product1);		
		MuleClient client = muleContext.getClient();
		MuleMessage result2 = client
				.request("http://127.0.0.1:8087/product/b1", 1000);
		assertThat(result2, is(notNullValue()));
		Product product2=mapper.readValue(result2.getPayloadAsString(),Product.class);
		assertEquals(null,product2);
	}


	@Override
	protected String getConfigResources() {
		
		return "product.xml,productlist-testflow.xml";
	}

}
