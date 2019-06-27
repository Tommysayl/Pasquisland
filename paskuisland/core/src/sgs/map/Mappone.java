package sgs.map;

import java.util.HashMap;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import sgs.entities.Entity;
import sgs.entities.Omino;
import sgs.entities.Palma;
import sgs.entities.posRandom;
import sgs.pasquisland.Pasquisland;


public class Mappone {

	private static Mappone singleton;
	

	
	private WorldMap map; // mappa con le grafiche e ti dice se � acqua o terra il terreno
	private HashMap<GridPoint2, Array<Entity>> mappa_entita; // mappa 2d delle entita
	private Array<Entity> da_aggiornare;
	private Array<Entity> crepate;
	private Array<Entity> girini;
	private Array<Entity> nellaGriglia;
	private int selected = 0;

	public Mappone(int map_width, int map_height) {
		if (singleton == null) singleton = this;
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
		for (Entity e : da_aggiornare) {
			e.update(delta);
			if (e.life <= 0)
				crepate.add(e);
		}
		da_aggiornare.removeAll(crepate, true);
		for (Entity e : crepate)
			chiCeStaQua(e.gridposition).removeValue(e, true);
		crepate.clear();
	}

	public void disegnaTutto(SpriteBatch batch, ShapeRenderer sr, int[] che_se_vede) {
		disegnaMappetta(sr, che_se_vede);
		sr.begin(ShapeType.Filled);
		for (Entity entita : nellaGriglia) {
			sr.setColor(Color.RED);
			for (Entity e :vedi((Omino) entita, Omino.RAGGIO_VISIVO)) {
				sr.rect(e.position.x, e.position.y, 30, 30); //perch� ogni quadrato � 32x32 => per non avere rettangoli in caso di entit� vicine considero un'area minore :)	
			}
		}
		for (Entity entita : nellaGriglia) {
			sr.setColor(Color.CHARTREUSE);
			sr.rect(entita.position.x, entita.position.y, 30, 30); //perch� ogni quadrato � 32x32 => per non avere rettangoli in caso di entit� vicine considero un'area minore :)
		}
		sr.end();
		batch.begin();
		batch.enableBlending();
		disegnaEntita(batch);
		batch.disableBlending();
		batch.end();
		for( Entity e: nellaGriglia) {
			if(e.life<0) {
				nellaGriglia.removeValue(e, true);
			}
		}
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

	public void spammaPalme(float densita){
		Random r = ((Pasquisland) Gdx.app.getApplicationListener()).getRandom();
		for(int y=0; y<map.getHeight(); y++) {
			for(int x=0; x< map.getWidth(); x++) {
				if(map.getTerrainTypeAt(x, y)==map.land_id) {
					if(r.nextFloat()<densita) {
						Palma cespuglio= new Palma(x*map.tile_size,y*map.tile_size);
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
	

	public posRandom posizioneIntorno(GridPoint2 posizione) {
		posRandom newpos = new posRandom(posizione.x*map.tile_size,posizione.y*map.tile_size);
		newpos.gridposition= posizione.cpy(); 
		Random r = ((Pasquisland) Gdx.app.getApplicationListener()).getRandom();
		int r1= r.nextInt(4)-1;
		int r2= r.nextInt(4)-1;
		if (posizione.x+r1< map.getWidth() && posizione.x+r1>0 && posizione.y+r2<map.getHeight() && posizione.y+r2>0) {
			if(map.getTerrainTypeAt(posizione.x+r1, posizione.y+r2)!= WorldMap.water_id) {
					newpos.gridposition.x= posizione.x+r1;
					newpos.gridposition.y= posizione.y+r2;
					newpos.position.x= (posizione.x+r1)*map.tile_size;
					newpos.position.y= (posizione.y+r2)*map.tile_size;
			}
		}
		return newpos;
		
		
	}
	public Array<Entity> vedi(Entity omino,int raggio, boolean add_pos){
		Array<Entity> RaggioVisivo= new Array<Entity>();
		for( int y=omino.gridposition.y-raggio; y<= omino.gridposition.y+raggio; y++) {
			if(y< map.getHeight() && y>=0) {
				for( int x= omino.gridposition.x-raggio; x<=omino.gridposition.x+raggio; x++) {
					if (x< map.getWidth() && x>=0) {
						if( y==omino.gridposition.y && x== omino.gridposition.x) {
							for(Entity entita: chiCeStaQua(x,y)) {
								if(entita != omino) {
									RaggioVisivo.add(entita);
							}
						}
					}
						else if(map.getTerrainTypeAt(x, y)!= WorldMap.water_id) {
							for(Entity entita: chiCeStaQua(x,y)) {
								RaggioVisivo.add(entita);
							}
						}
					}
				}
			}
		}
		if(add_pos==true) {
		RaggioVisivo.add(posizioneIntorno(omino.gridposition));
		}
		return RaggioVisivo;
	}

	public Array<Entity> vedi(Entity omino, int raggio){
		return vedi(omino,raggio, true);
		
	}
	public static Mappone getInstance() {
		return  singleton;
	}
	
	public void spawnaBimbo(Omino genitore1, Omino genitore2) {
		Random r = ((Pasquisland) Gdx.app.getApplicationListener()).getRandom();
		int r1= r.nextInt(2);
		if(r1==0) {
			posRandom newpos= posizioneIntorno(genitore1.gridposition);
			Omino bimbo= new Omino(newpos.gridposition.x*map.tile_size,newpos.gridposition.y*map.tile_size);
			da_aggiornare.add(bimbo);
			chiCeStaQua(newpos.gridposition.x, newpos.gridposition.y).add(bimbo);
			assegnaNuoviValoriAlBimbo(genitore1, genitore2, bimbo);
		}
		else if(r1==1){
			posRandom newpos= posizioneIntorno(genitore2.gridposition);
			Omino bimbo= new Omino(newpos.gridposition.x*map.tile_size,newpos.gridposition.y*map.tile_size);
			da_aggiornare.add(bimbo);
			chiCeStaQua(newpos.gridposition.x, newpos.gridposition.y).add(bimbo);
			assegnaNuoviValoriAlBimbo(genitore1, genitore2, bimbo);
		}
	}
	

	private void assegnaNuoviValoriAlBimbo(Omino genitore1, Omino genitore2, Omino bimbo) {
		Random r = ((Pasquisland) Gdx.app.getApplicationListener()).getRandom();
		bimbo.setValues(
				MathUtils.clamp(genitore1.strength + genitore2.strength * r.nextFloat() / 10 * (r.nextInt(3) - 1), Omino.MIN_STRENGTH, Omino.MAX_STRENGTH),
				MathUtils.clamp(genitore1.sociality + genitore2.sociality * r.nextFloat() / 10 * (r.nextInt(3) - 1), Omino.MIN_SOCIALITY, Omino.MAX_SOCIALITY),
				MathUtils.clamp(genitore1.speed + genitore2.speed * r.nextFloat() / 10 * (r.nextInt(3) - 1), Omino.MIN_SPEED, Omino.MAX_SPEED));
	}

	public void vediRect(Rectangle rettangolo) {
		nellaGriglia.clear();
		int xgs= (int) (rettangolo.x/map.tile_size);//vertice basso sx griglia
		int ygs= (int) (rettangolo.y/map.tile_size);//vertice basso sx griglia
		float yd= rettangolo.y+ rettangolo.height;// vertice alto dx pixel
		float xd= rettangolo.x+rettangolo.width;//vertice alto dx pixel
		int ygd= (int) (yd/map.tile_size);//vertice alto dx griglia
		int xgd= (int) (xd/map.tile_size);//vertice alto dx griglia
		for(int y=Math.min(ygs, ygd); y<= Math.max(ygs,ygd); y++) {
			if(rettangolo.y<=map.getHeight()*map.tile_size && y>=0)
				for(int x= Math.min(xgs, xgd); x<=Math.max(xgs, xgd); x++) {
					if(rettangolo.x<=map.getWidth()*map.tile_size && x>=0) 
					for( Entity entita: chiCeStaQua(x,y)) {
						if(entita instanceof Omino) {
							nellaGriglia.add(entita);
							}
						}
					}
				}
			}	

	public void spawnaPalmaQuaVicino(GridPoint2 posizione) {
	posRandom newpos= posizioneIntorno(posizione);
	if(map.getTerrainTypeAt(newpos.gridposition.x, newpos.gridposition.y)== map.land_id) {
		if(!presente(newpos.gridposition.x,newpos.gridposition.y,Palma.class)) {
			Palma palmetta= new Palma(newpos.gridposition.x*map.tile_size,newpos.gridposition.y*map.tile_size);
			da_aggiornare.add(palmetta);
			chiCeStaQua(newpos.gridposition.x, newpos.gridposition.y).add(palmetta);
			return;
			}
		}
	}
	
	private <T extends Entity> boolean presente(int x,int y, Class<T> cls){
		for(Entity e: chiCeStaQua(x,y)) {
			if(cls.isInstance(e)) 
				return true;
		}
		return false;
	}
	


}
