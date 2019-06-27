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
	
	private static Random random;
	
	public static final int RAGGIO_VISIVO = 4;
	public static final float ACTION_DST = 20;
	public static final float BLOCCO_INT = 2f;
	//public static final float PREGNANCY = .1f;
	public static final float HUNGER_PER_SECOND = .2f; // la fame arriva a 1 in 5 sec
	public static final float MATURITY = 17;
	
	/* BOUNDARIES FOR STATS */
	public static final float MIN_STRENGTH = 0, MAX_STRENGTH = 1;
	public static final float MIN_SOCIALITY = 0, MAX_SOCIALITY = 1;
	public static final float MIN_SPEED = 32, MAX_SPEED = 96;
	
	public static Texture texture = new Texture(Gdx.files.internal("spy.png"));
	  
	  public float strength; 
	  public float speed;
	  public float hunger; 
	  public float sociality;
	  //public float wait_for_next_child;
	  public Entity Obiettivo;
	  public String tribu;
	  
	  Array<Tuple<Entity,Float>> UltimiInc;
	  Array<Tuple<Entity,Float>> toRemove;
	  
	  public Omino(float x, float y) {
		super(x,y);
		random = ((Pasquisland) Gdx.app.getApplicationListener()).getRandom();
		setValues(random.nextFloat(),random.nextFloat(),random.nextInt((int) (MAX_SPEED - MIN_SPEED))+MIN_SPEED);
		
		this.hunger = 0f; 	
		life = 15;
		UltimiInc = new Array<Tuple<Entity,Float>>();
		toRemove = new Array<Tuple<Entity,Float>>();
		//wait_for_next_child = 0;
		}
	  
	  public void setValues(float strength, float sociality, float speed) {
		  this.strength =  strength;
		  this.sociality =  sociality;
		  this.speed =  speed;
		  tribu = dammiTribu();
		  life = 20;
	  }
	  
	
	private String dammiTribu() {
		char[] tribu =new char[3];
		float[] stats_and_bounds = {
				strength, MIN_STRENGTH, MAX_STRENGTH,
				sociality,MIN_SOCIALITY, MAX_SOCIALITY,
				speed, MIN_SPEED, MAX_SPEED};
		for (int i = 0; i < stats_and_bounds.length; i+=3) {
			float lower_bound = stats_and_bounds[i + 1];
			float upper_bound = stats_and_bounds[i + 2];
			
			if (stats_and_bounds[i] < lower_bound + (upper_bound - lower_bound) / 3)
				tribu[i/3] = 'c';
			else if (stats_and_bounds[i] < lower_bound + 2 * (upper_bound - lower_bound) / 3)
				tribu[i/3] = 'b';
			else 
				tribu[i/3] = 'a';
		}
		return String.copyValueOf(tribu); 
	}
	
	private void setTribuColor(SpriteBatch batch) {
		float[] rgb = new float[3];
		int curr_col = 0;
		for (char c : tribu.toCharArray()) {
			switch (c) {
			case 'a':
				rgb[curr_col] = 1;
				break;
			case 'b':
				rgb[curr_col] = 0.5f;
				break;
			case 'c':
				rgb[curr_col] = 0;
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
		for(int i=0;i<UltimiInc.size;i++){
			Dintorni.removeValue(UltimiInc.get(i).getLeft(), true);
		}
		
		float [] Score = new float[Dintorni.size]; //lista di score 
		for (int i = 0; i < Dintorni.size; i++) {//per il scorrere el in una lista scrivo for tipo di el della lista, nome che voglio dare agli el, :, nome della lista
			float dst = gridposition.dst2(Dintorni.get(i).gridposition) / (2*RAGGIO_VISIVO * RAGGIO_VISIVO);
			
			if (Dintorni.get(i) instanceof Palma) {
				Score[i] = this.hunger / (dst + 1);
			    //Gdx.app.log("Score Palma", ""+Score[i]);
			}
			else if (Dintorni.get(i) instanceof Omino) {
				if (tribu.equals(((Omino) Dintorni.get(i)).tribu)) {
			      if (life >MATURITY || Dintorni.get(i).life > MATURITY)
			    	  Score[i] = 0;
			      else {
					Score[i] = (this.sociality);
			      }}
			    else  {//perche altrimenti selezionerei anche le posizioni vuote
			    	Score[i] = (this.strength * this.hunger);
					Score[i] = (this.sociality * (1 - hunger)) / (dst + 1);
			    }
			      
			      //Gdx.app.log("Score Amico", ""+Score[i]);
				}
			   else  {//perche altrimenti selezionerei anche le posizioni vuote
			    	Score[i] = (this.strength * this.hunger) / (dst + 1);
				    //Gdx.app.log("Score Nemico", ""+Score[i]);
			    }
			
			
			
		}
		/*float max = Score[0];
		int j = 0;
	    for (int i = 0; i < Score.length;i++) {
		    if (Score[i] >= max ) {
		    	max = Score[i];
		    	j = i; //j � l'indice del massimo
		    }  
	    }
	    
		Obiettivo = Dintorni.get(j);*/
		/*for (int i = 0; i < Score.length; i++)
			Gdx.app.log("Score sysrtem",i+" : "+ Dintorni.get(i)+" Score = "+Score[i]);*/
		int choice = sceltaProbabilisticaFiltrata(Dintorni,Score);
		//Gdx.app.log("Score sysrtem", "I choose "+choice+" it is a "+ Dintorni.get(choice));
		Obiettivo = Dintorni.get(choice);
	}
	
	private int sceltaProbabilistica(float[] probs) {
		float sum = 0;
		for (float p : probs) {
			sum += p;
		}
		
		float choice = random.nextFloat();
		for (int i = 0; i < probs.length; i++) {
			choice -= probs[i] / sum;
			if (choice <= 0)
				return i;
		}
		return probs.length - 1;
	}
	
	private int sceltaProbabilisticaFiltrata(Array<Entity> ents, float[] probs) {
		float[] best_per_type = new float[4];
		int[] map_index = new int[4];
		for (int i = 0; i < ents.size; i++)
			if (ents.get(i) instanceof posRandom) {
				best_per_type[3] = Math.max(best_per_type[3], probs[i]);
				map_index[3] = best_per_type[3] == probs[i]? i : map_index[3];
			}
			else if (ents.get(i) instanceof Palma){
				best_per_type[2] = Math.max(best_per_type[2], probs[i]);
				map_index[2] = best_per_type[2] == probs[i]? i : map_index[2];
			}
			else if (ents.get(i) instanceof Omino){
				if (tribu.equals(((Omino) ents.get(i)).tribu)) {
					best_per_type[0] = Math.max(best_per_type[0], probs[i]);
					map_index[0] = best_per_type[0] == probs[i]? i : map_index[0];
				}
				else {
					best_per_type[1] = Math.max(best_per_type[1], probs[i]);
					map_index[1] = best_per_type[1] == probs[i]? i : map_index[1];
				}
			}
	
		//for (int i = 0; i < best_per_type.length; i++)
		//	Gdx.app.log("score", ents.get(map_index[i])+" "+best_per_type[i]);
		int choice = sceltaProbabilistica(best_per_type);
		//Gdx.app.log("choose", "i choose "+ents.get(map_index[choice]));
		return map_index[choice];
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
				
				
		if (Obiettivo == null)
			cheDevoFa();
		else if (Obiettivo.life <= 0)
			cheDevoFa();
		else
			move(delta);
		
		if (position.dst(Obiettivo.position) < ACTION_DST)
        	cheStamoAFa();
		
		hunger = Math.min(hunger+delta*HUNGER_PER_SECOND, 1);
		life -= /*delta +*/ delta * hunger;
		//wait_for_next_child -= delta;
	}
	
    public void cheStamoAFa() {
    	if (Obiettivo instanceof Palma) {
    		Obiettivo.life = -1;
    		hunger = 0;
    	}
    	
    	else if (Obiettivo instanceof Omino) {
    		if (tribu.equals(((Omino) Obiettivo).tribu)) {
    			if (sociality > hunger ) {
    				Mappone.getInstance().spawnaBimbo(this, (Omino) Obiettivo);
    				UltimiInc.add(new Tuple<Entity, Float>(Obiettivo,BLOCCO_INT));
    			if (hunger < 0.9f /*&& wait_for_next_child <= 0*/) {
    				procreate();
    			}
    			else if (scontroProbabilistico(sociality, hunger/2) /*&& wait_for_next_child <= 0*/){
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
    			
    			
    		 {	
    				if (((Omino) Obiettivo).strength > strength ) {
    				life = -1;
    				((Omino) Obiettivo).hunger = 0;
    			if (scontroProbabilistico(strength, sociality * (1 - hunger))) {
	    			if (scontroProbabilistico(((Omino) Obiettivo).strength, strength)) {
	    				life = -1;
	    				((Omino) Obiettivo).hunger = 0;
	    			}
	    			else {
	    				((Omino) Obiettivo).life = -1;
	    				hunger = 0;
	    			}
    			}
    			else {
    				procreate();
    			}
    			
    		}
    	}
    	
    	Obiettivo = null;
    	
    }
    }
    	}}

    
    private void procreate() {
    	Mappone.getInstance().spawnaBimbo(this, (Omino) Obiettivo);
		UltimiInc.add(new Tuple<Entity, Float>(Obiettivo,BLOCCO_INT));
		((Omino) Obiettivo).UltimiInc.add(new Tuple<Entity, Float>(this,BLOCCO_INT));
		hunger += .1f;
		//wait_for_next_child = PREGNANCY;
		//((Omino)Obiettivo).wait_for_next_child = PREGNANCY;
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
