package sgs.map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;


public class WorldMap {
	
	//public static vars
	public static final int BORDER_FADE_OUT_DST = 0;
	public static final int mini_map_pixel_size = 256;
	public static final int small_map_size = 128;
	public static final int medium_map_size = 256;
	public static final int big_map_size = 512;
	public static final int tile_size = 32;
	
	public static final int water_id = 0;
	public static final int shallow_water_id = 1;
	public static final int sand_id = 2;
	public static final int land_id = 3;

	//***** GENERAL 
	float[][] map;
	
	int mapWidth;
	int mapHeight;
	Pixmap map_pixmap; // the size of the pixmap 
	Texture map_texture; // and of the texture/drawable
	TextureRegionDrawable map_drawable; // are always 256*256
	float water_p;
	float sand_p;
	
	//***** COLORS AND RENDERING
	Color water_col = new Color(55f/255f,155f/255f,255f/255f,1);
	Color shallow_water_col = new Color(105f/255f,205f/255f,255f/255f,1);
	Color sand_col = new Color(1,228/255f,181/255f,1);
	Color land_col = new Color(105f/255f,179f/255f,38f/255f,1);
	
	//***** MAP SPECIFICS
	int seed;
	int octaves;
	float scale;
	float persistence;
	float lacunarity;
	float gaussian_smooth;
	Vector2 offset;
	
	//***** INTERNAL
	boolean color_changed;
	boolean settings_changed;
	boolean apply_gaussian;
	
	
	public WorldMap(int width, int height, int s,float scal, int oct, float pers, float lac, Vector2 offset, float[] terrain_p, boolean gaussian, float gauss_val) {
		resetMapGenSettings(width, height, s, scal, oct, pers, lac, offset, gaussian, gauss_val);
		resetMapSettings(terrain_p);
		map = new float[height][width];
		map_pixmap = new Pixmap(mini_map_pixel_size, mini_map_pixel_size, Format.RGB888);
		generateMap();
	}

	public void resetMapGenSettings(int width, int height, int s, float scal, int oct, float pers,float lac, Vector2 offset, boolean gaussian, float gauss_val) {
		this.mapWidth = width;
		this.mapHeight = height;
		this.seed = s;
		this.scale = scal;
		this.octaves = oct;
		this.persistence = pers;
		this.lacunarity = lac;
		this.offset = offset;
		this.apply_gaussian = gaussian;
		this.settings_changed = true;
		this.gaussian_smooth = gauss_val;
	}
	
	public void resetMapSettings(float water, float sand) {
		water_p = water;
		sand_p = sand;
		this.color_changed = true;
	}
	
	public void resetMapSettings(float[] perc) {
		resetMapSettings(perc[0], perc[1]);
		this.color_changed = true;
	}
	
	public void render(ShapeRenderer batch, int x, int y, int rend_width, int rend_height) {
		batch.begin(ShapeType.Filled);
		for (int i = y; i < y + rend_height; i++) {
			for (int j = x; j < x + rend_width; j++) {
				if (i > getHeight() -1 || i < 0 || j > getWidth() -1 || j < 0) {
					continue;
				}
				
				else if (i > getHeight() - BORDER_FADE_OUT_DST - 1|| i < BORDER_FADE_OUT_DST || 
						j > getWidth() - BORDER_FADE_OUT_DST - 1 || j < BORDER_FADE_OUT_DST) {
					Color col = getColorAt(j,i).cpy();
					float dstx = 0;
					float dsty = 0;
					if (i < BORDER_FADE_OUT_DST) dsty = BORDER_FADE_OUT_DST - i;
					else if (i > getHeight()-1-BORDER_FADE_OUT_DST) dsty = BORDER_FADE_OUT_DST-getHeight() + i;
					if (j < BORDER_FADE_OUT_DST) dstx = BORDER_FADE_OUT_DST - j;
					else if (j > getWidth()-1-BORDER_FADE_OUT_DST) dstx = BORDER_FADE_OUT_DST- getWidth() + j;
					
					col.lerp(Color.BLACK, (dstx+dsty)/BORDER_FADE_OUT_DST);
					batch.setColor(col);
				}
				else 
					batch.setColor(getColorAt(j,i));
				
				batch.rect(j * tile_size, i * tile_size, tile_size, tile_size);
			}
		}
		batch.end();
	}
	

	public TextureRegionDrawable getDrawable() {
		return map_drawable;
	}

	public void generateMap() {
		//Check if width or height has changed
		if (map.length != mapHeight || map[0].length != mapWidth) {
			map = new float[mapHeight][mapWidth];
			//map_pixmap.dispose();
			//map_texture.dispose();
			//map_pixmap = new Pixmap(256, 256, Format.RGB888);
		}
		//Generate the new map
		if (this.settings_changed) {
			MapGenerator.GenerateNoiseMap(map, mapWidth, mapHeight, seed, scale, octaves, persistence, lacunarity, offset);
			for (int x = 0; x < mapWidth; x++) {
				for (int y = 0; y < mapHeight; y++) {
					//apply gaussian
					if (apply_gaussian) {
						float xm, ym;
						if (x > mapWidth/2) xm = (mapWidth - x) / (float)(mapWidth/2);
						else xm = x / (float)(mapWidth/2);
						if (y > mapHeight/2) ym = (mapHeight - y) / (float)(mapHeight/2);
						else ym = y / (float)(mapHeight/2);
						
						map[y][x] *= (float) (Math.pow(Math.E, gaussian_smooth * -1f/(xm*xm)));
						map[y][x] *= (float) (Math.pow(Math.E, gaussian_smooth * -1f/(ym*ym)));
					}
				}
			}
		}
		
		recolorMap();
		
		map_texture = new Texture(map_pixmap);
		map_drawable = new TextureRegionDrawable(map_texture);
		
		this.settings_changed = false;
	}
	
	public void recolorMap() {
		/* recolors the pixmap which is always 256 * 256
		 * which is th minimum size for the map, this is because
		 * the pixmap is used only for minimap , so its useless to have 
		 * a 1000*1000 image */
		float step = ((float) mapWidth) / mini_map_pixel_size;
		int i = 0;
		for (float x = 0; x < mapWidth; x+=step, i++) {
			int j = 0;
			for (float y = 0; y < mapHeight; y+=step, j++) {
				map_pixmap.setColor(getColorAt((int)Math.floor(x), (int)Math.floor(y)));
				map_pixmap.drawPixel(i, mini_map_pixel_size - j - 1);
			}
		}
		this.color_changed = false;
	}
	
	private Color getColorAt(int x, int y) {
		//setup byte map
		if (map[y][x] < water_p*5/6)
			return water_col;
		else if (map[y][x] < water_p)
			return shallow_water_col;
		else if (map[y][x] < sand_p)
			return sand_col;
		else 
			return land_col;
	}
	
	public int getTerrainTypeAt(int x, int y) {
		if (map[y][x] < water_p*5/6)
			return water_id;
		else if (map[y][x] < water_p)
			return shallow_water_id;
		else if (map[y][x] < sand_p)
			return sand_id;
		else
			return land_id;
	}
	
	public float getTerrainHeightAt(int x, int y) {
		return map[y][x];
	}
	
	public int getWidth() {
		return mapWidth;
	}

	public int getHeight() {
		return mapHeight;
	}
	
	public int getSeed() {
		return seed;
	}
	
	public float getGaussianSmoothness() {
		return gaussian_smooth;
	}
	
}
