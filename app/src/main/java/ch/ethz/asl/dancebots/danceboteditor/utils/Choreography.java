package ch.ethz.asl.dancebots.danceboteditor.utils;

import java.util.ArrayList;

import ch.ethz.asl.dancebots.danceboteditor.model.BeatElement;

/**
 * Created by andrin on 16.10.15.
 */
public class Choreography<T extends BeatElement> {

    private static final String LOG_TAG = "CHOREOGRAPHY";

    //TODO -> change to private
    private ArrayList<T> mBeatElements;

    public Choreography(ArrayList<T> elems) {

        mBeatElements = elems;
    }

    public void addBeatElement(T elem) {

        mBeatElements.add(elem);
    }

    public void addSequence(T startElem) {

        int startIdx = startElem.getChoreoStartIdx();
        int choreoLength = startElem.getChoreoLength();

        int length = 1;
        int nextElemIdx = startIdx + 1;

        T nextElem = mBeatElements.get(nextElemIdx);

        // Update element if it does not belong to any choreography and if the current length is
        // less than the total choreography length
        while (isNotAssigned(nextElem) && (length < choreoLength)) {

            // Copy the element properties
            nextElem.setProperties(startElem);

            // Increment the current length
            length += 1;

            // Increment element
            nextElemIdx += 1;
            nextElem = mBeatElements.get(nextElemIdx);
        }
    }

    public void removeSequence(T startElem) {

        int startIdx = startElem.getChoreoStartIdx();
        int choreoLength = startElem.getChoreoLength();

        // At least the clicked element belongs to a choreography
        int length = 1;
        int nextElemIdx = startIdx + 1;

        T nextElem = mBeatElements.get(nextElemIdx);

        // Update element if it does not belong to any choreography and if the current length is
        // less than the total choreography length
        while (nextElem.isSameDanceSequence(startElem) && (length < choreoLength)) {

            // Copy the element properties
            nextElem.setDefaultProperties();

            // Increment the current length
            length += 1;

            // Increment element
            nextElemIdx += 1;
            nextElem = mBeatElements.get(nextElemIdx);
        }
    }

    private boolean isNotAssigned(T elem) {
        return (elem.getChoreoStartIdx() == -1);
    }

    public ArrayList<T> getBeatElements() {
        return mBeatElements;
    }
}
