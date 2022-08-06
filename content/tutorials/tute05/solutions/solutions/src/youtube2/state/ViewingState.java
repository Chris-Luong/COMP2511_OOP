package youtube2.state;

import youtube2.Viewing;

public abstract class ViewingState {

    private Viewing viewing;

    public ViewingState(Viewing viewing) {
        this.viewing = viewing;
    }

    public abstract String onLock();

    public abstract String onPlay();

    public abstract String onNext();

    public Viewing getViewing() {
        return viewing;
    }

}