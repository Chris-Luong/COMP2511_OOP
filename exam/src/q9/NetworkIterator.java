package q9;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NetworkIterator<P extends Comparable<P>> implements Iterator<P> {

    private Iterator<P> iter;
    List<Person<P>> members = new ArrayList<Person<P>>();

    public NetworkIterator(Iterator<P> iter) {
        this.iter = iter;
    }

    public void setMembers(List<Person<P>> newMemberList) {
        this.members = newMemberList;
    }

    @Override
    public boolean hasNext() {
        return iter.hasNext();
    }

    @Override
    public P next() {
        return iter.next();
    }

}
