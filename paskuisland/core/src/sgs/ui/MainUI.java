package sgs.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainUI extends Stage {
	
	
	private Table main_table;
	private Table game_screen_table;
	private Label fps;
	private Skin skin;

	public MainUI(Skin skin, Viewport viewport) {
		super(viewport);
		this.skin = skin;
		buildUI();
	}

	public MainUI(Skin skin, Viewport viewport, Batch batch) {
		super(viewport, batch);
		this.skin = skin;
		buildUI();
	}
	
	private void buildUI() {
		main_table = new Table(skin);
		game_screen_table = new Table(skin);
		fps = new Label("FPS",skin);
		
		game_screen_table.add(fps).left().top().expand();
		//game_screen_table.setFillParent(true);
		main_table.add(game_screen_table).expand().fill().bottom().left();
		main_table.add("qua ci vanno grafici interfaccia e robe varie");
		
		main_table.setFillParent(true);
		game_screen_table.validate();
		main_table.validate();
		addActor(main_table);
		setDebugAll(true);
	}
	
	public void setGameScreenViewport(FillViewport game_vp) {
		game_vp.setScreenSize((int)(game_screen_table.getWidth()* getViewport().getScreenWidth() / getViewport().getWorldWidth()), 
				getViewport().getScreenHeight());
		game_vp.setWorldSize(game_vp.getScreenWidth(), game_vp.getScreenHeight());
		game_vp.setScreenPosition(getViewport().getLeftGutterWidth(), getViewport().getBottomGutterHeight());
		game_vp.apply();
	}

}
