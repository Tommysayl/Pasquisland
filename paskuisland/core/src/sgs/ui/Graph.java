package sgs.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.MathUtils;
import com.kotcrab.vis.ui.building.utilities.Alignment;

public class Graph extends Table {
	
	private Vector2 MIN_POINT, MAX_POINT, origin, X, Y;
	private Vector2[] x_lines, y_lines;
	private Array<Vector2> points;
	private Array<Vector2> screen_points;
	private float[] verts;
	
	private Label x_name, y_name;
	private Label graph;
	
	private VerticalGroup y_label_group;
	private HorizontalGroup x_label_group;
	
	private Color graph_bg_col;
	private Color graph_line_col;
	
	private ShapeRenderer sr;
	
	public Graph(Skin skin, String y_axis, String x_axis) {
		super(skin);
		
		sr = new ShapeRenderer();
		
		graph = new Label("", skin);
		
		x_name = new Label(x_axis, skin);
		y_name = new Label(y_axis, skin);
		
		x_label_group = new HorizontalGroup();
		y_label_group = new VerticalGroup();
		
		for (int i = 1; i < 6; i++) {
			x_label_group.addActor(new Label(""+i, skin));
			y_label_group.addActor(new Label(""+(6-i), skin));
		}
		
		x_label_group.space(50);
		y_label_group.space(25);
		
		graph.setAlignment(Alignment.CENTER.getAlignment());
		
		MIN_POINT = new Vector2(0,0);
		MAX_POINT = new Vector2(10,10);
		
		add(y_name).top().left().colspan(3).row();
		add(y_label_group).right().padTop(10).fill();
		add(graph).center().center().fill().row();
		add().fill();
		add(x_label_group).left().top().padRight(10).fill();
		add(x_name).bottom().right();
		
		points = new Array<Vector2>();
		screen_points = new Array<Vector2>();
		
		graph_bg_col = Color.WHITE;
		graph_line_col = Color.LIGHT_GRAY;
		
		x_lines = y_lines = new Vector2[0];
		
		updateGraph();
	}
		
	public void draw(Batch batch, float palpha) {
		sr.setProjectionMatrix(getStage().getCamera().combined);
		drawGraphLines(sr);
	}
	
	
	private void drawGraphLines(ShapeRenderer sr) {	
		sr.begin(ShapeType.Filled);
		sr.setColor(graph_bg_col);
		sr.rect(origin.x, origin.y, X.x-origin.x, Y.y-origin.y);
		sr.end();
		sr.begin(ShapeType.Line);
		sr.setColor(Color.BLACK);
		sr.line(origin,Y);
		sr.line(origin, X);
		//GRAPH LINES
		sr.setColor(graph_line_col);	
		for (int i = 0; i < x_lines.length; i+=2) {
			sr.line(x_lines[i], x_lines[i+1]);
		}
		for (int i = 0; i < y_lines.length; i+=2) {
			sr.line(y_lines[i], y_lines[i+1]);
		}
		
		if (verts.length >= 4) {
			sr.setColor(Color.RED);
			sr.polyline(verts);
		}
		sr.end();
	}
	
	public void addAllPoints(Array<Vector2> npoints) 
	{
		this.points.addAll(npoints);
		updateGraph();
	}
	
	public void addPoint(Vector2 point) 
	{
		this.points.add(point);
		updateGraph();
	}
	
	public void addPoint(float x, float y) {
		addPoint(new Vector2(x,y));
	}
	
	public void setPoints(Array<Vector2> cpoints) 
	{
		this.points = cpoints;
		updateGraph();
	}
	
	public void clearPoints() 
	{
		this.points.clear();
		updateGraph();
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
		if (points.isEmpty()) {
			MIN_POINT.set(0, 0);
			MAX_POINT.set(1, 1);
		}
		else {
			MIN_POINT.set(points.first().x, min_y);
			MAX_POINT.set(points.get(points.size-1).x, max_y);
		}
		
		screen_points.clear();
		for (Vector2 point : points) {
			screen_points.add(graphToStageCoordinates(point));
		}
		
		// SETTING LABELS NAMES
		int x_label_count = x_label_group.getChildren().size;
		int y_label_count = y_label_group.getChildren().size;
		float[] x_labels_pos = new float[x_label_count];
		float[] y_labels_pos = new float[y_label_count];
		
		for (int xi = 0; xi < x_label_count; xi++)
			x_labels_pos[xi] = MathUtils.lerp(MIN_POINT.x, MAX_POINT.x, ((float)xi) / (x_label_count - 1)); 
		for (int yi = 0; yi < y_label_count; yi++)
			y_labels_pos[yi] =  MathUtils.lerp(MIN_POINT.y, MAX_POINT.y, ((float)y_label_count - yi -1) / (y_label_count - 1));  
		
		int li = 0;
		for (Actor actor : x_label_group.getChildren()) {
			((Label) actor).setText(String.format("%.2f", x_labels_pos[li]));
			li++;
		}
		li = 0;
		for (Actor actor : y_label_group.getChildren()) {
			((Label) actor).setText(String.format("%.2f",y_labels_pos[li]));
			li++;
		}
		
		verts = new float[2*screen_points.size];
		int i = 0;
		for (Vector2 point : screen_points) {
			verts[i] = point.x;
			verts[i+1] = point.y;
			i += 2;
		}
		
		//SETTING graph lines
		x_lines = new Vector2[2*x_label_count];
		y_lines = new Vector2[2*y_label_count];
		
		i = 0;
		for (; i < 2*x_label_count; i += 2){
			x_lines[i] = graphToStageCoordinates(new Vector2(x_labels_pos[i/2], MIN_POINT.y));
			x_lines[i+1] = graphToStageCoordinates(new Vector2(x_labels_pos[i/2], MAX_POINT.y));
		}
		
		i = 0;
		for (; i < 2*y_label_count; i += 2){
			y_lines[i] = graphToStageCoordinates(new Vector2(MIN_POINT.x, y_labels_pos[i/2]));
			y_lines[i+1] = graphToStageCoordinates(new Vector2(MAX_POINT.x,  y_labels_pos[i/2]));
		}
		
		//SETTING graph origin lines
		origin = graphToStageCoordinates(MIN_POINT);
		Y = graphToStageCoordinates(new Vector2(MIN_POINT.x, MAX_POINT.y));
		X = graphToStageCoordinates(new Vector2(MAX_POINT.x, MIN_POINT.y));
	}
	
	private Vector2 graphToStageCoordinates(Vector2 point) {
		Vector2 graph_actor_point = new Vector2(
				(point.x - MIN_POINT.x) / (MAX_POINT.x - MIN_POINT.x), 
				(point.y - MIN_POINT.y) / (MAX_POINT.y - MIN_POINT.y));
		
		//System.out.println(graph_actor_point.toString());
		//System.out.println("gw:"+graph.getWidth()+" gh:"+graph.getHeight()+" scale x, y "+graph.getScaleX()+" "+graph.getScaleY());
		
		graph_actor_point.scl(graph.getWidth(), graph.getHeight());
		return graph.localToStageCoordinates(graph_actor_point);
	}
	
	public void setGraphBackgroundColor(Color bg) {
		graph_bg_col = bg.cpy();
	}
	
	public void setGraphLinesColor(Color l_col) {
		graph_line_col = l_col.cpy();
	}
	

}
