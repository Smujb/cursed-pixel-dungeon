package com.shatteredpixel.yasd.general.levels.builders;

import com.shatteredpixel.yasd.general.levels.Level;
import com.shatteredpixel.yasd.general.levels.rooms.Room;
import com.shatteredpixel.yasd.general.levels.rooms.connection.ConnectionRoom;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class MetroidVaniaBuilder extends RegularBuilder {

	@Override
	public ArrayList<Room> build(ArrayList<Room> rooms, Level level) {

		setupRooms(rooms);

		if (entrance == null){
			return null;
		}

		float direction = Random.Float(0, 360);
		ArrayList<Room> branchable = new ArrayList<>();

		entrance.setSize();
		entrance.setPos(0, 0);
		branchable.add(entrance);

		if (shop != null){
			placeRoom(rooms, entrance, shop, direction + 180f);
		}

		int roomsOnPath = (int)(multiConnections.size()*pathLength) + Random.chances(pathLenJitterChances);
		roomsOnPath = Math.min(roomsOnPath, multiConnections.size());

		Room curr = entrance;

		float[] pathTunnels = pathTunnelChances.clone();
		for (int i = 0; i <= roomsOnPath; i++){
			if (i == roomsOnPath && exit == null)
				continue;

			int tunnels = Random.chances(pathTunnels);
			if (tunnels == -1){
				pathTunnels = pathTunnelChances.clone();
				tunnels = Random.chances(pathTunnels);
			}
			pathTunnels[tunnels]--;

			for (int j = 0; j < tunnels; j++){
				ConnectionRoom t = ConnectionRoom.createRoom(level, false);

				placeRoom(rooms, curr, t, direction + Random.Float(-pathVariance, pathVariance));
				branchable.add(t);
				rooms.add(t);
				curr = t;
			}

			Room r = (i == roomsOnPath ? exit : multiConnections.get(i));
			placeRoom(rooms, curr, r, direction + Random.Float(-pathVariance, pathVariance));
			branchable.add(r);
			curr = r;
		}

		ArrayList<Room> roomsToBranch = new ArrayList<>();
		for (int i = roomsOnPath; i < multiConnections.size(); i++){
			roomsToBranch.add(multiConnections.get(i));
		}
		roomsToBranch.addAll(singleConnections);
		weightRooms(branchable);
		createBranches(rooms, branchable, roomsToBranch, branchTunnelChances, level);

		findNeighbours(rooms);

		for (Room r : rooms){
			for (Room n : r.neigbours){
				if (!n.connected.containsKey(r)
						&& Random.Float() < extraConnectionChance){
					r.connect(n);
				}
			}
		}

		return rooms;

	}
}
