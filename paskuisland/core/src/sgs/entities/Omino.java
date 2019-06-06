package sgs.entities;


import java.util.Random;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
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
	public static final float PREGNANCY = .2f;
	public static final float HUNGER_PER_SECOND = .2f; // la fame arriva a 1 in 5 sec
	public static Texture texture = new Texture(Gdx.files.internal("spy.png"));
	  
	  public float strength; 
	  public float speed;
	  public float hunger; 
	  public float sociality;
	  public float wait_for_next_child;
	  public Entity Obiettivo;
	  public String tribu;
	  
	  //Array<Tuple<Entity,Float>> UltimiInc;
	  //Array<Tuple<Entity,Float>> toRemove;
	  
	  public Omino(float x, float y) {
		super(x,y);
		Random r = ((Pasquisland) Gdx.app.getApplicationListener()).getRandom();
		setValues(r.nextFloat(),r.nextFloat(),r.nextInt(64)+32);
		
		this.hunger = 0f; 	
		life = 20;
		//UltimiInc = new Array<Tuple<Entity,Float>>();
		//toRemove = new Array<Tuple<Entity,Float>>();
		wait_for_next_child = PREGNANCY;
		}
	  
	  public void setValues(float strength, float sociality, float speed) {
		  this.strength =  strength;
		  this.sociality =  sociality;
		  this.speed =  speed;
		  tribu = dammiTribu();
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
		
		if (this.speed <= 53) 
			tribu[2] = 'a';
		else if (this.speed >53 && this.speed <= 73)
			tribu[2] = 'b';
		else if (this.speed > 73)
			tribu[2] = 'c';
		
		return String.copyValueOf(tribu); 
	}
	
	private void setTribuColor(SpriteBatch batch) {
		float[] rgb = new float[3];
		int curr_col = 0;
		for (char c : tribu.toCharArray()) {
			switch (c) {
			case 'a':
				rgb[curr_col] = 0;
				break;
			case 'b':
				rgb[curr_col] = 0.5f;
				break;
			case 'c':
				rgb[curr_col] = 1;
				break;
			}
			
			curr_col++;
		}
		
		batch.setColor(rgb[0], rgb[1], rgb[2], 1);
	}

	public void disegnami(SpriteBatch batch) {
		setTribuColor(batch);
		batch.draw(texture, position.x, position.y);
		batch.setColor(Color.WHITE);
	}
	
	//metodo che prende in input un array con tutte le entit� vicine all'omino e decide cosa fare => non returna niente
	public void cheDevoFa() { 
		
		
		Array<Entity> Dintorni = Mappone.getInstance().vedi(this, Omino.RAGGIO_VISIVO); //ritorna la lista per CheDevoFa di tipi generici importando la funzione vedi da mappone
		//Rimuoviamo dalla lista quelli con cui ci siamo appena accoppiati
		/*for(int i=0;i<UltimiInc.size;i++){
			Dintorni.removeValue(UltimiInc.get(i).getLeft(), true);
		}*/
		
		float [] Score = new float[Dintorni.size]; //lista di score 
		for (int i = 0; i < Dintorni.size; i++) {//per il scorrere el in una lista scrivo for tipo di el della lista, nome che voglio dare agli el, :, nome della lista
			float dst = gridposition.dst2(Dintorni.get(i).gridposition) / (2*RAGGIO_VISIVO * RAGGIO_VISIVO);
			if (Dintorni.get(i) instanceof Palma)  
				Score[i] = (this.hunger * this.hunger) / (dst + 1);
			else if (Dintorni.get(i) instanceof Omino) {
				if (tribu.equals(((Omino) Dintorni.get(i)).tribu))
			      if (life >=15)
			    	  Score[i] = 0;
			      else
					Score[i] = (this.sociality + 1) / (dst + 1);
			    else //perche altrimenti selezionerei anche le posizioni vuote
			    	Score[i] = (this.strength * this.hunger) / (dst + 1) - .5f;
			}
			else 
				Score[i] = -1;
		}
		float max = Score[0];
		int j = 0;
	    for (int i = 0; i < Score.length;i++) {
		    if (Score[i] > max ) {
		    	max = Score[i];
		    	j = i; //j � l'indice del massimo
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
		/*for (int i=0;i<UltimiInc.size;i++) {
			UltimiInc.get(i).setRight((UltimiInc.get(i).getRight()) - delta);
			if (UltimiInc.get(i).getRight() <= 0 || UltimiInc.get(i).getLeft().life <= 0)
					toRemove.add(UltimiInc.get(i));
		}
		UltimiInc.removeAll(toRemove, true);
		toRemove.clear();	*/	
				
				
		if (Obiettivo == null)
			cheDevoFa();
		else if (Obiettivo.life <= 0)
			cheDevoFa();
		else
			move(delta);
		
		if (position.dst(Obiettivo.position) < ACTION_DST)
        	cheStamoAFa();
		
		hunger = Math.min(hunger+delta*HUNGER_PER_SECOND, 1);
		life -= delta + delta * hunger;
		wait_for_next_child -= delta;
	}
	
    public void cheStamoAFa() {
    	if (Obiettivo instanceof Palma) {
    		Obiettivo.life = -1;
    		hunger = 0;
    	}
    	
    	else if (Obiettivo instanceof Omino) {
    		if (tribu.equals(((Omino) Obiettivo).tribu)) {
    			if (hunger < 0.9f && wait_for_next_child <= 0) {
    				procreate();
    			}
    			else if (scontroProbabilistico(sociality, hunger) && wait_for_next_child <= 0){
    				procreate();
    			}
    			else {
    				
    				if (scontroProbabilistico(strength, ((Omino) Obiettivo).strength)) {
	    				Obiettivo.life = -1;
	    				hunger = 0;
    				}
    				else {
    					life = -1;
    					((Omino) Obiettivo).hunger = 0;
    				}
    			}
    		}
    		else {
    			if (scontroProbabilistico(((Omino) Obiettivo).strength, strength)) {
    				life = -1;
    				((Omino) Obiettivo).hunger = 0;
    			}
    			else {
    				((Omino) Obiettivo).life = -1;
    				hunger = 0;
    			}
    			
    		}
    	}
    	
    	Obiettivo = null;
    	
    }
    
    private void procreate() {
    	Mappone.getInstance().spawnaBimbo(this, (Omino) Obiettivo);
		//UltimiInc.add(new Tuple<Entity, Float>(Obiettivo,BLOCCO_INT));
		//((Omino) Obiettivo).UltimiInc.add(new Tuple<Entity, Float>(this,BLOCCO_INT));
		hunger += .1f;
		wait_for_next_child = PREGNANCY;
    }
    
    /**
     * returns true if p1 is chosen else false
     * @param p1
     * @param p2
     * @return
     */
    private boolean scontroProbabilistico(float p1, float p2) {
    	return ((Pasquisland) Gdx.app.getApplicationListener()).getRandom().nextFloat() < p1 / (p1 + p2);
    }
}
