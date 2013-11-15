package com.oreilly.test1;

import java.util.List;
import java.util.ArrayList;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/")
public class MyRoute {
    @GET
    @Produces("application/json")
    @Path("/test1")
    public List<TestItem> getTestResult1() {
    	List<TestItem> items= new ArrayList<TestItem>();
    	TestItem testItem= new TestItem();
    	testItem.setVar1("hello1");
    	testItem.setVar2("goodbye1");
    	items.add(testItem);
        return items;
    }
    @GET
    @Produces("application/json")
    @Path("/test2")
    public List<TestItem> getTestResult2() {
    	List<TestItem> items= new ArrayList<TestItem>();
    	TestItem testItem= new TestItem();
    	testItem.setVar1("hello2");
    	testItem.setVar2("goodbye2");
    	items.add(testItem);
        return items;
    }

}
