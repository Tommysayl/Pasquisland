package sgs.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;

import sgs.map.WorldMap;

public class Omino extends Entity {
	
	public static Texture texture = new Texture(Gdx.files.internal("spy.png"));

	public Omino(float x, float y) {
		super(x, y);
	}
	
	public void disegnami(SpriteBatch batch) {
		batch.draw(texture, position.x, position.y);
	}

}
