package sgs.pasquisland;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;

import sgs.map.WorldMap;
import sgs.ui.MainUI;

/**
 * Ciao belli,
 * questa è la classe che gestisce tutte le cose , è la classe centrale che chiama
 * e crea le altre classi!
 *
 */
public class Pasquisland extends ApplicationAdapter {
	
	OrthographicCamera camera;
	CameraMover cam_mov;
	FillViewport vp;
	
	MainUI ui;
	Skin skin;
	
	WorldMap map;
	
	SpriteBatch batch;
	ShapeRenderer rend;
	
	@Override
	public void create () {
		int map_width = 128;
		int map_height = 128;
		
		camera = new OrthographicCamera();
		cam_mov = new CameraMover(camera, map_width, map_height);
		vp = new FillViewport(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight(), camera);
		Gdx.input.setInputProcessor(cam_mov);
		
		skin = new Skin(Gdx.files.internal("skins/metal/metal-ui.json"));
		ui = new MainUI(skin, new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), new SpriteBatch());
		
		batch = new SpriteBatch();
		rend = new ShapeRenderer();
		
		float[] terrain_values = new float[3];
		terrain_values[0] = .3f;
		terrain_values[1] = .4f;
		terrain_values[2] = .7f;
		map = new WorldMap(map_width, map_height, (int) System.currentTimeMillis(),
				200f, 4, .3f, 2f, Vector2.Zero, terrain_values);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(.8f, .8f, .8f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight());
		vp.apply();
		cam_mov.update();
		rend.setProjectionMatrix(camera.combined);
		int map_sight[] = cam_mov.computeMapSight();
		map.render(rend, map_sight[0], map_sight[1], map_sight[2], map_sight[3]);
		camera.update();
		
		ui.getViewport().apply();
		ui.act();
		ui.draw();
	}
	
	@Override
	public void resize(int width, int height) {
		ui.getViewport().update(width, height);
		ui.getViewport().getCamera().update();
		
		ui.setGameScreenViewport(vp);
		camera.update();
		
	}
	
	@Override
	public void dispose () {
		
	}
}
