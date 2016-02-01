package com.precis.aprioriOnAggregates.singleMachine;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;

import com.precis.bitSetEvaluation.CanditateGeneratorStageKGT2;

public class BitSetStrToBitSetConverter {
    static final String openBrace = "{";
    static final String comma = ",";
    static final String closeBrace = "}";

    private static HashSet<BitSet> passedCandidates = new HashSet<BitSet>();

    public BitSetStrToBitSetConverter(String bitSetRep) {
	// System.out.println(bitSetRep);
	// System.out.flush();
	String[] parts1 = bitSetRep.split("\\{");
	String[] parts2 = parts1[1].split(closeBrace);
	String commaSeparatedBitSet = parts2[0];
	String[] bitPos = commaSeparatedBitSet.split(comma);
	BitSet b = new BitSet(35);
	for (int i = 0; i < bitPos.length; i++) {
	    b.set(Integer.parseInt(bitPos[i].trim()));
	}

	passedCandidates.add(b);
    }

    public static void main(String[] args) throws Exception {
	String fName = args[0];
	populateTreeSet(fName);

	CanditateGeneratorStageKGT2 candStageKGT2 = new CanditateGeneratorStageKGT2(
		4, passedCandidates);
	HashMap<BitSet, ArrayList<BitSet>> cdParttion = candStageKGT2
		.getCandidatePartition();

	BitSet allOneBS1 = new BitSet(35);
	allOneBS1.flip(0, 34);

	BitSet allOneBS2 = new BitSet(35);
	allOneBS2.flip(0, 34);

	for (ArrayList<BitSet> bsList : cdParttion.values()) {
	    for (int i = 0; i < bsList.size() - 1; i++) {
		allOneBS1.and(bsList.get(i));
		for (int j = i + 1; j < bsList.size(); j++) {
		    allOneBS1.xor(bsList.get(j));
		    if (allOneBS1.cardinality() == 4) {
			BitSet newCand = (BitSet) bsList.get(i).clone();
			System.out.println("New Candidate::"
				+ newCand.toString());
			newCand.or(bsList.get(j));
			System.out.println("New Candidate to test::"
				+ newCand.toString());
			if (candStageKGT2.isPresent(newCand)) {
			    System.out
				    .println("Candidate Present in Base Feed::"
					    + newCand.toString());
			}

		    }
		    allOneBS1.or(allOneBS2);
		    allOneBS1.and(bsList.get(i));
		}
		allOneBS1.or(allOneBS2);
	    }
	}
	System.out.flush();
    }

    public static void populateTreeSet(String fName) throws Exception {
	// BitSetStrToBitSetConverter b2 = new
	// BitSetStrToBitSetConverter(fName);
	FileInputStream fstream = new FileInputStream(fName);
	BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
	String strLine;

	BitSetStrToBitSetConverter bsToBitConv = null;
	// Read File Line By Line
	while ((strLine = br.readLine()) != null) {
	    bsToBitConv = new BitSetStrToBitSetConverter(strLine);
	}
	// Close the input stream
	br.close();
    }

    public HashSet<BitSet> getBitTreeSet() {
	return passedCandidates;
    }

}
