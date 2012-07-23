import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ants.Action;
import ants.Ant;
import ants.Direction;
import ants.Surroundings;
import ants.Tile;


public class BaileyAnt implements Ant {
    Random rand;

    public BaileyAnt() {
        rand = new Random(System.currentTimeMillis());
    }

    @Override
    public Action getAction(Surroundings s) {
        List<Direction> dSet = new ArrayList<Direction>();
        for(Direction d : Direction.values()) {
            Tile t = s.getTile(d);
            if (t.isTravelable()) {
                dSet.add(d);
            }
        }

        return Action.move(dSet.get(rand.nextInt(dSet.size())));
    }

    @Override
    public byte[] send() {
        return null;
    }

    @Override
    public void receive(byte[] paramArrayOfByte) {
    }

}
