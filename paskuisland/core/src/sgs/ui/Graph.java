package sgs.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

public class Graph extends Table {
	
	private Vector2 MAX_X, MAX_Y;
	private Array<Vector2> points;
	
	private Label x_name, y_name;
	private Label[] x_labels;
	private Label[] y_labels;
	
	private boolean graphChanged;
	
	public Graph(Skin skin, String x_axis, String y_axis) {
		super(skin);
		
		x_name = new Label(x_axis, skin);
		y_name = new Label(y_axis, skin);
		
		MAX_X = new Vector2(10,0);
		MAX_Y = new Vector2(0,10);
		
		add(x_name).top().left().row();
		add(y_name).bottom().right();
	}

	public Graph(Skin skin) {
		super(skin);
	}
	
	public void drawGraph(ShapeRenderer sr) {
		drawGraphLines(sr);
	}
	
	public void act(float delta) {
		super.act(delta);
		if (graphChanged)
			updateGraph();
	}
	
	private void updateGraph() {
		
	}
	
	private void drawGraphLines(ShapeRenderer sr) {
		sr.begin(ShapeType.Line);
		sr.line(Vector2.Zero, MAX_X);
		sr.line(Vector2.Zero, MAX_Y);
		sr.end();
	}
	
	private void plotLine() {
		
	}

}
