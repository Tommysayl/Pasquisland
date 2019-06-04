package sgs.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;

import sgs.map.WorldMap;

public abstract class Entity {
	
	public Vector2 position;
	public GridPoint2 gridposition;
	public float life;
	
	public Entity(Vector2 pos) {
		life = 1;
		position = pos;
		gridposition= new GridPoint2((int)pos.x/WorldMap.tile_size, (int)pos.y/WorldMap.tile_size);
	}
	
	public Entity(float x, float y) {
		this(new Vector2(x, y));
	}
	
	public void disegnami(SpriteBatch batch) {
	}
	
	public void update(float delta) {
			
	}


}