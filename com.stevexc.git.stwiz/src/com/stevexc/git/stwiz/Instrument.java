package com.stevexc.git.stwiz;

import java.util.ArrayList;

public class Instrument {

	private String instType;
	private int stringCount;
	private double totalTension;
	private ArrayList<StringTension> strings;
	private int magicNumber; // index of selected instrument type

	private String[] instrumentTypes = { "BASS", "5SBASS", "6SBASS", "SHORTBASS", "BARITONEGUITAR", "FENDERGUITAR",
			"GIBSONGUITAR", "7SGUITAR", "8SGUITAR", "BASSVI", "CUSTOM" };
	private int[] instrumentStrings = { 4, 5, 6, 4, 4, 6, 6, 7, 8, 6, 6 };
	private double[] instrumentScales = { 34, 35, 35, 30, 28, 25.5, 24.75, 26.5, 28, 30, 25.5 };
	private String[] tunings = { "E1,A1,D2,G2", "B0,E1,A1,D2,G2", "B0,E1,A1,D2,G2,C3", "E1,A1,D2,G2",
			"B1,E2,A2,D3,G3,B3", "E2,A2,D3,G3,B3,E4", "E2,A2,D3,G3,B3,E4", "B1,E2,A2,D3,G3,B3,E4",
			"F#1,B1,E2,A2,D3,G3,B3,E4", "E1,A1,D2,G2,B2,E3", "C2,F2,A#2,D#3,G3,C4" };
	private int[] woundPlain = { 4, 5, 6, 4, 4, 3, 3, 4, 5, 6, 4 };
	private String threshold = "10";

	public Instrument() {
		this("");
	}

	public Instrument(String instType) {
		int tempStrings = 0;
		for (int i = 0; i < instrumentTypes.length; i++) {
			// System.out.println("Does " + instType.toUpperCase() + " equal " +
			// instrumentTypes[i] + "?");
			if (instType.toUpperCase().equals(instrumentTypes[i])) {
				// System.out.println("y");
				tempStrings = instrumentStrings[i];
			}
		}
		setInstType(instType);
		strings = new ArrayList<StringTension>();
		setStringCount(tempStrings);
		setUpInstrument();
	}

	public void setInstType(String instType) {
		String type = instrumentTypes[instrumentTypes.length - 1];
		int number = instrumentTypes.length - 1;
		for (int i = 0; i < instrumentTypes.length; i++) {
			if (instType.equalsIgnoreCase(instrumentTypes[i])) {
				type = instrumentTypes[i];
				number = i;
			}
		}

		this.instType = type;
		setMagicNumber(number);
	}

	public void setStringCount(int stringCount) {
		this.stringCount = stringCount;
	}

	private void setMagicNumber(int magicNumber) {
		this.magicNumber = magicNumber;
	}

	public String getInstType() {
		String tempType = instType;
		return tempType;
	}

	public int getStringCount() {
		int tempCount = this.stringCount;
		return tempCount;
	}

	private void setTension(double totalTension) {
		this.totalTension = totalTension;
	}

	public double getTension() {
		double tempTension = totalTension;
		return tempTension;
	}

	private void setUpInstrument() {
		String splitBy = ",";
		int totalTension = 0;

		String stringBuilder;
		String tempNote;
		String tempSTypeTens;

		String[] defaultTuning = tunings[magicNumber].split(splitBy);
		String tempInst = getInstType();
		String tempScale = "" + instrumentScales[magicNumber];
		int switchSize = woundPlain[magicNumber];

		for (int i = 0; i < instrumentStrings[magicNumber]; i++) {
			//System.out.println("Top " + i);
			// System.out.println("Setting Up String: " + i + " Out Of " +
			// instrumentStrings.length);
			tempNote = defaultTuning[i];
			tempSTypeTens = switch (tempInst) {
			case "BASS", "5SBASS", "6SBASS", "SHORTBASS" -> "XLB,0.0,40";
			case "BASSVI" -> "NWB,0.0,30";
			default -> {
				if (i >= switchSize) {
					yield "PL,0.0,14";
				} else
					yield "NW,0.0,19";

			}
			};
			stringBuilder = tempInst + "," + tempNote + "," + tempScale + "," + tempSTypeTens;
			// System.out.println(stringBuilder);
			strings.add(new StringTension(stringBuilder));
			totalTension += strings.get(i).getTension();
			//System.out.println("Bottom " +i);
		}

		setTension(totalTension);
	}

	public String printString(int stringNum) {
		StringTension tempString = strings.get(stringNum);
		String tempOut = tempString.toString();
		return tempOut;
	}

	public String toString() {
		String tempOut = "";
			double totalTension = 0.0;
		if (getStringCount() > 0) {
			for (int i = 0; i < getStringCount(); i++) {
				tempOut += printString(i) + "\n";
				totalTension += strings.get(i).getTension();
			}
		} else {
			tempOut = "Not Initialized\n";
		}
		tempOut += "Total tension: " + totalTension;
		return tempOut;
	}

	public double setStringNote(int stringNum, String noteName) {
		double newTension = 0.0;

		if (strings.get(stringNum) != null) {
			strings.get(stringNum).setNote(noteName);
			newTension = strings.get(stringNum).calcTension();
		}

		return newTension;
	}

	public double setStringScale(int stringNum, double scaleLen) {
		double newTension = 0.0;
		if (strings.get(stringNum) != null) {
			strings.get(stringNum).setScale(scaleLen);
			newTension = strings.get(stringNum).calcTension();
		}
		return newTension;
	}

	public double setStringGauge(int stringNum, double gauge) {
		double newTension = 0.0;
		if (strings.get(stringNum) != null) {
			strings.get(stringNum).setGauge("" + gauge);
			newTension = strings.get(stringNum).calcTension();
		}
		return newTension;
	}

	public String setStringTension(int stringNum, double tension) {
		String newGauge = "0.0";
		if (strings.get(stringNum) != null) {
			strings.get(stringNum).setTension(tension);
			newGauge = strings.get(stringNum).calcGauge(threshold);
		}
		return newGauge;
	}
	
	public void setTensionThreshold(String thresh) {
		String newThresh = thresh;
		double validate;
		try {
			validate = Double.valueOf(newThresh);
			if(validate < 1 || validate > 100) {
				newThresh = "10";
			}
			
		}catch(NumberFormatException | NullPointerException e) {
			newThresh = "10";
		}
		this.threshold = newThresh;
	}

	public double setStringType(int stringNum, String stringType) {
		double newTension = 0.0;
		if (strings.get(stringNum) != null) {
			strings.get(stringNum).setStringType(stringType);
			newTension = strings.get(stringNum).calcTension();
		}
		return newTension;
	}
	
	public void setGlobalScale(double scale) {		
		for (int i = 0; i < getStringCount() - 1; i++) {
			strings.get(i).setScale(scale);
		}
	}
	
	public void setGlobalTension(double tension) {		
		for (int i = 0; i < getStringCount() - 1; i++) {
			strings.get(i).setTension(tension);
		}
	}
	
	public String calcInstrument() {
		for (int i = 0; i < getStringCount() - 1; i++) {
			strings.get(i).calcTension();
		}
		return toString();
	}
	
	public String calcInstrument(String type) {
		for (int i = 0; i < getStringCount() - 1; i++) {
			strings.get(i).calcGauge(type);
		}
		return toString();
	}

}
