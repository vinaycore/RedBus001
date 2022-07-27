package stepDefinitions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Functions.redBusUIFunctions;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;

public class RedBusCounterStepDef {

	static WebDriver driver ;
	static WebDriverWait wait ;
	String redBusHomePageUrl="https://www.redbus.in/";
	String expTitle="Book Bus Travels, AC Volvo Bus, rPool & Bus Hire - redBus India";
	String actTtile="";
	List<String> busList = new ArrayList<String>();
	
	
	
	@Given("User opens the RebBus homePage Url")
	public void precondition_is_given() throws InterruptedException {
		
		ChromeOptions chromeOptions = new ChromeOptions();
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver(chromeOptions);
		wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		driver.get(redBusHomePageUrl);
		driver.manage().window().maximize();
		actTtile=driver.getTitle();
		assertEquals(actTtile, expTitle);
	}

	@When("User enters SourceLocation,DestinationLocation,DateOfJourney and Hits Search button")
	public void something_is_done() throws InterruptedException, FileNotFoundException, IOException {
		boolean res=true;
		String inputDatafilePath = System.getProperty("user.dir") + "/resources/redBusData.csv";
		List<String> fileEntries = new ArrayList<String>();
		fileEntries=redBusUIFunctions.createListFromcsv(inputDatafilePath,fileEntries);
		for(Object line:fileEntries) {

			String source=line.toString().split(",")[0].trim();
			String destination=line.toString().split(",")[1].trim();
			String dojFile=line.toString().split(",")[2].trim();
			String doj = redBusUIFunctions.dojFormatter(dojFile);

			System.out.println( source +  "," +  destination + "," + doj);
			Thread.sleep(300);
			res= res & redBusUIFunctions.busFinder(driver,wait,redBusHomePageUrl,source,destination,dojFile,doj,busList);		
		}
		assertTrue(res);

	}

	@Then("User is navigated to BusAvailability Page")
	public void something_is_expected() throws IOException {	
		String outPutFilePath = System.getProperty("user.dir") + "/resources/redBusOutPutData.csv";
		redBusUIFunctions.writeBusCount(outPutFilePath,busList);
		driver.quit();
	    
	    
	}

	


}
