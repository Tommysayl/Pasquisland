package sgs.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import sgs.map.WorldMap;
import sgs.pasquisland.Pasquisland;

public class MainUI extends Stage {
	
	
	private Table main_table;
	private Table ui_table;
	private Table game_screen_table;
	
	private Label fps;
	
	private TextField seed;
	
	private Skin skin;

	public MainUI(Skin skin, Viewport viewport) {
		super(viewport);
		this.skin = skin;
		buildUI();
		buildSettingsUI();
		main_table.setFillParent(true);
		game_screen_table.validate();
		main_table.validate();
		main_table.layout();
		addActor(main_table);
		setDebugAll(true);
	}

	public MainUI(Skin skin, Viewport viewport, Batch batch) {
		super(viewport, batch);
		this.skin = skin;
		buildUI();
		buildSettingsUI();
		main_table.setFillParent(true);
		game_screen_table.validate();
		main_table.validate();
		main_table.layout();
		addActor(main_table);
		setDebugAll(true);
	}
	
	private void buildUI() {
		getRoot().addCaptureListener(new InputListener() {
		    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
		        if (!(event.getTarget() instanceof TextField)) setKeyboardFocus(null);
		        return false;
		    }
		});
		main_table = new Table(skin);
		game_screen_table = new Table(skin);
		ui_table = new Table(skin);
		
		fps = new Label("FPS : ",skin);
		
		game_screen_table.add(fps).left().top().expand();
		//game_screen_table.setFillParent(true);
		main_table.add(game_screen_table).expand().fill().bottom().left();
		main_table.add(ui_table).fill().expandY().left().bottom();
	}
	
	private void buildSettingsUI() {
		TextButton seed_button = new TextButton(" Random! ", skin);
		seed_button.addListener(new ChangeListener() {public void changed (ChangeEvent event, Actor actor) {seed.setText(((Pasquisland) Gdx.app.getApplicationListener()).getRandom().nextInt(9999999)+"");updateMap();}});
		seed = new TextField(""+((Pasquisland) Gdx.app.getApplicationListener()).getMappone().getMap().getSeed(), skin);
		seed.setMaxLength(7);
		seed.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());
		TextButton start = new TextButton(" Start Simulation ", skin);
		
		ChangeListener updateMapListener = new ChangeListener() {public void changed (ChangeEvent event, Actor actor) {updateMap();}}; 

		seed.addListener(updateMapListener);
		
		ui_table.add(" Seed: ");
		ui_table.add(seed).pad(5);
		ui_table.add(seed_button).row();
		ui_table.add(start).pad(10).colspan(3);
	}
	
	private void updateMap() {
		WorldMap map = ((Pasquisland) Gdx.app.getApplicationListener()).getMappone().getMap();
		int seed_val = map.getSeed();
		try {
			seed_val = Integer.parseInt(seed.getText());
		}
		catch(Exception ex) {
			
		}
		map.resetMapGenSettings(
				map.getWidth(),map.getHeight(), seed_val,200,4,.3f, 2f, Vector2.Zero,true);
		map.generateMap();
	}
	
	public void setGameScreenViewport(FillViewport game_vp) {
		game_vp.setScreenSize((int)(game_screen_table.getWidth()* getViewport().getScreenWidth() / getViewport().getWorldWidth()), 
				getViewport().getScreenHeight());
		game_vp.setWorldSize(game_vp.getScreenWidth(), game_vp.getScreenHeight());
		game_vp.setScreenPosition(getViewport().getLeftGutterWidth(), getViewport().getBottomGutterHeight());
	}
	
	@Override
	public void act() {
		super.act();
		fps.setText("FPS: "+Gdx.graphics.getFramesPerSecond());
	}

}
