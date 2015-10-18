//BEGIN
package core;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class ConvertS {

	// read URL
	public final String[] urlS = ReadURL();

	private static String[] ReadURL() {
		ArrayList<String> arrS2D = new ArrayList<String>();

		String csvFile = "./src/main/resources/URL.csv";
		BufferedReader br = null;
		String line = null;
		String[] csv = null;
		String SplitBy = "!#";

		try {
			br = new BufferedReader(new FileReader(csvFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		try {
			while ((line = br.readLine()) != null) {
				csv = line.split(SplitBy);
				if (csv.length == 1) {
					arrS2D.add(csv[0]);
				} else {
					for (int i = 0; i < csv.length; i++)
						arrS2D.add(csv[i]);
				}
			} // while ((line = br.readLine()) != null) {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		} // try {

		// convert
		String[] URLS2D = new String[arrS2D.size()];
		arrS2D.toArray(URLS2D);
		return URLS2D;
	}

	// showElement
	public static void showElement(WebDriver driver, WebElement element) {
		final String originalBackgroundColor = element
				.getCssValue("backgroundColor");
		final JavascriptExecutor romaTestJS = ((JavascriptExecutor) driver); // java
																				// script
		romaTestJS.executeScript(
				"arguments[0].style.backgroundColor = 'rgb(0,200,0)'", element);

		try {
			Thread.sleep(45);
		} catch (InterruptedException e) {
		}
		romaTestJS.executeScript("arguments[0].style.backgroundColor = '"
				+ originalBackgroundColor + "'", element);
	}

	public String[][] ConvertSToS2D() {

		// ArrayList
		ArrayList<String[]> arrS2D = new ArrayList<String[]>();
		WebElement element = null;
		String itemName = null;
		double itemPriceUSD = 0;

		// JSON
		String[][] JSONS2D = JSON.JSONS2D();
		// JSON

		// WebDriver
		WebDriver dr = null;
		String tWD = System.getProperties().getProperty("typeWebDriver");
		if ((tWD == null)) {
			// class org.openqa.selenium.htmlunit.HtmlUnitDriver
			dr = new HtmlUnitDriver();
			// ((HtmlUnitDriver) dr).setJavascriptEnabled(true);
		} else if ((tWD.equals("Firefox")) // ---
				|| (tWD.equals("Fox"))
				|| (tWD.equals("fox"))
				|| (tWD.equals("FirefoxDriver"))) {
			// class org.openqa.selenium.firefox.FirefoxDriver
			dr = new FirefoxDriver();
		} else {
			// class org.openqa.selenium.htmlunit.HtmlUnitDriver
			dr = new HtmlUnitDriver();
			// ((HtmlUnitDriver) dr).setJavascriptEnabled(true);
		}
		// WebDriver

		double doubleOld;
		double doubleNew;
		for (int i = 0; i < urlS.length; i++) {
			// URL
			// dr.get("http://www.amazon.com/Withings-70031601-Pulse-Heart-Monitor/dp/B00KBN1F5A/ref=pd_sim_sbs_sg_6?ie=UTF8&refRID=0VQXF03E0KCG4S8JZKF4");
			dr.get(urlS[i]);

			// поиск элемента
			element = dr.findElement(By.id("productTitle"));
			itemName = element.getText();
			if (dr.getClass().toString()
					.equals("class org.openqa.selenium.firefox.FirefoxDriver"))
				showElement(dr, element);
			element = dr.findElement(By.id("priceblock_ourprice"));
			if (dr.getClass().toString()
					.equals("class org.openqa.selenium.firefox.FirefoxDriver"))
				showElement(dr, element);
			itemPriceUSD = Double.parseDouble(element.getText().substring(1,
					element.getText().length()));

			// запись
			arrS2D.add(new String[] { "", "Name: " + itemName,
					"Name: " + itemName });
			arrS2D.add(new String[] { "", "US Prise: " + itemPriceUSD,
					"US Prise: " + itemPriceUSD });
			arrS2D.add(new String[] { "", "Country: " + JSONS2D[i][2],
					"Country: " + JSONS2D[i][2] });
			doubleOld = itemPriceUSD * Double.parseDouble(JSONS2D[i][7]);
			doubleNew = new BigDecimal(doubleOld).setScale(2,
					RoundingMode.HALF_EVEN).doubleValue();
			arrS2D.add(new String[] { "",
					"Local Price: " + JSONS2D[i][5] + doubleNew,
					"Local Price: " + JSONS2D[i][5] + doubleNew });
		} // for (int i = 0; i < urlS.length; i++) {

		// close
		dr.close();

		// convert
		String[][] S2D = new String[arrS2D.size()][];
		arrS2D.toArray(S2D);
		return S2D;
	}

	public static void main(String[] args) {
		ConvertS conv = new ConvertS();
		String[][] str = conv.ConvertSToS2D();
		int n = conv.urlS.length;

		for (int i = 0; i < n; i++) {
			System.out.println("Item_" + i + ": " + str[i * 4 + 0][2] + "; "
					+ str[i * 4 + 1][2] + "; " + str[i * 4 + 2][2] + "; "
					+ str[i * 4 + 3][2] + ";");
		}
	}
}
// END