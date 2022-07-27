package Functions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class redBusUIFunctions {



	public static List<String> createListFromcsv(String filePath, List<String> fileEntries) throws IOException {


		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = br.readLine()) != null)
				if(!line.startsWith("From,To,Date")) {
					fileEntries.add(line);
				}

		}

		return fileEntries;
	}

	public static boolean busFinder(WebDriver driver,WebDriverWait wait, String redBusHomePageUrl,String source,String destination,String dojFile,String doj,List<String> busList) throws InterruptedException {
       boolean res=false;
		//Explicit Wait
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@id='redBus']")));

       //Landing Page
		WebElement redBusTab = driver.findElement(By.xpath("//a[@id='redBus']"));
		redBusTab.sendKeys(Keys.ENTER);
		//Bus Booking Page
		WebElement fromField = driver.findElement(By.id("txtSource"));
		fromField.sendKeys(source);
		Thread.sleep(500);
		WebElement ToField = driver.findElement(By.id("txtDestination"));
		ToField.sendKeys(destination);
		Thread.sleep(500);
		driver.findElement(By.id("txtOnwardCalendar")).sendKeys(doj);
		driver.findElement(By.xpath("//button[@class='D120_search_btn searchBuses']")).sendKeys(Keys.ENTER);
	
		boolean noBusFound = true;

		try {
			driver.findElement(By.xpath("//div[@class='error-view oops-page']")).isDisplayed();
			busList.add(source+","+destination + "," + dojFile + ","+ "0");
			Thread.sleep(1200);
			res=true;
		} catch(Exception e) {
			// Element not found....
			noBusFound=false;
		}
		Thread.sleep(2800);


		if (!noBusFound) {
			res=true;
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@class='f-bold busFound']")));
			WebElement noOfBuses= driver.findElement(By.xpath("//span[@class='f-bold busFound']"));
			System.out.println("Number of Bus avil :" +  noOfBuses.getText());
			busList.add(source+","+destination + "," + dojFile + ","+noOfBuses.getText().split(" ")[0]);

		}
		Thread.sleep(500);
		driver.navigate().to(redBusHomePageUrl);
		return res;
	}

	public static void writeBusCount(String FileName, List<String> BusList) throws IOException {
		FileWriter writer = new FileWriter(FileName);
		writer.append("From,To,Date,Bus_Count" + "\n");
		for (String line:BusList) {
			writer.append(line);
			writer.append("\n");

		}
		writer.close();
	}


	public static String getMonthName(int monthNumber)
	{
		String monthName="";
		switch(monthNumber){
		case 1:
			monthName = "JAN";
			break;
		case 2:
			monthName = "FEB";
			break;
		case 3:
			monthName = "MAR";
			break;
		case 4:
			monthName = "APR";
			break;
		case 5:
			monthName = "MAY";
			break;
		case 6:
			monthName = "JUN";
			break;
		case 7:
			monthName = "JUL";
			break;
		case 8:
			monthName = "AUG";
			break;
		case 9:
			monthName = "SEP";
			break;
		case 10:
			monthName = "OCT";
			break;
		case 11:
			monthName = "NOV";
			break;
		case 12:
			monthName = "DEC";
			break;
		default:
			monthName = "Invalid month";
			break;
		}
		return monthName; 
	}

	public static String dojFormatter(String dojFile) {


		String dojFileFormat=dojFile.replaceAll("\\/", "-");	
		String monthInDigit=dojFileFormat.split("-")[1];
		String month= redBusUIFunctions.getMonthName(Integer.parseInt(monthInDigit));
		String doj = dojFileFormat.split("-")[0] + "-" + month + "-" + dojFileFormat.split("-")[2];
		return doj;

	}


}
