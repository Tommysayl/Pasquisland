package sgs.pasquisland;

import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;

import sgs.map.Mappone;
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
	InputMultiplexer multi_input;
	
	SpriteBatch batch;
	ShapeRenderer rend;
	
	Mappone mappone;
	
	Random random;

	
	@Override
	public void create () {
		int map_width = 128;
		int map_height = 128;
		
		random = new Random(System.nanoTime());
		mappone = new Mappone(map_width, map_height);
		
		camera = new OrthographicCamera();
		cam_mov = new CameraMover(camera, map_width, map_height);
		vp = new FillViewport(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight(), camera);
		
		skin = new Skin(Gdx.files.internal("skins/metal/metal-ui.json"));
		ui = new MainUI(skin, new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), new SpriteBatch());
		ui.draw(); // QUESTO RISOLVE IL BUG DEL RESIZE
		
		batch = new SpriteBatch();
		rend = new ShapeRenderer();
	
		
		multi_input = new InputMultiplexer();
		multi_input.addProcessor(ui);
		multi_input.addProcessor(cam_mov);
		Gdx.input.setInputProcessor(multi_input);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(.8f, .8f, .8f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight());
		vp.apply();
		cam_mov.update();
		rend.setProjectionMatrix(camera.combined);
		mappone.disegnaTutto(batch, rend, cam_mov.computeMapSight());
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
	
	public Mappone getMappone() {
		return mappone;
	}
	
	public Random getRandom() {
		return random;
	}
}
