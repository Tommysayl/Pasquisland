package sgs.entities;


import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import sgs.map.Mappone;

public class Omino extends Entity {
	
	public static Texture texture = new Texture(Gdx.files.internal("spy.png"));
	  
	  public float strength; 
	  public float speed;
	  public float hunger; 
	  public float sociality;
	  public char[] tribu;
      
	  public Omino(float x, float y, float strength, float speed, float hunger, float sociality, char tribu[]) {
		super();
		super.position = new Vector2(x,y);   //assegno dei valori random alle caratteristiche
		float S = new Random().nextFloat();
		this.strength = S;
		float So = new Random().nextFloat();
		this.sociality = So;
		float Sp = new Random().nextFloat();
		this.speed = 2*So;
		tribu = dammiTribu();
		this.hunger = 0; 
		
		}
	
	private char[] dammiTribu() {
		
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
		
		
		return tribu; 
	}

	public void disegnami(SpriteBatch batch) {
		batch.draw(texture, position.x, position.y);
	}
	
	//metodo che prende in input un array con tutte le entità vicine all'omino e decide cosa fare => non returna niente
	public void cheDevoFa() { 
		
		float [] Score; //lista di score 
		Array<Entity> Dintorni = Mappone.getInstance().vedi(this); //ritorna la lista per CheDevoFa di tipi generici importando la funzione vedi da mappone
		for (int i = 0; i < Dintorni.length(); i++) {//per il scorrere el in una lista scrivo for tipo di el della lista, nome che voglio dare agli el, :, nome della lista
			if Dintorni.get(i) instanceof Palma  
				Score[i] = (this.hunger * this.hunger) / (this.position.dst2(Dintorni.get(i).position));
			else if Dintorni.get(i) instanceof Omino 
				if this.tribu == Dintorni.get(i).tribu
			       Score[i] = (this.sociality * this.sociality) / (this.position.dst2(Dintorni.get(i).position));
			    else if  this.tribu != Dintorni.get(i).tribu	//perche altrimenti selezionerei anche le posizioni vuote
			    		Score[i] = (this.strength * this.hunger) / (this.position.dst2(Dintorni.get(i).position));
		}
		int max = 0;
		int j = 0;
	    for (i = 0; i < Score.length;i++) {
		    if (score[i] > max ) {
		    	max = score[i];
		    	j = i; //j è l'indice del massimo
		    }  
	    }
		move(Dintorni.get(j));   
	}
	public void  move {
		
		
		
		
		
		
		
	}
	
	
    public void cheStamoAFa {
    	
    	
    }
    

}
 

 