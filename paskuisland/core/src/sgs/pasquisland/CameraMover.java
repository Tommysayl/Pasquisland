package sgs.pasquisland;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import sgs.map.WorldMap;

public class CameraMover extends InputAdapter{
	
	final float MAX_ZOOM = .5f;
	final float MIN_ZOOM = 6f;
	float zoom_speed = 4.75f;
	float speed = 1000;
	
	float dx, dy, dz;
	
	OrthographicCamera camera;
	
	int mapWidth, mapHeight;
	
	public CameraMover(OrthographicCamera cam, int mw, int mh) {
		camera = cam;
		dz = 0;
		dx = 0;
		dy = 0;
		mapWidth = mw;
		mapHeight = mh;
	}
	
	public int[] computeMapSight() {
		float x = (camera.position.x - camera.viewportWidth/2*camera.zoom) / WorldMap.tile_size;
		float y = (camera.position.y - camera.viewportHeight/2*camera.zoom) / WorldMap.tile_size;
		float width = (camera.viewportWidth*camera.zoom + WorldMap.tile_size*(4*camera.zoom + 2))/ WorldMap.tile_size;
		float height = (camera.viewportHeight*camera.zoom + WorldMap.tile_size*(4*camera.zoom + 2))/ WorldMap.tile_size;
		
		return  new int[]{(int)(x-(2*camera.zoom + 1)), (int)(y-(2*camera.zoom + 1)), (int) width, (int) height};
	}
	
	public void update() {
		camera.zoom = MathUtils.clamp(camera.zoom + dz, MAX_ZOOM, MIN_ZOOM);
		//dx = isOutBoundX(dx)? 0 : dx;
		//dy = isOutBoundY(dy)? 0 : dy;
		camera.translate(dx, dy);
		camera.position.set(
				MathUtils.clamp(camera.position.x, 0, WorldMap.tile_size * mapWidth), 
				MathUtils.clamp(camera.position.y, 0, WorldMap.tile_size * mapHeight),
				0);
	}
	
	@Override
	public boolean scrolled(int amount) {
		float zoom = camera.zoom + Gdx.graphics.getDeltaTime()*amount*zoom_speed;
		zoom = MathUtils.clamp(zoom, MAX_ZOOM, MIN_ZOOM);
		camera.zoom = zoom;
		dx = Math.signum(dx) * Gdx.graphics.getDeltaTime()*speed*camera.zoom;
		dy = Math.signum(dy) * Gdx.graphics.getDeltaTime()*speed*camera.zoom;
		return true;
	}
	
	@Override
	public boolean keyDown(int keycode) {
		boolean handled = false;
		
		switch(keycode) {
			case Keys.D:
				dx = Gdx.graphics.getDeltaTime()*speed*camera.zoom;
				handled = true;
				break;
			case Keys.A:
				dx = -Gdx.graphics.getDeltaTime()*speed*camera.zoom;
				handled = true;
				break;
			case Keys.W:
				dy = Gdx.graphics.getDeltaTime()*speed*camera.zoom;
				handled = true;
				break;
			case Keys.S:
				dy = -Gdx.graphics.getDeltaTime()*speed*camera.zoom;
				handled = true;
				break;
			case Keys.Z:
				dz = -Gdx.graphics.getDeltaTime()*zoom_speed;
				handled = true;
				break;
			case Keys.X:
				dz = Gdx.graphics.getDeltaTime()*zoom_speed;
				handled = true;
				break;
		}
		
		return handled;
	}

	@Override
	public boolean keyUp(int keycode) {
		boolean handled = false;
		
		switch(keycode) {
			case Keys.D:
				dx = 0;
				handled = true;
				break;
			case Keys.A:
				dx = 0;
				handled = true;
				break;
			case Keys.W:
				dy = 0;
				handled = true;
				break;
			case Keys.S:
				dy = 0;
				handled = true;
				break;
			case Keys.Z:
				dz = 0;
				handled = true;
				break;
			case Keys.X:
				dz = 0;
				handled = true;
				break;
		}
		
		return handled;
	}
	
	private boolean isOutBoundX(float dx) {
		return (camera.position.x + dx - camera.viewportWidth/2 * camera.zoom < 0 ||
				WorldMap.tile_size * mapWidth < camera.position.x + dx + camera.viewportWidth / 2 * camera.zoom);		
	}
	
	private boolean isOutBoundY(float dy) {
		return (camera.position.y + dy - camera.viewportHeight/2 * camera.zoom < 0 ||
				WorldMap.tile_size * mapHeight < camera.position.y + dy + camera.viewportHeight / 2 * camera.zoom);		
	}
}
