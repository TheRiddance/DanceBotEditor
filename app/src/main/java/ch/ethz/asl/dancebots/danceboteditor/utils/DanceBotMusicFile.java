package ch.ethz.asl.dancebots.danceboteditor.utils;

/**
 * The DanceBotMusicFile class stores all relevant data about the selected song.
 * Meta data, details and extracted beats are stored in this object.
 */
public class DanceBotMusicFile {

    // Meta data
    private String mSongTitle;
    private String mSongArtist;
    private String mSongPath;

    // Details about selected song
    private int mDurationInMiliSeconds;
    private long mNumberOfSamples;
    private int mSampleRate;
    private int mChannels;

    // Decoding and Beat Extraction
    private int mNumberOfBeatsDetected;
    private int[] mBeatBuffer;

    public DanceBotMusicFile(String songTitle, String songArtist, String songPath, int duration) {

        mSongTitle = songTitle;
        mSongArtist = songArtist;
        mSongPath = songPath;
        mDurationInMiliSeconds = duration;
    }

    /**
     * Clean up decoder
     */
    public void cleanUp() {
        if (mSongPath != null) {
            Decoder.cleanUp();
        }
    }

    public void setSampleRate(int rate) {
        mSampleRate = rate;
    }
    public void setTotalNumberOfSamples(long samples) {
        mNumberOfSamples = samples;
    }
    public void setNumberOfBeatsDected(int numBeats) {
        mNumberOfBeatsDetected = numBeats;
    }

    public String getSongPath() {
        return mSongPath;
    }
    public String getSongTitle() {
        return mSongTitle;
    }
    public String getDurationReadable() {
        int seconds = (mDurationInMiliSeconds / 1000) % 60;
        int minutes = (mDurationInMiliSeconds / (1000*60));
        if (seconds < 10) {
            return Integer.toString(minutes) + ":0" + Integer.toString(seconds);
        } else {
            return Integer.toString(minutes) + ":" + Integer.toString(seconds);
        }
    }
    public int getDurationInMilliSecs() {
        return mDurationInMiliSeconds;
    }
    public int getBeatCount() {
        return mNumberOfBeatsDetected;
    }
    public long getSampleCount() {
        return mNumberOfSamples;
    }

    public int getSampleRate() {
        return mSampleRate;
    }

    public void setBeatBuffer(int[] beatBuffer) {
        mBeatBuffer = beatBuffer;
    }

    public int[] getBeatBuffer() {
        return mBeatBuffer;
    }

    public String getSongArtist() {
        return mSongArtist;
    }

    public int getBeatFromMicroSecs(long microSecs) {

        // Time in seconds
        double secs = microSecs * 0.001 * 0.001;

        // The n-th sample
        int sample = (int) (secs * getSampleRate());

        int i;

        for (i = 1; i < mNumberOfBeatsDetected; ++i) {

            int samplePos = mBeatBuffer[i];

            if (sample < samplePos) {
                break;
            }
        }

        // If time window too large, beat is ahead
        // Correct with -1
        return i - 1;
    }

}
