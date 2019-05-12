package sgs.map;

import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import sgs.entities.Entity;

/**
 * 
 * Gestisce le entità e la mappa
 * insomma è il big changus di pasquisland
 *
 */
public class Mappone {
	
	private WorldMap map; // mappa con le grafiche e ti dice se è acqua o terra il terreno
	
	private HashMap<Vector2, Array<? extends Entity>> mappa_entita; // mappa 2d delle entita
	
	private Array<? extends Entity> da_aggiornare;
	private Array<? extends Entity> crepate;
	private Array<? extends Entity> girini;

	public Mappone(int map_width, int map_height) {
		float[] terrain_values = new float[3];
		terrain_values[0] = .3f;
		terrain_values[1] = .4f;
		terrain_values[2] = .7f;
		map = new WorldMap(map_width, map_height, (int) System.currentTimeMillis(),
				200f, 4, .3f, 2f, Vector2.Zero, terrain_values);
		
		mappa_entita = new HashMap<Vector2, Array<? extends Entity>>();
		
		da_aggiornare = new Array<Entity>();
		crepate = new Array<Entity>();
		girini = new Array<Entity>();
	}
	
	public void aggiorna(float delta) {
		
	}
	
	public void disegnaTutto(SpriteBatch batch, ShapeRenderer sr, int[] che_se_vede) {
		disegnaMappetta(sr, che_se_vede);
		disegnaEntita(batch);
	}
	
	public void disegnaMappetta(ShapeRenderer sr, int[] che_se_vede) {
		map.render(sr, che_se_vede[0], che_se_vede[1], che_se_vede[2], che_se_vede[3]);
	}
	
	public void disegnaEntita(SpriteBatch batch) {
		for (Entity e : da_aggiornare)
			e.disegnami(batch);
	}
	
	public Array<? extends Entity> chiCeStaQua(Vector2 qua) {
		Array<? extends Entity> ecco_la_lista = null;
		try {
			ecco_la_lista = mappa_entita.get(qua);
		}
		catch(Exception ex) {
			ecco_la_lista = new Array<Entity>();
			mappa_entita.put(qua, ecco_la_lista);
		}
		
		return ecco_la_lista;
	}

}
