package com.tutorial.jaxrs.calc;
import java.util.Date;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/date")
public class Main 
{
	static double msPerGregorianYear = 365.25 * 86400 * 1000;
    @SuppressWarnings("deprecation")
	@GET
    @Path("/years/{cyyyy}/{cmm}/{cdd}/{yyyy}/{mm}/{dd}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getYears(@PathParam("yyyy") int yyyy, @PathParam("mm") int mm, @PathParam("dd") int dd, @PathParam("cyyyy") int cyyyy, @PathParam("cmm") int cmm, @PathParam("cdd") int cdd) 
    {
    	Date dob = new Date(yyyy,mm,dd);
    	Date date = new Date(cyyyy,cmm,cdd);
    	double years = (date.getTime() - dob.getTime()) / msPerGregorianYear;
    	int yy = (int) years;
    	return Integer.toString(yy);	
    }
    @GET
    @Path("/months/{cyyyy}/{cmm}/{cdd}/{yyyy}/{mm}/{dd}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getMonths(@PathParam("yyyy") int yyyy, @PathParam("mm") int mm, @PathParam("dd") int dd, @PathParam("cyyyy") int cyyyy, @PathParam("cmm") int cmm, @PathParam("cdd") int cdd) 
    {
    	@SuppressWarnings("deprecation") 
    	Date dob = new Date(yyyy,mm,dd);
    	@SuppressWarnings("deprecation")
		Date date = new Date(cyyyy,cmm,cdd);
    	double years = (date.getTime() - dob.getTime()) / msPerGregorianYear;
    	int yy = (int) years;
    	int months = (int) ((years - yy) * 12);
    	return Integer.toString(months);	
    }
    
    @POST
    @Path("/post")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response postRequest(String string)
    {
    	String result = "Recieved: " + string;
    	return Response.status(200).entity(result).build();
    }
    
    @PUT
    @Path("/put")
    @Produces(MediaType.TEXT_PLAIN)
    public Response putRequest(String string)
    {
    	String result = "Sent: " + string;
    	return Response.status(200).entity(result).build();
    }
    
    @DELETE
    @Path("/delete")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public Response deleteRequest(String stringToDelete)
    {
    	return Response.status(200).entity(stringToDelete + " is deleted").build();
    }
    
}