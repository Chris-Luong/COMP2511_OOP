package youtube2.state;

import youtube2.Viewing;

public class ReadyState extends ViewingState {

    public ReadyState(Viewing viewing) {
        super(viewing);
    }

    @Override
    public String onLock() {
        getViewing().changeState(new LockedState(getViewing()));
        return "Locked";        
    }

    @Override
    public String onPlay() {
        String action = getViewing().startPlayback();
        getViewing().changeState(new PlayingState(getViewing()));
        return action;
    }

    @Override
    public String onNext() {
        return "Locked.";
    }
    
}