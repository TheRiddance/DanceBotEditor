package ch.ethz.asl.dancebots.danceboteditor;

/**
 * Created by andrin on 09.07.15.
 */
public class DanceBotEditorProjectFile {

    public boolean musicFileSelected;
    public boolean beatExtractionDone;
    public boolean startedEditing;

    private DanceBotMusicFile mDBMusicFile;
    private BeatGrid mBeatGrid;
    private ChoreographyManager mChoreoManager;

    /**
     * TODO comment
     */
    public DanceBotEditorProjectFile() {

        // Init all important dance bot editor file properties
        musicFileSelected = false;
        beatExtractionDone = false;
        startedEditing = false;

        mBeatGrid = new BeatGrid();
        mChoreoManager = new ChoreographyManager();
    }


    /**
     * TODO comment
     * @param dbMusicFile
     */
    public void attachMusicFile(DanceBotMusicFile dbMusicFile) {

        mDBMusicFile = dbMusicFile;
    }


    public DanceBotMusicFile getDanceBotMusicFile() {

        return mDBMusicFile;
    }

    public BeatGrid getBeatGrid() {

        return mBeatGrid;
    }
}
