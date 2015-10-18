//BEGIN
package core;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

public class JSON {

	private static String[] ReadIP() {
		ArrayList<String> arrS2D = new ArrayList<String>();

		String csvFile = "./src/main/resources/IP.csv";
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
		String[] IPS2D = new String[arrS2D.size()];
		arrS2D.toArray(IPS2D);
		return IPS2D;
	}

	public static String[][] JSONS2D() {
		// read IP
		// final String[] AllIP = { "88.191.179.56", "61.135.248.220" };
		final String[] AllIP = ReadIP();

		final String[] JSONElementS = { "geoplugin_status",
				"geoplugin_countryName", "geoplugin_city",
				"geoplugin_currencyCode", "geoplugin_currencySymbol_UTF8" };
		final String[] JSONElementS2 = { "id", "Rate" };
		String[] urlRate = {
				"http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.xchange%20where%20pair%20in%20%28%22",
				"%22%29&format=json&env=store://datatables.org/alltableswithkeys" };

		String status = null; // 200
		String countryName = null; // France
		String city = null; // Paris
		String currencyCode = null; // EUR
		String currencySymbol_UTF8 = null; // &#8364; - €
		String id = null; // USDEUR
		String rate = null; // 0.7348

		ArrayList<String[]> JSONArList = new ArrayList<String[]>();
		InputStream is = null;
		JsonParser parser = null;
		URL url = null;

		for (int i = 0; i < AllIP.length; i++) {
			// ############Date###########################################
			try {
				url = new URL("http://www.geoplugin.net/json.gp?ip=" + AllIP[i]);
			} catch (MalformedURLException e1) {
				// String[][] s = new String[][] { { "1", "CAN'T GET RATE", "" }
				// };
				return new String[][] { { "1", "CAN'T GET DATE", "" } }; // s;
			}

			try {
				is = url.openStream();
			} catch (IOException e1) {
				return new String[][] { { "1", "CAN'T GET DATE", "" } };
			}
			parser = Json.createParser(is);

			while (parser.hasNext()) {
				Event e = parser.next();
				if (e == Event.KEY_NAME) {
					if (parser.getString().equals(JSONElementS[0])) {
						parser.next();
						status = parser.getString();
					} else if (parser.getString().equals(JSONElementS[1])) {
						parser.next();
						countryName = parser.getString();
					} else if (parser.getString().equals(JSONElementS[2])) {
						parser.next();
						city = parser.getString();
					} else if (parser.getString().equals(JSONElementS[3])) {
						parser.next();
						currencyCode = parser.getString();
					} else if (parser.getString().equals(JSONElementS[4])) {
						parser.next();
						currencySymbol_UTF8 = parser.getString();
					} // if (parser.getString().equals(JSONElementS[0])) {
				} // if (e == Event.KEY_NAME) {
			} // while (parser.hasNext()) {
				// ############Date###########################################

			// ############Rate###########################################
			try {
				url = new URL(urlRate[0] + "USD" + currencyCode + urlRate[1]);
			} catch (MalformedURLException e1) {
				// String[][] s = new String[][] { { "1", "CAN'T GET RATE", "" }
				// };
				return new String[][] { { "1", "CAN'T GET RATE", "" } }; // s;
			}

			try {
				is = url.openStream();
			} catch (IOException e1) {
				return new String[][] { { "1", "CAN'T GET RATE", "" } };
			}
			parser = Json.createParser(is);

			while (parser.hasNext()) {
				Event e = parser.next();
				if (e == Event.KEY_NAME) {
					if (parser.getString().equals(JSONElementS2[0])) {
						parser.next();
						id = parser.getString();
					} else if (parser.getString().equals(JSONElementS2[1])) {
						parser.next();
						rate = parser.getString();
					} // if (parser.getString().equals(JSONElementS[0])) {
				} // if (e == Event.KEY_NAME) {
			} // while (parser.hasNext()) {
				// ############Rate###########################################

			// запись
			JSONArList.add(new String[] { AllIP[i], status, countryName, city,
					currencyCode, currencySymbol_UTF8, id, rate });
		} // for(int i = 0; i < AllIP.length; i++)

		// convert
		String[][] JSONS2D = new String[JSONArList.size()][];
		JSONArList.toArray(JSONS2D);
		return JSONS2D;
	}
}
// END