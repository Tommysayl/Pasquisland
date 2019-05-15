package sgs.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Omino extends Entity {
	
	public static Texture texture = new Texture(Gdx.files.internal("spy.png"));

	public Omino(float x, float y) {
		super();
		super.position = new Vector2(x,y);
	}
	
	public void disegnami(SpriteBatch batch) {
		batch.draw(texture, position.x, position.y);
	}

}
