package sgs.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
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
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import sgs.map.WorldMap;
import sgs.pasquisland.Pasquisland;
import sgs.ui.MultiSlider.MultiSliderStyle;

public class MainUI extends Stage {
	
	//******* MAIN UI
	private Window ui_window;
	private Table main_table;
	
	private Label fps;
		
	private Skin skin;

	public MainUI(Skin skin, Viewport viewport) {
		super(viewport);
		init(skin);
	}

	public MainUI(Skin skin, Viewport viewport, Batch batch) {
		super(viewport, batch);
		init(skin);
	}
	
	private void init(Skin skin) {
		this.skin = skin;
		ui_window = new Window("TITLE", skin);
		ui_window.setResizable(true);
		buildMainUI();
		main_table.setFillParent(true);
		main_table.validate();
		main_table.layout();
		addActor(main_table);
		addActor(ui_window);
		setDebugAll(true);
	}
	
	private void buildMainUI() {
		getRoot().addCaptureListener(new InputListener() {
		    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
		        if (!(event.getTarget() instanceof TextField)) setKeyboardFocus(null);
		        return false;
		    }
		});
		
		main_table = new Table(skin);
		fps = new Label("FPS : ",skin);
		
		main_table.add(fps).left().top().expand();
	}
	

	private void startSimulation() {
		((Pasquisland) Gdx.app.getApplicationListener()).startSimulation();
		main_table.layout();
		Gdx.app.getApplicationListener().resize((int)getWidth(), (int)getHeight());
	};
	
	private void stopSimulation() {
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
	}

}
