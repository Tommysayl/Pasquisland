package sgs.entities;


import java.util.Random;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;

import sgs.map.Mappone;
import sgs.map.WorldMap;
import sgs.pasquisland.Pasquisland;
import sgs.utils.Tuple;

public class Omino extends Entity {
	
	public static final int RAGGIO_VISIVO = 4;
	public static final float ACTION_DST = 20;
	public static final float BLOCCO_INT = 2;
	public static Texture texture = new Texture(Gdx.files.internal("spy.png"));
	  
	  public float strength; 
	  public float speed;
	  public float hunger; 
	  public float sociality;
      public Entity Obiettivo;
      public String tribu;
      Array<Tuple<Entity,Float>> UltimiInc;
      Array<Tuple<Entity,Float>> toRemove;
      
	  public Omino(float x, float y) {
		super(x,y);
 		Random r = ((Pasquisland) Gdx.app.getApplicationListener()).getRandom();
		this.strength =  r.nextFloat();
		this.sociality =  r.nextFloat();
		this.speed =  r.nextInt(32)+32;
		tribu = dammiTribu();
		this.hunger = .1f; 	
		life = 20;
		UltimiInc = new Array<Tuple<Entity,Float>>();
		toRemove = new Array<Tuple<Entity,Float>>();

		}
	
	private String dammiTribu() {
		char[] tribu =new char[3];
		
		if (this.strength < 0.33f)    //assegno una tribu ad ogni omino
			tribu[0] = 'a';
		else if (this.strength > 0.33 && this.strength <= 0.66)
			tribu[0] = 'b';
		else if (this.strength > 0.66)
			tribu[0] = 'c';
		
		if (this.sociality < 0.33f) 
			tribu[1] = 'a';
		else if (this.sociality > 0.33 && this.sociality <= 0.66)
			tribu[1] = 'b';
		else if (this.sociality > 0.66)
			tribu[1] = 'c';
		
		if (this.speed <= 42) 
			tribu[2] = 'a';
		else if (this.speed >42 && this.speed <= 53)
			tribu[2] = 'b';
		else if (this.speed > 53)
			tribu[2] = 'c';
		
		return String.copyValueOf(tribu); 
	}

	public void disegnami(SpriteBatch batch) {
		batch.draw(texture, position.x, position.y);
	}
	
	//metodo che prende in input un array con tutte le entità vicine all'omino e decide cosa fare => non returna niente
	public void cheDevoFa() { 
		
		Array<Entity> Dintorni = Mappone.getInstance().vedi(this, Omino.RAGGIO_VISIVO); //ritorna la lista per CheDevoFa di tipi generici importando la funzione vedi da mappone
		for(int i=0;i<UltimiInc.size;i++){
			Dintorni.removeValue(UltimiInc.get(i).getLeft(), true);
		}
		float [] Score = new float[Dintorni.size]; //lista di score 
		for (int i = 0; i < Dintorni.size; i++) {//per il scorrere el in una lista scrivo for tipo di el della lista, nome che voglio dare agli el, :, nome della lista
			if (Dintorni.get(i) instanceof Palma)  
				Score[i] = (this.hunger * this.hunger);
			else if (Dintorni.get(i) instanceof Omino) {
				if (tribu.equals(((Omino) Dintorni.get(i)).tribu))
			      if (life >=15)
			    	  Score[i] = 0;
			      else
					Score[i] = (this.sociality * this.sociality) ;
			    else //perche altrimenti selezionerei anche le posizioni vuote
			    	Score[i] = (this.strength * this.hunger);
			}
			else 
				Score[i] = 0;
		}
		float max = Score[0];
		int j = 0;
	    for (int i = 0; i < Score.length;i++) {
		    if (Score[i] > max ) {
		    	max = Score[i];
		    	j = i; //j è l'indice del massimo
		    }  
	    }
		Obiettivo = Dintorni.get(j);   
	}
	public void move(float delta) {
		    Vector2 direction = Obiettivo.position.cpy().sub(position);
		    direction.nor().scl(speed*delta);
	        position.add(direction);
	        
	        if (position.x / WorldMap.tile_size != gridposition.x || position.y / WorldMap.tile_size != gridposition.y) {
	        	Mappone.getInstance().chiCeStaQua(gridposition).removeValue(this,  true);
	        	gridposition.set((int) position.x / WorldMap.tile_size, (int) position.y / WorldMap.tile_size);
	        	Mappone.getInstance().chiCeStaQua(gridposition).add(this);
	        }
		}
	
	public void update(float delta) {
		for (int i=0;i<UltimiInc.size;i++) {
			UltimiInc.get(i).setRight((UltimiInc.get(i).getRight()) - delta);
			if (UltimiInc.get(i).getRight() <= 0 || UltimiInc.get(i).getLeft().life <= 0)
					toRemove.add(UltimiInc.get(i));
		}
		UltimiInc.removeAll(toRemove, true);
		toRemove.clear();		
				
				
		if (Obiettivo == null || Obiettivo.life <= 0)
			cheDevoFa();
		else
			move(delta);
		
		if (position.dst(Obiettivo.position) < ACTION_DST)
        	cheStamoAFa();
		
		hunger += delta;
		life -= delta;
	}
	
    public void cheStamoAFa() {
    	if (Obiettivo instanceof Palma) {
    		Obiettivo.life = -1;
    		hunger = 0;
    		return;
    	}
    	
    	if (Obiettivo instanceof Omino) {
    		if (tribu.equals(((Omino) Obiettivo).tribu)) {
    			if (sociality > hunger) {
    				Mappone.getInstance().spawnaBimbo(this, (Omino) Obiettivo);
    				UltimiInc.add(new Tuple<Entity, Float>(Obiettivo,BLOCCO_INT));
    			}
    			else {
    				Obiettivo.life = -1;
    				hunger = 0;
    			}
    		}
    		else {
    			if (((Omino) Obiettivo).strength > strength ) {
    				life = -1;
    				((Omino) Obiettivo).hunger = 0;
    			}
    			else {
    				((Omino) Obiettivo).life = -1;
    				hunger = 0;
    			}
    			
    		}
    	}
    }
}
