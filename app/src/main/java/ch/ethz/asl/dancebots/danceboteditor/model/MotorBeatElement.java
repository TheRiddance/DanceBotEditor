package ch.ethz.asl.dancebots.danceboteditor.model;

import android.content.Context;
import android.util.Log;

import ch.ethz.asl.dancebots.danceboteditor.R;

/**
 * Created by andrin on 04.09.15.
 */
public class MotorBeatElement extends BeatElement<MotorType> {

    private static final String LOG_TAG = "MOTOR_BEAT_ELEMENT";

    private int mVelocityLeftIdx;
    private int mVelocityRightIdx;

    public MotorBeatElement(Context context, int beatPos, int samplePos, MotorType[] types) {

        // Parent constructor call
        super(context, beatPos, samplePos);

        // Initialize beat element properties
        mMotionTypes = types;

        // Initialize specific motor element default properties
        mVelocityLeftIdx = 0;
        mVelocityRightIdx = 0;
        mColor = mContext.getResources().getColor(R.color.motor_list_default_color);

    }

    /**
     * Set motor element properties based on specific input value
     * @param idx
     */
    public void setVelocityLeftIdx(int idx) {

        // Set motor element specific properties
        mVelocityLeftIdx = idx; // TODO Check that idx is a valid number?
    }

    /**
     * Set motor element properties based on specific input value
     * @param idx
     */
    public void setVelocityRightIdx(int idx) {

        // Set motor element specific properties
        mVelocityRightIdx = idx; // TODO Check that idx is a valid number?
    }

    public void setVelocityIndices(BeatElement elem) {

        // TODO is this cast type safe?
        setVelocityLeftIdx(((MotorBeatElement) elem).getVelocityLeftIdx());
        setVelocityRightIdx(((MotorBeatElement) elem).getVelocityRightIdx());
    }

    @Override
    protected void setColorAndTag() {
        switch (mMotionTypes[mMotionTypeIdx]) {

            case STRAIGHT:
                mColor = mContext.getResources().getColor(R.color.motor_elem_color1);
                mChoreoTag = "S";
                break;

            case SPIN:
                mColor = mContext.getResources().getColor(R.color.motor_elem_color2);
                mChoreoTag = "P";
                break;

            case TWIST:
                mColor = mContext.getResources().getColor(R.color.motor_elem_color3);
                mChoreoTag = "T";
                break;

            case BACK_AND_FORTH:
                mColor = mContext.getResources().getColor(R.color.motor_elem_color4);
                mChoreoTag = "B";
                break;

            case CONSTANT:
                mColor = mContext.getResources().getColor(R.color.motor_elem_color5);
                mChoreoTag = "C";
                break;

            case WAIT:
                mColor = mContext.getResources().getColor(R.color.motor_elem_color6);
                mChoreoTag = "W";
                break;

            default:
                Log.e(LOG_TAG, "Error switch: " + mMotionTypes[mMotionTypeIdx]);
                break;
        }
    }

    @Override
    public void setProperties(BeatElement elem) {

        // Set general beat element properties
        super.setProperties(elem);

        // Set motor element specific properties
        setVelocityIndices(elem);
    }

    @Override
    public boolean hasChoreography() {
        return mHasChoreography;
    }

    public int getVelocityLeftIdx() {
        return mVelocityLeftIdx;
    }

    public int getVelocityRightIdx() {
        return mVelocityRightIdx;
    }
}
