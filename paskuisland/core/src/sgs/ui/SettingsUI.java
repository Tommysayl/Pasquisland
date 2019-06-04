package sgs.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;

import sgs.map.WorldMap;
import sgs.pasquisland.Pasquisland;
import sgs.ui.MultiSlider.MultiSliderStyle;

public class SettingsUI extends Table {
	/*
	 * 	//******* SETTINGS UI
	private TextField seed;
	private MultiSlider terrain;
	private Slider gaussian;
	private CheckBox apply_gaussian;
	 * 
	 * 	private void buildSettingsUI() {
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

		add(apply_gaussian).colspan(3).row();
		ui_table.add(" Gaussian value ");
		ui_table.add(gaussian).colspan(2).row();
		ui_table.add(" Terrain Specs: ");
		ui_table.add(terrain).colspan(2).row();
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
		float[] terrain_vals = java.util.Arrays.copyOf(terrain.getKnobPositions(), 3);
		terrain_vals[2] = 1f;
		map.resetMapSettings(terrain_vals);
		map.resetMapGenSettings(
				map.getWidth(),map.getHeight(), seed_val,200,4,.3f, 2f, Vector2.Zero,apply_gaussian.isChecked(), gaussian.getValue());
		map.generateMap();
	}
	 */
}
