package com.fratics.precis.fis.feed.candidategeneration;

import com.fratics.precis.fis.base.BaseCandidateElement;
import com.fratics.precis.fis.base.PrecisProcessor;
import com.fratics.precis.fis.base.ValueObject;
import com.fratics.precis.fis.util.Util;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Date;

/*
 * This is the Second stage Candidate Generator. This is developed as a flow processor.
 * 
 * The Main functionalities are:-
 * 1) cross product on the first stage candidates to generate potential candidates.
 * 2) Applies the Base feed data on the potential candidates
 * 3) Applies the threshold handler to filter the successful candidates.
 * 
 */

public class CandidateGeneratorStage2V2 extends PrecisProcessor {
    private int currStage = 2;
    private ValueObject o;

    public CandidateGeneratorStage2V2() {
    }

    // Produces the cross product of the first stage candidates to
    // generate the 2nd Stage potential candidates.
    private void crossProduct() {
        BaseCandidateElement[] it = o.inputObject.firstStageCandidates.values()
                .toArray(new BaseCandidateElement[0]);
        // System.err.println(Arrays.toString(it));
        for (int i = 0; i < it.length; i++) {
            for (int j = i + 1; j < it.length; j++) {
                if (it[i].xor(it[j]).cardinality() == 4) {
                    BitSet b = it[i].or(it[j]);
                    o.inputObject.addCandidate(b);
                }
            }
        }
    }

    public boolean process(ValueObject o) throws Exception {
        this.o = o;
        o.inputObject.currentStage = currStage;
        System.err.println("Current Stage ::" + this.currStage);
        System.err.println("No of Candidates from Previous Stage ::"
                + o.inputObject.firstStageCandidates.values().size());

        // Generate Cross Product
        long milliSec1 = new Date().getTime();
        crossProduct();
        long milliSec2 = new Date().getTime();
        System.err.println("No of Candidates Before Applying Threshold::"
                + o.inputObject.currCandidateSet.size());
        System.err.println("Time taken in MilliSec for Candidate Gen ::"
                + (milliSec2 - milliSec1));
        milliSec1 = new Date().getTime();

        // o.inputObject.dataFeedTrie.print();

        for (ArrayList<BaseCandidateElement> al : o.inputObject.currCandidatePart
                .values()) {
            for (BaseCandidateElement bce : al) {
                // o.inputObject.applyBaseFeed(bce);
                o.inputObject.apply(bce);
            }
        }

        // Apply the threshold handler.
        boolean ret = o.inputObject.applyThreshold();
        milliSec2 = new Date().getTime();
        System.err.println("No of Candidates After Applying Threshold::"
                + o.inputObject.currCandidateSet.size());
        System.err.println("Time taken in MilliSec for Applying Threshold ::"
                + (milliSec2 - milliSec1));
        // Dump the Candidate Stage.
        if (ret)
            Util.dump(this.currStage, o);

        // Move the precis context to next stage - 3
        o.inputObject.moveToNextStage();
        return ret;
    }
}
