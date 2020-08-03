package com.tutorial.jaxrs.calc;
import javax.ws.rs.core.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.json.*;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import org.json.*;

public class MyClient
{
	
	static final String FILEPATH = "D:\\eclipse workspace\\Files\\";
	static final String MY_REST_URI = "http://localhost:9999/MyServer";
	static final String YEARS = "date/years";
	static final String MONTHS = "date/months";
	static final String NUMBERS_URI = "http://numbersapi.com/";
	static final String PIRATE_URI = "https://api.funtranslations.com/translate/pirate.json";
	
	public static void main(String[] args) throws JSONException , IOException
	{
		setupGUI(); 
	}
	
	private static void setupGUI()
	{
		//GUI
		JFrame f=new JFrame("Client"); 
		JFrame frame = new JFrame("Response");
		//submit button
		JButton b=new JButton("Submit");    
		b.setBounds(100,200,100,40);    
		JLabel label = new JLabel();		
		label.setText("Enter Current date (yyyy mm dd):");
		label.setBounds(10, 10, 400, 100);
		JLabel label2 = new JLabel();
		label2.setText("Enter your date of birth (yyyy mm dd):");
		label2.setBounds(10,100,400,100);
		JTextField textfield = new JTextField();
		textfield.setBounds(300, 50, 130, 30);
		JTextField textfield2 = new JTextField();
		textfield2.setBounds(300,140,130,30);
		//add to frame
		f.add(textfield);
		f.add(textfield2);
		f.add(label);
		f.add(label2);
		f.add(b);    
		f.setSize(500,300);    
		f.setLayout(null);    
		f.setVisible(true);    
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setSize(1300, 500);
		frame.setLayout(null);
		frame.setVisible(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//action listener
		b.addActionListener(new ActionListener() 
		{

			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				String currentDate = textfield.getText();
				String DoB = textfield2.getText();
				f.setVisible(false);
				frame.setVisible(true);
				run(currentDate, DoB, frame);		
			}          
		});
	}
    

	
	private static void run(String currentDate, String DoB, JFrame frame)
	{
		String[] CurrentDateArray = currentDate.split("\\s+");
		String[] DOBArray = DoB.split("\\s+");

		int cYear = Integer.parseInt(CurrentDateArray[0]);
		int cMonth = Integer.parseInt(CurrentDateArray[1]);
		int cDay = Integer.parseInt(CurrentDateArray[2]);
		int y = Integer.parseInt(DOBArray[0]);
		int m = Integer.parseInt(DOBArray[1]);
		int d = Integer.parseInt(DOBArray[2]);

		//WEB SERVICE 1 - returns number of years and months alive
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		
		WebResource service = client.resource(MY_REST_URI);
		WebResource Years = service.path(YEARS).path(cYear + "/" + cMonth + "/" + cDay + "/" + y + "/" + m + "/" + d);
		WebResource Months = service.path(MONTHS).path(cYear + "/" + cMonth + "/" + cDay + "/" + y + "/" + m + "/" + d);
		
		String yearsResponse = getOutputAsText(Years);
		String monthsResponse = getOutputAsText(Months);
		
		System.out.println("Response: " + yearsResponse + " years");
		System.out.println("Response: " + monthsResponse + " months");	

		
		//WEB SERVICE 2 - returns information about output numbers from web service 1
		Client client2 = Client.create(config);
		WebResource NumberTrivia = client2.resource(NUMBERS_URI);
		WebResource YearTrivia = NumberTrivia.path(yearsResponse);
		WebResource MonthTrivia = NumberTrivia.path(monthsResponse);
		
		String yearTriviaResponse = getOutputAsText(YearTrivia);
		String monthTriviaResponse = getOutputAsText(MonthTrivia);
		
		System.out.println("Response :" + yearTriviaResponse);
		System.out.println("Response :" + monthTriviaResponse);




		//WEB SERVICE 3 - returns "pirate speak" translation of output from web service 2
		Client client3 = Client.create(config);
		WebResource pirate = client3.resource(PIRATE_URI);
		WebResource text1 = pirate.queryParam("text",yearTriviaResponse);
		WebResource text2 = pirate.queryParam("text",monthTriviaResponse);
		
		ClientResponse response = text1.accept("application/json").get(ClientResponse.class);
		ClientResponse response2 = text2.accept("application/json").get(ClientResponse.class);
		String output = response.getEntity(String.class);
		String output2 = response2.getEntity(String.class);
		//call the getObj function to extract data from the JSON output
		String pirateString1 = getObj(output);
		String pirateString2 = getObj(output2);
		
		//update the GUI with the responses from web services
		updateGUI(yearsResponse, monthsResponse, yearTriviaResponse, monthTriviaResponse, pirateString1, pirateString2, frame);
		
		//Write the output to a .txt file
		fileWriter(DoB, currentDate, yearsResponse, monthsResponse, yearTriviaResponse, monthTriviaResponse, pirateString1, pirateString2);
		
		

	}
    
	
	private static void updateGUI(String yearsResponse, String monthsResponse, String yearTriviaResponse, String monthTriviaResponse, String pirateString1, String pirateString2, JFrame frame)
	{
		JLabel label = new JLabel();		
		label.setText("You have been alive for: " + yearsResponse+ " years and "+ monthsResponse + " months");
		label.setBounds(10, 10, 700, 100);
		
		JLabel label2 = new JLabel();		
		label2.setText(yearTriviaResponse);
		label2.setBounds(10, 80, 1200, 100);
		
		JLabel label3 = new JLabel();		
		label3.setText(monthTriviaResponse);
		label3.setBounds(10, 150, 1200, 100);
		
		JLabel label4 = new JLabel();
		label4.setText(pirateString1);
		label4.setBounds(10,220,1200,100);
		
		JLabel label5 = new JLabel();
		label5.setText(pirateString2);
		label5.setBounds(10,290,1200,100);
		
		frame.add(label); 
		frame.add(label2);
		frame.add(label3);
		frame.add(label4);
		frame.add(label5);
	}
	
	
	private static String getObj(String JsonString)
	{
		String jsonAsString = null;
		try 
		{
			JSONObject obj = new JSONObject(JsonString);
			jsonAsString = obj.getJSONObject("contents").getString("translated");
		} 
		catch (JSONException e2) 
		{
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		return jsonAsString;
	}
	
	
	
	
	
	
	
	private static void fileWriter(String DoB, String currentDate, String yearsResponse, String monthsResponse, String yearTriviaResponse, String monthTriviaResponse, String pirateString1, String pirateString2)
	{
		BufferedWriter writer;
		try 
		{
			writer = new BufferedWriter(new FileWriter(FILEPATH+DoB+"-"+currentDate+".txt"));
			writer.write("You have been alive for: " + yearsResponse+ " years and "+ monthsResponse + " months\n");
			writer.write(yearTriviaResponse+ "\n");
			writer.write(monthTriviaResponse+ "\n");
			writer.write(pirateString1 + "\n");
			writer.write(pirateString2 + "\n");
			writer.close();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
    private static String getResponse(WebResource service) 
    {
        return service.accept(MediaType.TEXT_XML).get(ClientResponse.class).toString();
    }
 
    private static String getOutputAsXML(WebResource service) 
    {
        return service.accept(MediaType.TEXT_XML).get(String.class);
    }
 
    private static String getOutputAsText(WebResource service) 
    {
        return service.accept(MediaType.TEXT_PLAIN).get(String.class);
    }
    private static String getOutputAsJSON(WebResource service)
    {
    	return service.accept(MediaType.APPLICATION_JSON).get(String.class);
    }
   
}
