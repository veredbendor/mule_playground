package com.oreilly.test1.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.mule.api.MuleMessage;
import org.mule.api.client.MuleClient;
import org.mule.tck.functional.FunctionalTestComponent;
import org.mule.tck.junit4.FunctionalTestCase;

public class ExternalRestTest extends FunctionalTestCase {

	@Test
	public void testBasicLookup() throws Exception {
		String city = "Sebastopol";
		String state = "CA";
		String tempFarenheitString="58.32";
		String tempKelvinString="287.77";
		String desc = "test desc";
		
		FunctionalTestComponent mockedWeatherService=getFunctionalTestComponent("mockWeatherService");
		
		mockedWeatherService.setReturnData(
				String.format("{\"coord\":{\"lon\":-122.83,\"lat\":38.4},\"sys\":{\"message\":0.0217,\"country\":\"United States of America\",\"sunrise\":1384440772,\"sunset\":1384477136}," +
				"\"weather\":[{\"id\":802,\"main\":\"Clouds\",\"description\":\"%s\",\"icon\":\"03d\"}],\"base\":\"gdps stations\",\"main\":{\"temp\":%s,\"humidity\":81," +
				"\"pressure\":1013,\"temp_min\":287.15,\"temp_max\":288.71},\"wind\":{\"speed\":1.65,\"deg\":286},\"clouds\":{\"all\":44},\"dt\":1384459280,\"id\":5394165," +
				"\"name\":\"Sebastopol\",\"cod\":200}",desc,tempKelvinString)
				);
		MuleClient client = muleContext.getClient();

		MuleMessage result = client.request(String.format(
				"http://127.0.0.1:8082?city=%s&state=%s", city, state), 1000);
		assertEquals("/data/2.5/weather?q=Sebastopol,CA,USA",mockedWeatherService.getLastReceivedMessage().toString());
		assertThat(result, is(notNullValue()));
		String response = result.getPayloadAsString();

		assertEquals(
				String.format(
						"<html><body><h1>Weather in %s,%s</h1>%s &deg; %s</body><br></html>",
						city, state, tempFarenheitString, desc), response);

	}

	@Override
	protected String getConfigResources() {
		return "externalrest.xml,externalrest-testflow.xml";
	}
}