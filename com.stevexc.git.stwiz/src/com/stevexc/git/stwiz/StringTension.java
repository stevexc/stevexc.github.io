package com.stevexc.git.stwiz;

import java.io.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class StringTension {

	private String instrumentType;
	private String note;
	private double scale;
	private String gauge;
	private String stringType;
	private double goalTension;

	private String[] validNotes = { "A", "B", "C", "D", "E", "F", "G", "A#", "Ab", "Bb", "C#", "Db", "D#", "Eb", "F#",
			"Gb", "G#" };
	private String[] validStringTypes = { "XLB", "CB", "PL", "NW", "NWB" };
	private ArrayList<String> values;
	private ArrayList<String> frequencies;

	/*
	 * public cString() { this(null, 0, 0.0, 0.0, null, 0.0); }
	 */
	public StringTension() {
		this("BLANK,0,0,0,,0");
	}

	public StringTension(String instrumentType, String note, double scale, String stringType, String gauge, 
			double goalTension) {
		this(instrumentType +","+ note +","+ scale +","+ stringType +","+ gauge +","+ goalTension);
	}

	public StringTension(String csvValues) throws IllegalArgumentException {
		//"FenderGuitar,A#0,24.75,NW,0.0,18"
		String instrumentType;
		String note;
		double scale;
		String stringType;
		String gauge;
		double goalTension;

//		System.out.println(csvValues);
		String splitBy = ",";
		String[] values = csvValues.split(splitBy);
		if (values.length == 4) {
			// System.out.println(csvValues);
			csvValues += ",0.0,0";
			// System.out.println(csvValues);
			values = csvValues.split(splitBy);
			// System.out.println(values[4]);
		}
		if (values.length != 6) {
			throw new IllegalArgumentException("Invalid parameters.");
		}
//		System.out.println("Type: " + values[0]);
//		System.out.println("Note: " + values[1]);
//		System.out.println("Scale: " + values[2]);
//		System.out.println("String Type: " + values[3]);
//		System.out.println("Gauge: " + values[4]);
//		System.out.println("Goal Tension: " + values[5]);
		
		instrumentType = values[0].toUpperCase();
		note = values[1];
		scale = Double.valueOf(values[2]);
		stringType = values[3];
		gauge = values[4];
		// System.out.println(values[5]);
		goalTension = Double.valueOf(values[5]);
		setInstrumentType(instrumentType);
		setNote(note);
		setScale(scale);
//		System.out.println("Setting gauge to " + gauge);
		setGauge(gauge);
//		System.out.println("In constructor, gauge set to " + this.gauge);
		setStringType(stringType);
		setTension(goalTension);
		
		buildValues();
		buildFrequencies();
		// System.out.println("Gauge = " + getGauge() + " and Tension = " +
		// getTension());
		if (getGaugeValue() == 0 && getTension() != 0) {
//			System.out.println("Building from Tension");
			calcGauge("10");
		} else if (getGaugeValue() != 0 && getTension() == 0) {
//			System.out.println("Building from Gauge");
			calcTension();
		}
		


		
		
//		System.out.println("String created: " + toString());

	}

	private void buildValues() {
		values = new ArrayList<String>();
		String line = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader("src/string_tension_calc/resources/values.csv"));
			while ((line = br.readLine()) != null) {
				values.add(line);
			}
			br.close();
		} catch (IOException e) {
			// System.out.println("Values file not found.");
		}
	}

	private void buildFrequencies() {
		frequencies = new ArrayList<String>();
		String line = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader("src/string_tension_calc/resources/frequencies.csv"));
			while ((line = br.readLine()) != null) {
				frequencies.add(line);
			}
			br.close();
		} catch (IOException e) {
			// System.out.println("Frequencies file not found.");
		}
	}

	public String getInstrumentType() {
		return instrumentType;
	}

	public String getNote() {
		return note;
	}

	public double getScale() {
		return scale;
	}

	public String getGauge() {
//		System.out.println("Gauge being returned: " + gauge);
		return gauge;
	}

	public double getGaugeValue() {
		double out;
		try{
			out = Double.valueOf(gauge);
		} catch(NumberFormatException | NullPointerException e) {
			out = 0.0;
		}
		return out;
	}

	public String getStringType() {
		return stringType;
	}

	public double getTension() {
		return goalTension;
	}

	private String getItemNumber() {
//		System.out.println("getItemNumber gauge: " + gauge);
//		System.out.println("getItemNumber getGauge(): " + getGauge());
		int offset = getGauge().indexOf(".") + 1;
//		System.out.println("Offset: " + offset);
		String wholeGauge = getGauge().substring(offset);
//		System.out.println("Substringing gauge to: " + wholeGauge);
		String suffix = "";
		if (getGaugeValue() == 0) {
			return "Unassigned";
		}
		while (wholeGauge.length() < 3) {
			wholeGauge = wholeGauge + "0";
		}
		if (getStringType() == "XLB") {
			if (wholeGauge.equals("0.020") || wholeGauge.equals("0.018")) {
				suffix = "P";
			} else {
				suffix = "W";
			}
		}
		String itemNumber = stringType + wholeGauge + suffix;
		return itemNumber;
	}

	public double getItemWeight() {
		String splitBy = ",";
		double weight = 0.0;

		for (int i = 0; i < values.size(); i++) {
			String[] row = values.get(i).split(splitBy);
			if (row[0].compareTo(getItemNumber()) == 0) {
				try {
					weight = Double.parseDouble("0" + row[1]);
				} catch (NumberFormatException e) {
					weight = 0.0;
				}
			}
		}
		return weight;

	}

	public void setInstrumentType(String instrumentType) {
		String type = switch (instrumentType.toUpperCase()) {
		case "BASS" -> "BASS";
		case "FENDERGUITAR" -> "FENDERGUITAR";
		case "BARITONEGUITAR" -> "BARITONEGUITAR";
		case "GIBSONGUITAR" -> "GIBSONGUITAR";
		case "SHORTBASS" -> "SHORTBASS";
		case "5SBASS" -> "5SBASS";
		case "6SBASS" -> "6SBASS";
		case "7SGUITAR" -> "7SGUITAR";
		case "8SGUITAR" -> "8SGUITAR";
		case "BASSVI" -> "BASSVI";
		default -> "CUSTOM";
		};
		this.instrumentType = type;
	}

	public void setNote(String note) {
		boolean noteFlag = false;
		boolean octFlag = false;
		String newNote;
		char newOctave;
		if (note.length() < 2 || note.length() > 3) {
			note = "C2";
		}
		newNote = note.substring(0, note.length() - 1);
		newOctave = note.charAt(note.length() - 1);
		for (String x : validNotes) {
			if (x.equals(newNote.toUpperCase())) {
				noteFlag = true;
			}
			if (Character.isDigit(newOctave)) {
				octFlag = true;
			}
		}
		if (noteFlag && octFlag) {
			newNote = newNote + newOctave;
			// System.out.println("Note value set to " + newNote);
		} else if (!noteFlag && octFlag) {
			newNote = switch (getInstrumentType()) {

			case "5SBASS", "6SBASS", "7SGUITAR", "BARITONEGUITAR" -> "B";
			case "BASS", "SHORTBASS", "FENDERGUITAR", "GIBSONGUITAR", "BASSVI" -> "E";
			case "8SGUITAR" -> "F#";
			case "CUSTOM" -> "C";
			default -> "C";
			};
			newNote += newOctave;
		} else if (noteFlag && !octFlag) {
			newOctave = switch (getInstrumentType()) {

			case "5SBASS", "6SBASS" -> 0;
			case "BASS", "SHORTBASS", "7SGUITAR", "BARITONEGUITAR", "8SGUITAR", "BASSVI" -> 1;
			case "FENDERGUITAR", "GIBSONGUITAR", "CUSTOM" -> 2;
			default -> 2;
			};
			newNote += newOctave;
		} else {
			newNote = "C2";
		}
		this.note = newNote;
	}

	public void setScale(double scale) {
		double newScale;
		if (scale < 50 && scale > 0) {
			newScale = scale;
			// System.out.println("Scale length value set to " + newScale);
		} else {
			newScale = switch (getInstrumentType()) {

			case "5SBASS", "6SBASS" -> 35;
			case "BASS" -> 34;
			case "SHORTBASS", "BASSVI" -> 30;
			case "BARITONEGUITAR" -> 28;
			case "FENDERGUITAR" -> 25.5;
			default -> 24.75;
			};
			// System.out.println("Invalid scale length: " + scale + ". Scale length value
			// set to: " + newScale);
		}
		this.scale = newScale;
	}

	public void setGauge(String gaugeIn) throws IllegalArgumentException {
		double gaugeCheck = 0.0;
		String newGauge = "0.0";
		try {
			gaugeCheck = Double.valueOf(gaugeIn);
			newGauge = gaugeIn;
//			System.out.println("Gauge in setGauge: " + newGauge);
			if (gaugeCheck == 0) {
//				System.out.println("Gauge is zero: " + gaugeCheck);
				newGauge = "0.0";
			} else if (gaugeCheck < 0.007 || gaugeCheck > 0.145) {
//				System.out.println("Out of bounds: " + gaugeCheck);
				throw new IllegalArgumentException();
			}
		} catch (IllegalArgumentException e) {
			newGauge = switch (getInstrumentType()) {

			case "5SBASS", "6SBASS" -> "0.125";
			case "BASS", "SHORTBASS" -> "0.105";
			case "BASSVI" -> "0.084";
			case "8SGUITAR" -> "0.074";
			case "BARITONEGUITAR" -> "0.068";
			case "7SGUITAR" -> "0.059";
			case "FENDERGUITAR", "GIBSONGUITAR" -> "0.049";
			default -> "0.056";
			};
			// System.out.println("Invalid value for gauge: " + gauge + ". Set to :" +
			// newGauge);
		}
//		System.out.println("Value to be set as gauge in setGauge: " + newGauge);
		this.gauge = newGauge;
//		System.out.println("This should match the last thing: " + this.gauge);

	}

	public void setStringType(String stringType) {
		/*
		 * Options are: XL Bass Round - XLB Chromes Stainless Bass Flat - CB Plain
		 * Steel- PL XL Nickel Guitar Round - NW Bass VI - XLB Guitar (auto select P or
		 * W based off tension) - GTR
		 */

		boolean flag = false;
		String newType;
		for (String x : validStringTypes) {
			if (x.equals(stringType.toUpperCase())) {
				flag = true;
			}
		}
		if (flag) {
			newType = stringType;
			// System.out.println("String type value set to " + newType);
		} else {
			newType = "NW";
			// System.out.println("Invalid input in setStringType:" + newType + "; value set
			// to XL Nickel Round (NW)");
		}
		this.stringType = newType;
	}

	public void setTension(double tension) {
		double newTension = 0;

		try {
			if (tension == 0) {
				newTension = 0;
			} else if (tension < 1 || tension > 75) {
				throw new InvalidRangeException("Value out of bounds");
			} else {
				newTension = tension;
			}
		} catch (NumberFormatException | InvalidRangeException e) {
			newTension = switch (getStringType()) {
			case "NWB" -> 20;
			case "XLB", "CB" -> 40;
			case "NW" -> 18;
			case "PL" -> 15;
			default -> 0;
			};

		}

		this.goalTension = newTension;
	}

	public String printString() {
		DecimalFormat dfWeight = new DecimalFormat("#.########");
		DecimalFormat dfTension = new DecimalFormat("#.##");
		String tension = "" + getTension();
		if (getGaugeValue() != (0)) {
			tension = dfTension.format(calcTension());
		}
		String out = "Note: " + getNote() + ", Scale Length: " + getScale() + ", Gauge: " + getGauge()
				+ ", String Type: " + getStringType() + ", Item Number: " + getItemNumber() + ", Unit Weight: "
				+ dfWeight.format(getItemWeight()) + ", String Tension: " + tension;
		// System.out.println("In method: " + out);
		return out;
	}

	public String toString() {
		return printString();
	}

	public double getFrequency() {
		String splitBy = ",";
		double freq = 0.0;

		for (int i = 0; i < frequencies.size(); i++) {
			String[] row = frequencies.get(i).split(splitBy);
			if (row[0].compareTo(getNote()) == 0) {
				return Double.parseDouble(row[1]);
			}
		}
		return freq;
	}

	public double calcTension() {
		double tension;
		// DecimalFormat df = new DecimalFormat("#.##");
		if (getGaugeValue() == 0) {
			// System.out.println("No gauge set. Calculate gauge instead.");
			return 0.0;
		} else {
			tension = (getItemWeight() * Math.pow((2 * getScale() * getFrequency()), 2)) / 386.4;
			// System.out.println("Tension: " + df.format(tension) + "lbs");
			return tension;
		}
	}

	private double calcTension(double weight, double scale, double freq) {
		double tension = (weight * Math.pow((2 * scale * freq), 2)) / 386.4;
		// DecimalFormat df = new DecimalFormat("#.##");
		// System.out.println("Tension: " + df.format(tension) + "lbs");
		return tension;
	}

	public String calcGauge(String type) {
		String gauge = "0";
		double distance = .1;
		if (getTension() == 0) {
//			System.out.println("Goal tension not set. Calculate tension instead.");
		} else {
			gauge = switch (type) {
			case "max" -> calcMaxGauge();
			case "min" -> calcMinGauge();
			default -> {
//				System.out.println("In range-based calculation.");
				try {
					distance = Double.valueOf(type) / 100;
					if (distance < 0 || distance > 1) {
						distance = .1;
//						System.out.println("Invalid tension threshold. Setting to 10%.");
					}
//					System.out.println("Good job. Setting to " + distance*100 + "%");
				} catch (NumberFormatException e) {
					distance = .1;
//					System.out.println("Invalid tension threshold. Setting to 10%.");
				}
				yield calcClosestGauge(distance);
			}
			};
		}

		setGauge(gauge);
		return gauge;

	}

	private String calcMaxGauge() {
		String gauge = "0";
		double goalWeight = (getTension() * 386.4) / Math.pow((2 * getScale() * getFrequency()), 2);
		double weight = 0.0;
		String[] in;
		Character isDigit;
		String[] nextLine;
		String splitBy = ",";

		for (int i = 0; i < values.size(); i++) {
			// in[0] = CB040, in[1] = .00032716
			in = values.get(i).split(splitBy);

			nextLine = new String[4];
			nextLine[3] = in[0];
			isDigit = in[0].charAt(2);
			if (Character.isDigit(isDigit)) {
				nextLine[0] = in[0].substring(0, 2);
				nextLine[1] = in[0].substring(2);
			} else {
				nextLine[0] = in[0].substring(0, 3);
				nextLine[1] = in[0].substring(3);
			}

			isDigit = in[0].charAt(in[0].length() - 1);
			if (Character.isDigit(isDigit)) {
				nextLine[1] = "0." + nextLine[1];
			} else {
				nextLine[1] = "0." + nextLine[1].substring(0, nextLine[1].length() - 1);
			}
			nextLine[2] = in[1];
			if (nextLine[0].equals(getStringType()) && Double.parseDouble(nextLine[2]) > weight
					&& goalWeight > Double.parseDouble(nextLine[2])) {
				weight = Double.parseDouble(nextLine[2]);
				gauge = nextLine[1];
			}
		}
		return gauge;

	}

	private String calcMinGauge() {
		String gauge = "0";
		double goalWeight = (getTension() * 386.4) / Math.pow((2 * getScale() * getFrequency()), 2);
		double weight = 1.0;
		String[] in;
		Character isDigit;
		String[] nextLine;
		String splitBy = ",";

		for (int i = values.size() - 1; i > 0; i--) {
			in = values.get(i).split(splitBy);

			nextLine = new String[4];
			nextLine[3] = in[0];
			isDigit = in[0].charAt(2);
			if (Character.isDigit(isDigit)) {
				nextLine[0] = in[0].substring(0, 2);
				nextLine[1] = in[0].substring(2);
			} else {
				nextLine[0] = in[0].substring(0, 3);
				nextLine[1] = in[0].substring(3);
			}

			isDigit = in[0].charAt(in[0].length() - 1);
			if (Character.isDigit(isDigit)) {
				nextLine[1] = "0." + nextLine[1];
			} else {
				nextLine[1] = "0." + nextLine[1].substring(0, nextLine[1].length() - 1);
			}
			nextLine[2] = in[1];
			if (nextLine[0].equals(getStringType()) && Double.parseDouble(nextLine[2]) < weight
					&& goalWeight < Double.parseDouble(nextLine[2])) {
				weight = Double.parseDouble(nextLine[2]);
				gauge = nextLine[1];
			}
		}
		return gauge;
	}

	public String calcClosestGauge(double distance) {
		String gauge = "0";
		String[] in;
		Character isDigit;
		String[] nextLine;
		String splitBy = ",";
		double tempTension = 0.0;
		double lastDiff = 100.0;
		double diff;

		for (int i = values.size() - 1; i > 0; i--) {
			in = values.get(i).split(splitBy);

			nextLine = new String[4];
			nextLine[3] = in[0];
			isDigit = in[0].charAt(2);
			if (Character.isDigit(isDigit)) {
				nextLine[0] = in[0].substring(0, 2);
				nextLine[1] = in[0].substring(2);
			} else {
				nextLine[0] = in[0].substring(0, 3);
				nextLine[1] = in[0].substring(3);
			}

			isDigit = in[0].charAt(in[0].length() - 1);
			if (Character.isDigit(isDigit)) {
				nextLine[1] = "0." + nextLine[1];
			} else {
				nextLine[1] = "0." + nextLine[1].substring(0, nextLine[1].length() - 1);
			}
			nextLine[2] = in[1];

			tempTension = calcTension(Double.parseDouble(nextLine[2]), getScale(), getFrequency());
			diff = Math.abs(tempTension - getTension());

			if (nextLine[0].equals(getStringType()) && diff < lastDiff) {
				lastDiff = diff;
				gauge = nextLine[1];
				// System.out.println("Chosen gauge in method: " + gauge);
			}
		}
//		System.out.println("Gauge calculated to be " + gauge);
		return gauge;
	}

}
