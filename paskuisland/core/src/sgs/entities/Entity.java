package sgs.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity {
	
	
	
	public Vector2 position;
	public GridPoint2 gridposition;
	
	public Entity() {
		position = new Vector2();
		gridposition = new GridPoint2();
	}
	
	public void disegnami(SpriteBatch batch) {
	}

}
