package sgs.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import sgs.map.WorldMap;
import sgs.pasquisland.Pasquisland;
import sgs.ui.MultiSlider.MultiSliderStyle;

public class MainUI extends Stage {
	
	//******* MAIN UI
	private Table main_table;
	private Table ui_table;
	private Table game_screen_table;
	
	private Label fps;
	
	//******* SETTINGS UI
	private TextField seed;
	private MultiSlider terrain;
	private Slider gaussian;
	private CheckBox apply_gaussian;
	
	//******** SIMULATION UI
	private Graph pop_graph;
	
	private Skin skin;
	private ShapeRenderer sr;
	private boolean is_in_simulation;

	public MainUI(Skin skin, Viewport viewport) {
		super(viewport);
		this.skin = skin;
		buildMainUI();
		buildSettingsUI();
		main_table.setFillParent(true);
		game_screen_table.validate();
		main_table.validate();
		main_table.layout();
		addActor(main_table);
		setDebugAll(true);
		sr = new ShapeRenderer();
	}

	public MainUI(Skin skin, Viewport viewport, Batch batch) {
		super(viewport, batch);
		this.skin = skin;
		buildMainUI();
		buildSettingsUI();
		main_table.setFillParent(true);
		game_screen_table.validate();
		main_table.validate();
		main_table.layout();
		addActor(main_table);
		setDebugAll(true);
		sr = new ShapeRenderer();
	}
	
	private void buildMainUI() {
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
		apply_gaussian = new CheckBox(" Apply Gaussian? ", skin);
		apply_gaussian.setChecked(true);
		gaussian = new Slider(0.001f, .2f, .01f, false, skin);
		gaussian.setValue(.1f);
		terrain = new MultiSlider(2, 0, .45f, .005f, false, new MultiSliderStyle(skin.get("default-horizontal", SliderStyle.class)));
		TextButton seed_button = new TextButton(" Random! ", skin);
		seed = new TextField(""+((Pasquisland) Gdx.app.getApplicationListener()).getMappone().getMap().getSeed(), skin);
		seed.setMaxLength(7);
		seed.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());
		TextButton start = new TextButton(" Start Simulation ", skin);
		
		ChangeListener updateMapListener = new ChangeListener() {public void changed (ChangeEvent event, Actor actor) {updateMap();}}; 

		seed.addListener(updateMapListener);
		terrain.addListener(updateMapListener);
		gaussian.addListener(updateMapListener);
		apply_gaussian.addListener(updateMapListener);
		seed_button.addListener(new ChangeListener() {public void changed (ChangeEvent event, Actor actor) {seed.setText(((Pasquisland) Gdx.app.getApplicationListener()).getRandom().nextInt(9999999)+"");updateMap();}});
		start.addListener(new ChangeListener() {public void changed (ChangeEvent event, Actor actor) {startSimulation();}});

		ui_table.add(apply_gaussian).colspan(3).row();
		ui_table.add(" Gaussian value ");
		ui_table.add(gaussian).colspan(2).row();
		ui_table.add(" Terrain Specs: ");
		ui_table.add(terrain).colspan(2).row();
		ui_table.add(" Seed: ");
		ui_table.add(seed).pad(5);
		ui_table.add(seed_button).row();
		ui_table.add(start).pad(10).colspan(3);
	}
	
	private void buildSimulationUI() {
		TextButton stop = new TextButton(" Stop Simulation ", skin);
		pop_graph = new Graph(skin, "population", "time");
		
		stop.addListener(new ChangeListener() {public void changed (ChangeEvent event, Actor actor) {stopSimulation();}});

		ui_table.add(pop_graph).row();
		ui_table.add(stop);
	}
	
	private void updateMap() {
		WorldMap map = ((Pasquisland) Gdx.app.getApplicationListener()).getMappone().getMap();
		int seed_val = map.getSeed();
		try {
			seed_val = Integer.parseInt(seed.getText());
		}
		catch(Exception ex) {
			
		}
		float[] terrain_vals = java.util.Arrays.copyOf(terrain.getKnobPositions(), 3);
		terrain_vals[2] = 1f;
		map.resetMapSettings(terrain_vals);
		map.resetMapGenSettings(
				map.getWidth(),map.getHeight(), seed_val,200,4,.3f, 2f, Vector2.Zero,apply_gaussian.isChecked(), gaussian.getValue());
		map.generateMap();
	}
	
	public void setGameScreenViewport(FillViewport game_vp) {
		game_vp.setScreenSize((int)(game_screen_table.getWidth()* getViewport().getScreenWidth() / getViewport().getWorldWidth()), 
				getViewport().getScreenHeight());
		game_vp.setWorldSize(game_vp.getScreenWidth(), game_vp.getScreenHeight());
		game_vp.setScreenPosition(getViewport().getLeftGutterWidth(), getViewport().getBottomGutterHeight());
	}
	
	private void startSimulation() {
		is_in_simulation = true;
		((Pasquisland) Gdx.app.getApplicationListener()).startSimulation();
		ui_table.clear();
		buildSimulationUI();
		main_table.layout();
		Gdx.app.getApplicationListener().resize((int)getWidth(), (int)getHeight());
	};
	
	private void stopSimulation() {
		is_in_simulation = false;
		ui_table.clear();
		buildSettingsUI();
		main_table.layout();
		Gdx.app.getApplicationListener().resize((int)getWidth(), (int)getHeight());
	}
	
	@Override
	public void act() {
		super.act();
		fps.setText("FPS: "+Gdx.graphics.getFramesPerSecond());
	}
	
	public void draw() {
		super.draw();
		
		if (is_in_simulation) {
			sr.setProjectionMatrix(getViewport().getCamera().combined);
			pop_graph.drawGraph(sr);
		}
	}

}
