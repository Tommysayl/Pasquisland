package sgs.map;

import java.util.HashMap;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import sgs.entities.Cespuglio;
import sgs.entities.Entity;
import sgs.entities.Omino;
import sgs.entities.posRandom;
import sgs.pasquisland.Pasquisland;

/**
 * 
 * Gestisce le entit√† e la mappa
 * insomma √® il big changus di pasquisland
 *
 */
public class Mappone {
	
	private WorldMap map; // mappa con le grafiche e ti dice se √® acqua o terra il terreno
	
	private HashMap<GridPoint2, Array<Entity>> mappa_entita; // mappa 2d delle entita
	
	private Array<Entity> da_aggiornare;
	private Array<Entity> crepate;
	private Array<Entity> girini;
	private Array<Entity> nellaGriglia;

	private int selected = 0;

	public Mappone(int map_width, int map_height) {
		float[] terrain_values = new float[3];
		terrain_values[0] = .2f;
		terrain_values[1] = .3f;
		terrain_values[2] = .9f;
		map = new WorldMap(map_width, map_height, ((Pasquisland) Gdx.app.getApplicationListener()).getRandom().nextInt(1000000),
				200f, 4, .3f, 2f, Vector2.Zero, terrain_values, true, .1f);
		
		mappa_entita = new HashMap<GridPoint2, Array<Entity>>();
		nellaGriglia= new Array<Entity>();
		da_aggiornare = new Array<Entity>();
		crepate = new Array<Entity>();
		girini = new Array<Entity>();
	}
	
	public void aggiorna(float delta) {

	}
	
	public void disegnaTutto(SpriteBatch batch, ShapeRenderer sr, int[] che_se_vede) {
		disegnaMappetta(sr, che_se_vede);
		sr.begin(ShapeType.Filled);
		for (Entity entita : nellaGriglia) {
			sr.setColor(Color.RED);
			for (Entity e :vedi((Omino) entita)) {
				sr.rect(e.position.x, e.position.y, 30, 30); //perchË ogni quadrato Ë 32x32 => per non avere rettangoli in caso di entit‡ vicine considero un'area minore :)	
			}
		}
		for (Entity entita : nellaGriglia) {
			sr.setColor(Color.CHARTREUSE);
			sr.rect(entita.position.x, entita.position.y, 30, 30); //perchË ogni quadrato Ë 32x32 => per non avere rettangoli in caso di entit‡ vicine considero un'area minore :)
		}
		sr.end();
		batch.begin();
		disegnaEntita(batch);
		batch.end();
	}
	
	public void disegnaMappetta(ShapeRenderer sr, int[] che_se_vede) {
		map.render(sr, che_se_vede[0], che_se_vede[1], che_se_vede[2], che_se_vede[3]);
	}
	
	public void disegnaEntita(SpriteBatch batch) {
		for (Entity e : da_aggiornare)
			e.disegnami(batch);
	}
	
	public Array<Entity> chiCeStaQua(GridPoint2 qua) {
		Array<Entity> ecco_la_lista = mappa_entita.get(qua);
		if (ecco_la_lista == null) {
			ecco_la_lista = new Array<Entity>();
			mappa_entita.put(qua, ecco_la_lista);
		}
		
		return ecco_la_lista;
	}
	
	public Array<Entity> chiCeStaQua(int x, int y) {
		GridPoint2 p = new GridPoint2(x,y);
		return chiCeStaQua(p);
	}
	
	public WorldMap getMap() {
		return map;
	}
	
	public void spammaOmini(float densita) {
		Random r = ((Pasquisland) Gdx.app.getApplicationListener()).getRandom();
		for(int y=0; y<map.getHeight(); y++) {
			for(int x=0; x< map.getWidth(); x++) {
				if(map.getTerrainTypeAt(x, y)!=map.water_id) {
					if(r.nextFloat()<densita) {
						Omino primoUomo= new Omino(x*map.tile_size,y*map.tile_size);
						da_aggiornare.add(primoUomo);
						chiCeStaQua(x,y).add(primoUomo); 
					}	
				}
			}
		}
	}
	
	public void spammaCespugli(float densita){
		Random r = ((Pasquisland) Gdx.app.getApplicationListener()).getRandom();
		for(int y=0; y<map.getHeight(); y++) {
			for(int x=0; x< map.getWidth(); x++) {
				if(map.getTerrainTypeAt(x, y)==map.land_id) {
					if(r.nextFloat()<densita) {
						Cespuglio cespuglio= new Cespuglio(x*map.tile_size,y*map.tile_size);
						da_aggiornare.add(cespuglio);
						chiCeStaQua(x,y).add(cespuglio);
						}
				}
			}
		}
	}
	
	public void ammazzaOmini() {
		for(int y=0; y<map.getHeight(); y++) {
			for(int x=0; x< map.getWidth(); x++) {
				if(map.getTerrainTypeAt(x, y)!=map.water_id) {
					chiCeStaQua(x, y).clear();
				}
			}
		}
		da_aggiornare.clear();
	}
	

	public int getPopulationCount() {return da_aggiornare.size;}
	
	public Array<Entity> vedi(Omino omino){
		int raggio = 3;
		Array<Entity> RaggioVisivo= new Array<Entity>();
		for( int y=omino.gridposition.y-raggio; y<= omino.gridposition.y+raggio; y++) {
			if(y<= map.getHeight() && y>=0) {
				for( int x= omino.gridposition.x-raggio; x<=omino.gridposition.x+raggio; x++) {
					if (x<= map.getWidth() && x>=0) {
						if( y==omino.gridposition.y && x== omino.gridposition.x) {
							for(Entity entita: chiCeStaQua(x,y)) {
								if(entita != omino) {
									RaggioVisivo.add(entita);
								}
							}
						}
						if(map.getTerrainTypeAt(x, y)!= WorldMap.water_id) {
							for(Entity entita: chiCeStaQua(x,y)) {
								RaggioVisivo.add(entita);
							}
						}
					}
				}

			}
		}
		posRandom newpos = new posRandom(omino.position.x, omino.position.y);
		newpos.gridposition= omino.gridposition.cpy(); 
		Random r = ((Pasquisland) Gdx.app.getApplicationListener()).getRandom();
		int r1= r.nextInt(3)-1;
		int r2= r.nextInt(3)-1;
		if (omino.gridposition.x+r1<= map.getWidth() && omino.gridposition.x+r1>=0 && omino.gridposition.y+r2<=map.getHeight() && omino.gridposition.y+r2>=0) {
			if(map.getTerrainTypeAt(omino.gridposition.x+r1, omino.gridposition.y+r2)!= WorldMap.water_id) {
			newpos.gridposition.x= omino.gridposition.x+r1;
			newpos.gridposition.y= omino.gridposition.y+r2;
			newpos.position.x= omino.position.x+r1*WorldMap.tile_size;
			newpos.position.y= omino.position.y+r2*WorldMap.tile_size;
			}
		}
		RaggioVisivo.add(newpos);
		return RaggioVisivo;
	}
	
	
	
	public void spawnaBimbo(Omino genitore1, Omino genitore2) {
		int x= (genitore1.gridposition.x + genitore2.gridposition.x)/2;
		int y= (genitore1.gridposition.y + genitore2.gridposition.y)/2;
		if(map.getTerrainTypeAt(x, y)!= WorldMap.water_id) {
			Omino bimbo= new Omino(x*map.tile_size,y*map.tile_size);
			da_aggiornare.add(bimbo);
			chiCeStaQua(x,y).add(bimbo); 
			/*Mancano i valori di forza socievolezza e velocit‡
			 * non sappiamo se il controllo nell'acqua sia utile o no
			 */
		}
		else {
			Random r = ((Pasquisland) Gdx.app.getApplicationListener()).getRandom();
			while(map.getTerrainTypeAt(x, y) != WorldMap.water_id) {
				int r1= r.nextInt(3)-1;
				int r2= r.nextInt(3)-1;
				x+=r1;
				y+=r2;
			}
			Omino bimbo= new Omino(x*map.tile_size,y*map.tile_size);
			da_aggiornare.add(bimbo);
			chiCeStaQua(x, y).add(bimbo);
		}
	}
	
	public void vediRect(Rectangle rettangolo) {
		nellaGriglia.clear();
		int xgs= (int) (rettangolo.x/map.tile_size);//vertice basso sx griglia
		int ygs= (int) (rettangolo.y/map.tile_size);//vertice basso sx griglia
		float yd= rettangolo.y- rettangolo.height;// vertice alto dx pixel
		float xd= rettangolo.x+rettangolo.width;//vertice alto dx pixel
		int ygd= (int) (yd/map.tile_size);//vertice alto dx griglia
		int xgd= (int) (xd/map.tile_size);//vertice alto dx griglia
		for(int y=ygd; y<= ygs; y++) {
			if(rettangolo.y<=map.getHeight()*map.tile_size && y>=0) {
			for(int x= xgs; x<=xgd; x++) {
				if(rettangolo.x<=map.getWidth()*map.tile_size && x>=0) {
				for( Entity entita: chiCeStaQua(x,y)) {
					if(entita instanceof Omino) {
						nellaGriglia.add(entita);
						}
					}
				}
			}	
		}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}



