package dungeonmania.entities;
import dungeonmania.util.Position;

public class Switch extends StaticEntity{
    
    public Switch(Position position, String type, String id) {
        super(position, type, id);
        setSkin("default");
    }
}
