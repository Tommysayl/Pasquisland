package sgs.entities;


import java.util.Random;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import sgs.map.Mappone;
import sgs.pasquisland.Pasquisland;

public class Omino extends Entity {
	
	public static final int RAGGIO_VISIVO = 2;
	public static Texture texture = new Texture(Gdx.files.internal("spy.png"));
	  
	  public float strength; 
	  public float speed;
	  public float hunger; 
	  public float sociality;
      public Entity Obiettivo;
      public String tribu;
      
	  public Omino(float x, float y) {
		super(x,y);
 		Random r = ((Pasquisland) Gdx.app.getApplicationListener()).getRandom();
		this.strength =  r.nextFloat();
		this.sociality =  r.nextFloat();
		this.speed =  r.nextInt(10)+16;
		tribu = dammiTribu();
		this.hunger = 0; 		
		}
	
	private String dammiTribu() {
		char[] tribu =new char[3];
		
		if (this.strength < 0.33f)    //assegno una tribu ad ogni omino
			tribu[1] = 'a';
		else if (this.strength > 0.33 && this.strength <= 0.66)
			tribu[1] = 'b';
		else if (this.strength > 0.66)
			tribu[1] = 'c';
		
		if (this.sociality < 0.33f) 
			tribu[2] = 'a';
		else if (this.sociality > 0.33 && this.strength <= 0.66)
			tribu[2] = 'b';
		else if (this.sociality > 0.66)
			tribu[2] = 'c';
		
		if (this.speed < 0.66f) 
			tribu[3] = 'a';
		else if (this.speed > 0.66 && this.strength <= 1.32)
			tribu[3] = 'b';
		else if (this.speed > 0.32)
			tribu[3] = 'c';
		
		return String.copyValueOf(tribu); 
	}

	public void disegnami(SpriteBatch batch) {
		batch.draw(texture, position.x, position.y);
	}
	
	//metodo che prende in input un array con tutte le entità vicine all'omino e decide cosa fare => non returna niente
	public void cheDevoFa() { 
		
		Array<Entity> Dintorni = Mappone.getInstance().vedi(this, Omino.RAGGIO_VISIVO); //ritorna la lista per CheDevoFa di tipi generici importando la funzione vedi da mappone
		float [] Score = new float[Dintorni.size]; //lista di score 
		for (int i = 0; i < Dintorni.size; i++) {//per il scorrere el in una lista scrivo for tipo di el della lista, nome che voglio dare agli el, :, nome della lista
			if (Dintorni.get(i) instanceof Palma)  
				Score[i] = (this.hunger * this.hunger) / (this.position.dst2(Dintorni.get(i).position));
			else if (Dintorni.get(i) instanceof Omino) {
				if (this.tribu == ((Omino) Dintorni.get(i)).tribu) 
			       Score[i] = (this.sociality * this.sociality) / (this.position.dst2(Dintorni.get(i).position));
			    else if  (this.tribu != ((Omino)Dintorni.get(i)).tribu)	//perche altrimenti selezionerei anche le posizioni vuote
			    		Score[i] = (this.strength * this.hunger) / (this.position.dst2(Dintorni.get(i).position));
			}
		}
		float max = 0;
		int j = 0;
	    for (int i = 0; i < Score.length;i++) {
		    if (Score[i] > max ) {
		    	max = Score[i];
		    	j = i; //j è l'indice del massimo
		    }  
	    }
		Obiettivo = Dintorni.get(j);   
	}
	public void move(Entity Obiettivo) {
		    Vector2 direction = Obiettivo.position.cpy().sub(this.position);
	        direction = direction.nor();
	        position.x += direction.x;
	        position.y += direction.y;
		}
	
	
    public void cheStamoAFa() {

    }
}
