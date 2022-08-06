package youtube2.state;

import youtube2.Viewing;

public class LockedState extends ViewingState {

    public LockedState(Viewing viewing) {
        super(viewing);
        viewing.setPlaying(false);
    }

    @Override
    public String onLock() {
        if (getViewing().isPlaying()) {
            getViewing().changeState(new ReadyState(getViewing()));
            return "Stop playing";
        } else {
            return "Locked.";
        }
    }

    @Override
    public String onPlay() {
        getViewing().changeState(new ReadyState(getViewing()));
        return "Ready...";
    }

    @Override
    public String onNext() {
        return "Error: Locked";
    }
    
}