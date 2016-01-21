package ch.ethz.asl.dancebots.danceboteditor.utils;

import android.util.Log;

/**
 * Created by andrin on 25.10.15.
 */
public class Decoder {

    private static final String LOG_TAG = "DECODER";

    // TODO: Is static what we want?
    private static long mSoundFileHandle;

    public Decoder()
    {
        // Initialize sound handle to "null"
        mSoundFileHandle = 0;

        // Initialize native mp3 decoder
        int result = initialize();

        if(result != DanceBotError.MPG123_OK)
        {
            throw new java.lang.Error("Error: " + result + " initializing native Mp3Decoder");
        }
    }

    public void openFile(String filePath) {

        // Open new sound file
        mSoundFileHandle = open(filePath);

        if (mSoundFileHandle == 0) {
            throw new IllegalArgumentException("couldn't open file: " + filePath);
        }
    }

    public int decode() {
        return decode(mSoundFileHandle);
    }

    // TODO: making this method static is reeeeeally dangerous. What if mSoundFileHandle is not yet initialized?
    public static int transfer(short[] pcmBuffer) {
        return transfer(mSoundFileHandle, pcmBuffer);
    }

    public static int checkAudioFormat(String filePath) {

        // Initialize mpg123 library for this thread
        int result = initialize();

        if (result != DanceBotError.MPG123_OK) {
            throw new java.lang.Error("Error: " + result + " initializing native Mp3Decoder");
        }

        // Check (audio) file format
        int err = checkFormat(filePath);

        if (err == DanceBotError.MPG123_OK) {
            return DanceBotError.NO_ERROR;
        } else {
            return DanceBotError.MPG123_FORMAT_ERROR;
        }
    }

    /**
     * This function is really important.
     * It cleans up all internally (native) used data structures and objects.
     */
    public static void cleanUp() {
        if (mSoundFileHandle != 0) {
            cleanUp(mSoundFileHandle);
            mSoundFileHandle = 0;
        }
    }

    // TODO: IS THIS CALL SAFE?
    @Override
    protected void finalize() throws Throwable {
        //cleanUp();
        Log.d(LOG_TAG, "Decoder finalize()");
        super.finalize();
    }

    // TODO should this method be static, if mSoundFileHandle is static?
    public long getHandle() {
        return mSoundFileHandle;
    }
    public int getSampleRate() {
        return (int) getSampleRate(mSoundFileHandle);
    }
    public long getNumberOfSamples() {
        return getNumberOfSamples(mSoundFileHandle);
    }

    // Prepare native mp3 decoder
    private native static int initialize();
    // Initialize native decoder handler from selected music file
    private native static long open(String filePath);
    // Decode the currently opened music file
    private native static int decode(long soundFileHandle);
    private native static int transfer(long soundFileHandle, short[] pcmBuffer);
    // Delete native sound file
    private native static int cleanUp(long mSoundFileHandle);
    // Check audio format
    private native static int checkFormat(String filePath);

    // Get sample rate from selected song
    private native static long getSampleRate(long soundFileHandle);
    // Get total number of samples from selected song
    private native static long getNumberOfSamples(long soundFileHandle);
}
