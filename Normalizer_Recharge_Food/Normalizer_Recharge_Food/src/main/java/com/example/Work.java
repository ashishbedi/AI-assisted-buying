package com.example;

import java.io.IOException; 
import java.util.*;
import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.grizzly.nio.transport.TCPNIOTransport;
import org.glassfish.grizzly.strategies.WorkerThreadIOStrategy;
import org.glassfish.grizzly.threadpool.ThreadPoolConfig;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

import com.niki.normalizer.*;

@Path("nlp")
public class Work
{	
	public static HashMapping MappingObj = null;
	
	@GET
	@Path("normalizer")
	@Produces(MediaType.TEXT_PLAIN)
	public String work(@QueryParam("sentence") String str) throws IOException
	{
		if(MappingObj == null)
		{
			HashMapping MappingObj1 = new HashMapping();
			MappingObj = MappingObj1;
		}
		Cleaner cleanobj = new Cleaner();
		String tempstr = str;
		tempstr = cleanobj.cleaning1(tempstr);
		String punctype[] = {" "};
		tempstr = MappingObj.socialhash(tempstr,punctype);	// using slangHandler + spellCorrector + n-gram + levenshtein
		System.out.println("Corrected: " + tempstr);
		return tempstr;
	}
}
