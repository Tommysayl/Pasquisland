package sgs.pasquisland;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;

import sgs.ui.MainUI;

/**
 * Ciao belli,
 * questa è la classe che gestisce tutte le cose , è la classe centrale che chiama
 * e crea le altre classi!
 *
 */
public class Pasquisland extends ApplicationAdapter {
	
	OrthographicCamera camera;
	FitViewport vp;
	
	MainUI ui;
	Skin skin;
	
	@Override
	public void create () {
		
		camera = new OrthographicCamera();
		vp = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
		
		skin = new Skin(Gdx.files.internal("skins/metal/metal-ui.json"));
		ui = new MainUI(skin, new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), new SpriteBatch());
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(.8f, .8f, .8f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		ui.act();
		ui.draw();
	}
	
	@Override
	public void dispose () {
		
	}
}
