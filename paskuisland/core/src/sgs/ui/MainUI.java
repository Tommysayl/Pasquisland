package sgs.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.viewport.Viewport;

import sgs.pasquisland.Pasquisland;

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
		ui_window = new Window("SETTINGS", skin);
		ui_window.setResizable(true);
		ui_window.setPosition(getWidth(), getHeight());
		ui_window.setSize(getWidth()/5, getHeight());
		getRoot().addCaptureListener(new InputListener() {
		    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
		        if (!(event.getTarget() instanceof TextField)) setKeyboardFocus(null);
		        return false;
		    }
		});
		
		main_table = new Table(skin);
		fps = new Label("FPS : ",skin);
		
		main_table.add(fps).left().top().expand();
		main_table.setFillParent(true);
		main_table.validate();
		main_table.layout();
		addActor(main_table);
		addActor(ui_window);
		setDebugAll(true);
	}

	private void startSimulation() {
		ui_window.getTitleLabel().setText("SIMULATION");
		((Pasquisland) Gdx.app.getApplicationListener()).startSimulation();
	};
	
	private void stopSimulation() {
		ui_window.getTitleLabel().setText("SETTINGS");
		((Pasquisland) Gdx.app.getApplicationListener()).stopSimulation();
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
