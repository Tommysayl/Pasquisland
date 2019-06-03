package sgs.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import java.util.Random;

public class Omino extends Entity {
	
	public static Texture texture = new Texture(Gdx.files.internal("spy.png"));
	  
	  public float strength; 
	  public float speed;
	  public float hunger; 
	  public float sociality;
      
	  public Omino(float x, float y, float strength, float speed, float hunger, float sociality) {
		super();
		super.position = new Vector2(x,y);
		float S = new Random().nextFloat();
		this.strength = S;
		float So = new Random().nextFloat();
		this.sociality = So;
		float Sp = new Random().nextFloat();
		this.speed = 2*So;
		this.hunger = 0;
		}
	
	public void disegnami(SpriteBatch batch) {
		batch.draw(texture, position.x, position.y);
	}
	public void cheDevofa{
		 
		
		
		}
    public void cheStamoAFa {
    	
    	
    }
    

}
 