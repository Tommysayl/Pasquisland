package sgs.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Palma extends Entity{

	public static Texture texture = new Texture(Gdx.files.internal("cespuglio.png"));
	
	public Palma(float x, float y) {
		super(x, y);
		// TODO Auto-generated constructor stub
	}
	public void disegnami(SpriteBatch batch) {
		batch.draw(texture, position.x, position.y, 32, 32);
	}
	
}
