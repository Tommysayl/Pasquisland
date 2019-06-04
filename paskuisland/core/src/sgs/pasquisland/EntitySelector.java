package sgs.pasquisland;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import sgs.map.Mappone;

public class EntitySelector implements InputProcessor{
	
	//***** drawing variables
	private float screenX,screenY;
	private float rx, ry, rx2, ry2;
	private boolean active;
	private boolean calculatedPos;

	public EntitySelector() {
		super();
		rx = -1;
		ry = -1;
		rx2 = -1;
		ry2 = -1;
		screenX = -1;
		screenY = -1;
		active = false;
		calculatedPos = false;
	}
	
	public void transformCoord(OrthographicCamera cam) {
		if (active) {
			if (!calculatedPos) {
				calculatedPos = true;
				Vector3 coord = cam.unproject(new Vector3(screenX,screenY,0));
				rx = coord.x;
				ry = coord.y;
			}
			
			Vector3 coord = cam.unproject(new Vector3(screenX,screenY,0));
			rx2 = coord.x;
			ry2 = coord.y;
		}
	}
	
	public void render(ShapeRenderer rend) {
		if (active) {
			rend.begin(ShapeType.Filled);
			rend.setColor(1f, 1, 1, 0.2f);
			rend.rect(rx, ry, (rx2 - rx), (ry2 - ry));
			rend.end();
			rend.begin(ShapeType.Line);
			rend.setColor(1f, 1, 1, 1f);
			rend.rect(rx, ry, (rx2 - rx), (ry2 - ry));
			rend.end();
		}
	}
	
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		this.screenX = screenX;
		this.screenY = screenY;
		calculatedPos = false;
		active = true;
		return true;
	}
	
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		this.screenX = screenX;
		this.screenY = screenY;
		return true;
	}
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		active = false;
		Mappone.getInstance().vediRect(new Rectangle(rx, ry, (rx2-rx), (ry2 - ry)));
		return true;
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
