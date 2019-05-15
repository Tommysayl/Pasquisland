package sgs.map;

import java.util.HashMap;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import sgs.entities.Entity;
import sgs.entities.Omino;
import sgs.pasquisland.Pasquisland;

/**
 * 
 * Gestisce le entità e la mappa
 * insomma è il big changus di pasquisland
 *
 */
public class Mappone {
	
	private WorldMap map; // mappa con le grafiche e ti dice se è acqua o terra il terreno
	
	private HashMap<GridPoint2, Array<Entity>> mappa_entita; // mappa 2d delle entita
	
	private Array<Entity> da_aggiornare;
	private Array<Entity> crepate;
	private Array<Entity> girini;

	public Mappone(int map_width, int map_height) {
		float[] terrain_values = new float[3];
		terrain_values[0] = .2f;
		terrain_values[1] = .3f;
		terrain_values[2] = .9f;
		map = new WorldMap(map_width, map_height, ((Pasquisland) Gdx.app.getApplicationListener()).getRandom().nextInt(1000000),
				200f, 4, .3f, 2f, Vector2.Zero, terrain_values, true, .1f);
		
		mappa_entita = new HashMap<GridPoint2, Array<Entity>>();
		
		da_aggiornare = new Array<Entity>();
		crepate = new Array<Entity>();
		girini = new Array<Entity>();
	}
	
	public void aggiorna(float delta) {

	}
	
	public void disegnaTutto(SpriteBatch batch, ShapeRenderer sr, int[] che_se_vede) {
		disegnaMappetta(sr, che_se_vede);
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
	
}
