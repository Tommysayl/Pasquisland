package sgs.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.MathUtils;

public class Graph extends Table {
	
	private Vector2 MIN_POINT, MAX_POINT;
	private Array<Vector2> points;
	private Array<Vector2> screen_points;
	
	private Label x_name, y_name;
	
	private boolean graphChanged;
	
	public Graph(Skin skin, String x_axis, String y_axis) {
		super(skin);
		
		x_name = new Label(x_axis, skin);
		y_name = new Label(y_axis, skin);
		
		MIN_POINT = new Vector2(10,0);
		MAX_POINT = new Vector2(0,10);
		
		add(x_name).top().left().row();
		add(y_name).bottom().right();
		
		points = new Array<Vector2>();
		screen_points = new Array<Vector2>();
	}

	public Graph(Skin skin) {
		super(skin);
	}
	
	public void drawGraph(ShapeRenderer sr) {
		drawGraphLines(sr);
	}
	
	private void updateGraph() {
		float min_y = Float.MAX_VALUE;
		float max_y = Float.MIN_VALUE;
		
		for (Vector2 point : points) {
			if (point.y < min_y)
				min_y = point.y;
			if (point.y > max_y)
				max_y = point.y;
		}
		
		MIN_POINT.set(points.first().x,  min_y);
		MAX_POINT.set(points.get(points.size-1).x, max_y);
		
		screen_points.clear();
		for (Vector2 point : points) {
			screen_points.add(toScreenCoordinates(point));
		}
	}
	
	private void drawGraphLines(ShapeRenderer sr) {
		
	}
	
	public void addAllPoints(Array<Vector2> npoints) 
	{
		this.points.addAll(npoints);
	}
	
	public void addPoint(Vector2 point) 
	{
		this.points.add(point);
	}
	
	public void setPoints(Array<Vector2> cpoints) 
	{
		this.points = cpoints;
	}
	
	public void clearPoints() 
	{
		this.points.clear();
	}
	
	private Vector2 toScreenCoordinates(Vector2 point) {
		Vector2 screen_point = new Vector2(
				(point.x - MIN_POINT.x) / MAX_POINT.x, 
				(point.y - MIN_POINT.y) / MAX_POINT.y);
		
		screen_point.scl(getWidth(), getHeight());
		screen_point.add(getX(), getY());
		
		return screen_point;
	}

}
