package ch.ethz.asl.dancebots.danceboteditor.model;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Author: Andrin Jenal
 * Copyright: ETH Zürich
 */
public class Choreography<T extends BeatElement> implements Serializable {

    private static final String LOG_TAG = "CHOREOGRAPHY";

    private ArrayList<T> mBeatElements;

    private int mNumBeats;
    // Hash map that stores unique ids with the corresponding dance sequence
    private HashMap<UUID, DanceSequence<T>> mDanceSequences;

    public Choreography(ArrayList<T> elems) {
        mBeatElements = elems;
        mNumBeats = elems.size();
        mDanceSequences = new HashMap<>();
    }

    /**
     * Add a new dance sequence to the choreography
     *
     * @param beatElem selected beat element
     * @param danceSequenceLength selected dance sequence length
     */
    public void addNewDanceSequence(T beatElem, int danceSequenceLength) {

        // Generate new unique dance sequence identifier
        UUID choreoID = UUID.randomUUID();

        // Fetch the starting element of the new dance sequence
        T startElem = beatElem;

        // Create new dance sequence instance
        DanceSequence<T> danceSeq = new DanceSequence<>(choreoID, startElem, danceSequenceLength);

        // Add dance sequence to the hash table
        if (!mDanceSequences.containsKey(choreoID)) {
            mDanceSequences.put(choreoID, danceSeq);
        } else {
            Log.d(LOG_TAG, "Error: Key already in hash table");
        }

        // Set unique dance sequence identifier, to the starting element of the dance sequence
        startElem.setChoreographyID(choreoID);

        // Get start element and length of dance sequence
        int startIdx = startElem.getBeatPosition();

        // Init dance sequence parameters
        int length = 1;
        int nextElemIdx = startIdx + 1;

        T nextElem;

        // Before we start adding new dance sequence elements check max length
        int maxLength = getMaxLength(startIdx, danceSequenceLength);

        // If max length is shorter than the chosen dance sequence length, we have to overwrite
        // the length values in the dance sequence and the current selected length value
        if (maxLength < danceSequenceLength) {
            danceSequenceLength = maxLength;
            danceSeq.setLength(maxLength);
        }

        /*
         * Update element if it does not belong to any choreography and if the current length is
         * less than the total choreography length, and current position is less than total beats
         */
        // TODO: Beautify
        while ((length < danceSequenceLength) && ((startIdx + length) < mNumBeats)) {

            // If nextElemIdx and length in valid range, fetch next element
            nextElem = mBeatElements.get(nextElemIdx);

            // THIS ENSURES, THAT NO OVERWRITES OF DANCE SEQUENCES OCCUR
            // Check if next element is already assigned to a dance sequence
            if (isNotAssignedToOther(choreoID, nextElem)) {

                // Copy UUID to the next element
                nextElem.setChoreographyID(choreoID);

                // Copy the element properties
                nextElem.setProperties(startElem);

                // Increment the current length
                length++;

                // Increment element
                nextElemIdx++;

            } else {

                // If the next element already was assigned, stop here.
                break;
            }

        }
    }

    /**
     * Compute the maximum length, that a dance sequence can possibly have, starting at start index
     * without interfering with other dance sequences
     *
     * @param startIdx start sequence of the selected dance sequence
     * @param danceSequenceLength expected dance sequence length
     * @return the maximum calculated length possible, starting from start index
     */
    private int getMaxLength(int startIdx, int danceSequenceLength) {
        // First element already belongs to the sequence
        int length = 1;

        // Start from second element and check whether it is already assigned
        int idx = startIdx + 1;
        while (idx < startIdx + danceSequenceLength && idx < mNumBeats) {
            if (mBeatElements.get(idx).getDanceSequenceId() != null) {
                break;
            }
            length++;
            idx++;
        }
        return length;
    }

    /**
     * Update existing BeatElements and handle (if there exist) remaining BeatElements
     *
     * @param elem dance sequence identifier for which elements should be updated
     * @param danceSequenceLength starting element
     */
    public void updateDanceSequence(T elem, int danceSequenceLength) {

        // Get dance sequence
        UUID danceSequenceID = elem.getDanceSequenceId();

        // Get starting element
        T startElement = mDanceSequences.get(danceSequenceID).getStartElement();

        // Copy selected properties
        startElement.setProperties(elem);

        // Remove old dance sequence
        removeDanceSequence(danceSequenceID, startElement.getBeatPosition() + 1, startElement.getBeatPosition() + mDanceSequences.get(danceSequenceID).getLength());

        // Add a new dance sequence
        addNewDanceSequence(startElement, danceSequenceLength);
    }


    /**
     * Remove dance sequence from hash map and reset beat elements from start to end index to default
     *
     * @param uuid the uuid of the dance sequence, which should be removed
     * @param startIdx start index belonging to the dance sequence
     * @param endIdx end index does not belong to dance sequence anymore
     */
    public void removeDanceSequence(UUID uuid, int startIdx, int endIdx) {

        // Delete dance sequence from hash map
        mDanceSequences.remove(uuid);

        // Iterate over all elements and set to default
        for (int i = startIdx; i < endIdx; ++i) {
            mBeatElements.get(i).setDefaultProperties();
        }
    }


    /**
     * Remove dance sequence from hash map and reset beat elements from start to end index to default
     *
     * @param selectedBeatElem beat element for which the dance sequence should be completely removed
     */
    public void removeDanceSequence(T selectedBeatElem) {

        // Get unique identifier of existing dance sequence
        UUID danceSequenceID = selectedBeatElem.getDanceSequenceId();

        // Get starting element
        T startElement = mDanceSequences.get(danceSequenceID).getStartElement();

        removeDanceSequence(danceSequenceID, startElement.getBeatPosition(), startElement.getBeatPosition() + mDanceSequences.get(danceSequenceID).getLength());
    }

    /**
     * Check if elem is not assigned to any other dance sequence
     *
     * @param choreoID existing dance sequence identifier
     * @param elem     the element which is checked
     * @return true if element is not yet assigned
     */
    private boolean isNotAssignedToOther(UUID choreoID, T elem) {
        return ((elem.getDanceSequenceId() == null) || (elem.getDanceSequenceId() == choreoID));
    }

    /**
     * @return BeatElement list
     */
    public ArrayList<T> getBeatElements() {
        return mBeatElements;
    }

    public DanceSequence<T> getDanceSequence(UUID choreographyID) {
        return mDanceSequences.get(choreographyID);
    }
}
